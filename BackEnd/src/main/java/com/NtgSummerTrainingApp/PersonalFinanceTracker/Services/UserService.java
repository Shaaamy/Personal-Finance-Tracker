package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.LoginResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.UserDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.handler.DuplicateResourceException;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDto  createUser(User user) {

        if(userRepo.existsByUsername(user.getUsername())){
            throw new DuplicateResourceException("Username '" + user.getUsername() + "' already exists");
        }
        if(userRepo.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponseDto(user.getId(),token, user.getUsername(), user.getFullName(),user.getEmail(), user.getRole().name());

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

    public LoginResponseDto login(UserDto userDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        if (authentication.isAuthenticated()) {
            //Fetching the user twice is redundant
            //The authentication step already loads the user using your UserDetailsService ---> ( MyUserDetailsService). You can get the authenticated user from:
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepo.findByUsername(principal.getUsername());
            String token = jwtService.generateToken(principal.getUsername());

            return new LoginResponseDto(
                    user.getId(),
                    token,
                    principal.getUsername(),
                    user.getFullName(), // from your User entity or principal
                    user.getEmail(),
                    user.getRole().name()      // assuming you store a single role, or adapt to list
            );        }
        throw new BadCredentialsException("Invalid username or password");
    }
}