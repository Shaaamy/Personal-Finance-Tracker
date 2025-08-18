package com.NtgSummerTrainingApp.PersonalFinanceTracker.Controllers;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Services.UserService;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.UserDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //
        // create new user
    //
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        User user1 = userService.createUser(user);
        return new ResponseEntity<>(UserMapper.toDTO(user1), HttpStatus.CREATED);
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
    @GetMapping
    public ResponseEntity<PaginationDto<UserDto>> getAllUsers(@ModelAttribute PaginationRequest paginationReq) {
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
}
