package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.dto.LoginRequest;
import vn.iotstar.dto.RegisterRequest;
import vn.iotstar.dto.UserDto;
import vn.iotstar.dto.UserRequest;
import vn.iotstar.entity.Role;

import java.util.Optional;

public interface UserService {

    UserDto register(RegisterRequest request);

    UserDto authenticate(LoginRequest request);

    Page<UserDto> search(String keyword, Role role, Integer status, Pageable pageable);

    Optional<UserRequest> findRequestById(Long id);

    UserDto create(UserRequest request);

    UserDto update(UserRequest request);

    void deleteById(Long targetId, Long currentUserId);

    long count();
}
