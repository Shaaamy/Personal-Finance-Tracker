package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.UserDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.handler.DuplicateResourceException;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User createUser(User user) {

        if(userRepo.existsByUsername(user.getUsername())){
            throw new DuplicateResourceException("Username '" + user.getUsername() + "' already exists");
        }
        if(userRepo.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' already exists");
        }
        return userRepo.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        existingUser.setBalance(userDetails.getBalance());
        existingUser.setFullName(userDetails.getFullName());
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setEmail(userDetails.getEmail());

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
}
