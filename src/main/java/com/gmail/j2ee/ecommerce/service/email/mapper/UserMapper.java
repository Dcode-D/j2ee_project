package com.gmail.j2ee.ecommerce.service.email.mapper;

import com.gmail.j2ee.ecommerce.domain.User;
import com.gmail.j2ee.ecommerce.dto.HeaderResponse;
import com.gmail.j2ee.ecommerce.dto.perfume.PerfumeResponse;
import com.gmail.j2ee.ecommerce.dto.user.BaseUserResponse;
import com.gmail.j2ee.ecommerce.dto.user.UpdateUserRequest;
import com.gmail.j2ee.ecommerce.dto.user.UserResponse;
import com.gmail.j2ee.ecommerce.service.UserService;
import com.gmail.j2ee.ecommerce.exception.InputFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CommonMapper commonMapper;
    private final UserService userService;

    public UserResponse getUserById(Long userId) {
        return commonMapper.convertToResponse(userService.getUserById(userId), UserResponse.class);
    }

    public UserResponse getUserInfo(String email) {
        return commonMapper.convertToResponse(userService.getUserInfo(email), UserResponse.class);
    }

    public List<PerfumeResponse> getCart(List<Long> perfumesIds) {
        return commonMapper.convertToResponseList(userService.getCart(perfumesIds), PerfumeResponse.class);
    }

    public HeaderResponse<BaseUserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAllUsers(pageable);
        return commonMapper.getHeaderResponse(users.getContent(), users.getTotalPages(), users.getTotalElements(), BaseUserResponse.class);
    }

    public UserResponse updateUserInfo(String email, UpdateUserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        User user = commonMapper.convertToEntity(userRequest, User.class);
        return commonMapper.convertToResponse(userService.updateUserInfo(email, user), UserResponse.class);
    }
}
