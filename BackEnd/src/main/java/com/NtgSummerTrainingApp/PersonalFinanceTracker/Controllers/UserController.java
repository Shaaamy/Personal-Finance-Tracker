package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.JwtService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.TokenBlacklistService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.UserService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.*;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    //
        // create new user
    //
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponseDto>> register(@Valid @RequestBody RegisterDto registerDto) {
        LoginResponseDto response = userService.createUser(registerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully", response));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = userService.login(loginRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    //
        // update user
    //
//    @PutMapping
//    public ResponseEntity<ApiResponse<UserDto>> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal,@Valid @RequestBody UserDto userDto) {
//            Long loggedInUserId = userPrincipal.getUser().getId();
//            userDto.setId(loggedInUserId);
//            User user = UserMapper.toEntity(userDto);
//            User updatedUser = userService.updateUser(loggedInUserId, user);
//            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", UserMapper.toDTO(updatedUser)));
//    }


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
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<ApiResponse<String>> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader) {
        String newAccessToken = userService.refreshToken(refreshTokenHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, "New access token generated", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "Refresh-Token", required = false) String refreshHeader) {

        String logoutStatus = userService.logout(authHeader, refreshHeader);

        return switch (logoutStatus) {
            case "Logout successful" -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Logged out successfully", null)
            );
            case "Already logged out" -> ResponseEntity.ok(
                    new ApiResponse<>(true, "Already logged out", null)
            );
            default -> ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "No valid token provided", null)
            );
        };
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        String token = userService.forgotPassword(email);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Password reset token generated. Check email for token.", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String token,
                                                             @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Password has been reset successfully", null)
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set-role")
    public ResponseEntity<ApiResponse<String>> setUserRole(@RequestBody Long userId, @RequestBody String role) {
        String result = userService.setUserRole(userId, role);
        return ResponseEntity.ok(new ApiResponse<>(true, "User role updated successfully", result));
    }
}
