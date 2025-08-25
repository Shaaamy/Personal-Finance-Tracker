package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import static com.NtgSummerTrainingApp.PersonalFinanceTracker.handler.BusinessExceptions.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.PasswordResetToken;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.PasswordResetTokenRepository;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordResetTokenRepository tokenRepository;
    private final MyUserDetailsService userDetailsService;
    private final EmailService emailService;
    public LoginResponseDto  createUser(User user) {

        if(userRepo.existsByUsername(user.getUsername())){
            throw new DuplicateResourceException("Username '" + user.getUsername() + "' already exists");
        }
        if(userRepo.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);
        return new LoginResponseDto(user.getId(), user.getUsername(), user.getFullName(),user.getEmail(), user.getRole().name(),accessToken,refreshToken);

    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        if(userDetails.getPassword() != null){
            String hashedPassword = passwordEncoder.encode(userDetails.getPassword());
            existingUser.setPassword(hashedPassword);
        }
        existingUser.setBalance(userDetails.getBalance());
        existingUser.setFullName(userDetails.getFullName());
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());

        return userRepo.save(existingUser);
    }


    public PaginationDto<UserDto> getAllUsers(PaginationRequest paginationReq) {
        Page<User> users = userRepo.findAll(PaginationHelper.getPageable(paginationReq));
        if(users.isEmpty()){
            throw new EntityNotFoundException("No Users Found");
        }
        Page<UserDto> usersDtoPage = users.map(UserMapper::toDTO);
        return PaginationMapper.toPaginatedDto(usersDtoPage);
    }


    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        return UserMapper.toDTO(user);
    }

    public String deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userRepo.delete(user);
        return "User with id " +id+" is deleted successfully";
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto ) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            //Fetching the user twice is redundant
            //The authentication step already loads the user using your UserDetailsService ---> ( MyUserDetailsService). You can get the authenticated user from:
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepo.findByUsername(principal.getUsername());
            String accessToken = jwtService.generateAccessToken(principal);
            String refreshToken = jwtService.generateRefreshToken(principal);

            return new LoginResponseDto(
                    user.getId(),
                    principal.getUsername(),
                    user.getFullName(), // from your User entity or principal
                    user.getEmail(),
                    user.getRole().name(),
                    accessToken,
                    refreshToken// assuming you store a single role, or adapt to list
            );        }
        throw new BadCredentialsException("Invalid username or password");
    }

    public String refreshToken(String refreshTokenHeader) {
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
            throw new TokenException("No valid token provided");
        }

        String refreshToken = refreshTokenHeader.substring(7);
        String tokenType = jwtService.extractClaim(refreshToken, claims -> (String) claims.get("tokenType"));

        if (!"REFRESH".equals(tokenType)) {
            throw new TokenException("Invalid token type");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.validateToken(refreshToken, userDetails)) {
            throw new TokenException("Invalid refresh token");
        }

        return jwtService.generateAccessToken(userDetails);
    }

    public String logout(String authHeader, String refreshHeader) {
        boolean alreadyLoggedOut = false;
        boolean newlyLoggedOut = false;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            if (tokenBlacklistService.isTokenBlacklisted(accessToken)) {
                alreadyLoggedOut = true;
            } else {
                tokenBlacklistService.blacklistToken(accessToken);
                newlyLoggedOut = true;
            }
        }

        if (refreshHeader != null && refreshHeader.startsWith("Bearer ")) {
            String refreshToken = refreshHeader.substring(7);
            if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
                alreadyLoggedOut = true;
            } else {
                tokenBlacklistService.blacklistToken(refreshToken);
                newlyLoggedOut = true;
            }
        }

        if (alreadyLoggedOut && !newlyLoggedOut) {
            return "Already logged out";
        } else if (newlyLoggedOut) {
            return "Logout successful";
        } else {
            return "No valid token provided";
        }
    }

    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // token valid 1 hour

        tokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());

        return resetToken.getToken();
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid password reset token"));

        if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new ExpiredPasswordResetTokenException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        tokenRepository.delete(resetToken); // invalidate token
    }
}