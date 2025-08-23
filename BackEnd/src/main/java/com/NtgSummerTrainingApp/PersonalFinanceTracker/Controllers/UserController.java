package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.JwtService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TokenBlacklistService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.UserService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
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
    public ResponseEntity<ApiResponse<LoginResponseDto>> register(@RequestBody UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        LoginResponseDto response = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully", response));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody UserDto userDto) {
        LoginResponseDto response = userService.login(userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    //
        // update user
    //
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            User user = UserMapper.toEntity(userDto);
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", UserMapper.toDTO(updatedUser)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    //
        // get all users
    //
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PaginationDto<UserDto>>> getAllUsers(@ModelAttribute PaginationRequest paginationReq) {
        PaginationDto<UserDto> users = userService.getAllUsers(paginationReq);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", users));
    }


    //
        // get user by id
    //
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully", userDto));
    }


    //
    // delete user
    //
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        String result = userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", result));
    }

    // Generate refresh Token

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader.substring(7);

        String tokenType = jwtService.extractClaim(refreshToken, claims -> (String) claims.get("tokenType"));
        if (!"REFRESH".equals(tokenType)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid token type", null));
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            return ResponseEntity.ok(new ApiResponse<>(true, "New access token generated", newAccessToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Invalid refresh token", null));
    }



    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
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
            return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No valid token provided", null));
        }
    }
}
