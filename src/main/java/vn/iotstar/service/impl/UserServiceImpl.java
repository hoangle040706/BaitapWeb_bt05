package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.LoginRequest;
import vn.iotstar.dto.RegisterRequest;
import vn.iotstar.dto.UserDto;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();
        String fullname = request.getFullname().trim();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("confirmPassword", "Mật khẩu nhập lại không khớp");
        }
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new BusinessException("username", "Tên đăng nhập đã được sử dụng");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("email", "Email đã được đăng ký");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .fullname(fullname)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(1)
                .build();

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto authenticate(LoginRequest request) {
        String identifier = request.getUsernameOrEmail().trim();
        User user = userRepository.findByUsernameOrEmail(identifier)
                .orElseThrow(() -> new BusinessException("Tên đăng nhập/email hoặc mật khẩu không đúng"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Tên đăng nhập/email hoặc mật khẩu không đúng");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException("Tài khoản hiện đang bị khóa");
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
}
