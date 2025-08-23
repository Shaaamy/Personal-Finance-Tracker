package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.JwtService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TokenBlacklistService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.UserService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.LoginResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.UserDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserDetailsService userDetailsService;

    //
        // create new user
    //
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.login(userDto),HttpStatus.OK);
    }

    //
        // update user
    //
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            User user = UserMapper.toEntity(userDto);
            User updatedUser = userService.updateUser(id, user);
            return new ResponseEntity<>(UserMapper.toDTO(updatedUser), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    //
        // get all users
    //
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers(@ModelAttribute PaginationRequest paginationReq) {
            return new ResponseEntity<>(userService.getAllUsers(paginationReq),HttpStatus.OK);
    }


    //
        // get user by id
    //
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }



    //
    // delete user
    //
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUser(id),HttpStatus.OK);

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader.substring(7);

        String tokenType = jwtService.extractClaim(refreshToken, claims -> (String) claims.get("tokenType"));
        if (!"REFRESH".equals(tokenType)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token type");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            return ResponseEntity.ok(newAccessToken);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                         @RequestHeader(value = "Refresh-Token", required = false) String refreshHeader) {

        boolean anyTokenBlacklisted = false;


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklistService.blacklistToken(authHeader.substring(7));
            anyTokenBlacklisted = true;
        }


        if (refreshHeader != null && refreshHeader.startsWith("Bearer ")) {
            tokenBlacklistService.blacklistToken(refreshHeader.substring(7));
            anyTokenBlacklisted = true;
        }

        if (anyTokenBlacklisted) {
            return ResponseEntity.ok("Logged out successfully");
        }else {
            return ResponseEntity.badRequest().body("No valid token provided");
        }
    }
}
