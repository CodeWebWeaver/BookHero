package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.enums.RoleName;
import com.parkhomovsky.bookstore.exception.RegistrationException;
import com.parkhomovsky.bookstore.mapper.UserMapper;
import com.parkhomovsky.bookstore.model.Role;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.role.RoleRepository;
import com.parkhomovsky.bookstore.repository.user.UserRepository;
import com.parkhomovsky.bookstore.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserRegistrationResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Error during registration, wrong data entered");
        }
        User user = userMapper.toModel(request);
        Role role = roleRepository.getByName(RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toRegistrationResponse(userRepository.save(user));
    }

    @Override
    public long getAuthenticatedUserId() {
        return ((User) getAuthenticatedUserDetails()).getId();
    }

    @Override
    public UserDetails getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (UserDetails) principal;
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) getAuthenticatedUserDetails();
    }
}
