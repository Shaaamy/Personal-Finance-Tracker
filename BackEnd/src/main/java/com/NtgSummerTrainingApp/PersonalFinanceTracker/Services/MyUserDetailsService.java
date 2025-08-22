package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.User;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.models.UserPrincipal;
import com.NtgSummerTrainingApp.PersonalFinanceTracker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null)
            throw new EntityNotFoundException("User Not Found");
        return new UserPrincipal(user);
    }
}
