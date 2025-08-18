package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.CategoryMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.PaginationMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.Mapper.UserMapper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.CategoryResponseDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.PaginationRequest;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.dto.UserDto;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.helper.PaginationHelper;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

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


    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        userRepo.delete(user);
    }
}
