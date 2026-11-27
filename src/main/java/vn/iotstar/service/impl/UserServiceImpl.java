package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.iotstar.dto.LoginRequest;
import vn.iotstar.dto.RegisterRequest;
import vn.iotstar.dto.UserDto;
import vn.iotstar.dto.UserRequest;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.exception.BusinessException;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.spec.UserSpecifications;
import vn.iotstar.service.UserService;

import java.util.Objects;
import java.util.Optional;

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

        validateConfirmPassword(request.getPassword(), request.getConfirmPassword());
        validateUniqueCredentials(username, email, null);

        User user = userMapper.toEntity(request);
        user.setUsername(username);
        user.setEmail(email);
        user.setFullname(request.getFullname().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(1);

        return userMapper.toDto(userRepository.save(user));
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
    public Page<UserDto> search(String keyword, Role role, Integer status, Pageable pageable) {
        return userRepository.findAll(UserSpecifications.withFilters(keyword, role, status), pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRequest> findRequestById(Long id) {
        return userRepository.findById(id).map(userMapper::toRequest);
    }

    @Override
    @Transactional
    public UserDto create(UserRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        validateConfirmPassword(request.getPassword(), request.getConfirmPassword());
        validateUniqueCredentials(username, email, null);

        User user = userMapper.toEntity(request);
        applyNormalizedFields(user, username, email, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserRequest request) {
        User existing = userRepository.findById(request.getId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng cần cập nhật"));

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        validateUniqueCredentials(username, email, existing.getId());

        if (StringUtils.hasText(request.getPassword())) {
            validateConfirmPassword(request.getPassword(), request.getConfirmPassword());
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userMapper.updateEntity(request, existing);
        applyNormalizedFields(existing, username, email, request);

        return userMapper.toDto(userRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteById(Long targetId, Long currentUserId) {
        if (Objects.equals(targetId, currentUserId)) {
            throw new BusinessException("Không thể tự xóa tài khoản đang đăng nhập.");
        }
        if (!userRepository.existsById(targetId)) {
            throw new BusinessException("Người dùng không tồn tại.");
        }
        userRepository.deleteById(targetId);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    private void validateConfirmPassword(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new BusinessException("confirmPassword", "Mật khẩu nhập lại không khớp");
        }
    }

    private void validateUniqueCredentials(String username, String email, Long excludeId) {
        boolean usernameTaken = (excludeId == null)
                ? userRepository.existsByUsernameIgnoreCase(username)
                : userRepository.existsByUsernameIgnoreCaseAndIdNot(username, excludeId);
        if (usernameTaken) {
            throw new BusinessException("username", "Tên đăng nhập đã được sử dụng");
        }

        boolean emailTaken = (excludeId == null)
                ? userRepository.existsByEmailIgnoreCase(email)
                : userRepository.existsByEmailIgnoreCaseAndIdNot(email, excludeId);
        if (emailTaken) {
            throw new BusinessException("email", "Email đã được đăng ký");
        }
    }

    private void applyNormalizedFields(User user, String username, String email, UserRequest request) {
        user.setUsername(username);
        user.setEmail(email);
        user.setFullname(request.getFullname().trim());
        user.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone().trim() : null);
        user.setAvatar(StringUtils.hasText(request.getAvatar()) ? request.getAvatar().trim() : null);
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);
    }
}
