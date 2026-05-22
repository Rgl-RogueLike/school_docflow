package com.haritonov.school.docflow.modules.user.service;

import com.haritonov.school.docflow.modules.employee.model.Employee;
import com.haritonov.school.docflow.modules.user.model.AppUser;
import com.haritonov.school.docflow.modules.user.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            return appUserRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        }
        throw new IllegalStateException("Not authenticated");
    }

    @Override
    public Employee getCurrentEmployee() {
        return getCurrentUser().getEmployee();
    }

    @Override
    public Long getCurrentEmployeeId() {
        return getCurrentEmployee().getId();
    }

    @Override
    public String getCurrentEmployeeFullName() {
        Employee employee = getCurrentEmployee();
        return employee.getLastName() + " " + employee.getFirstName() +
                (employee.getPatronymic() != null ? " " + employee.getPatronymic() : "");
    }
}
