package vn.iotstar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedUserIfMissing("admin", "admin@rangxay.vn", "Quản trị viên", "Admin@123", Role.ADMIN, 1);
        seedUserIfMissing("user", "user@rangxay.vn", "Khách hàng mẫu", "User@123", Role.USER, 1);
        seedUserIfMissing("minhhoang", "hoang@rangxay.vn", "Lê Minh Hoàng", "User@123", Role.ADMIN, 1);
        seedUserIfMissing("thuhuong", "huong@rangxay.vn", "Trần Thu Hương", "User@123", Role.USER, 1);
        seedUserIfMissing("quocbao", "bao@rangxay.vn", "Phạm Quốc Bảo", "User@123", Role.USER, 0);

        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                    Category.builder().name("Cà phê Arabica Cầu Đất").images("arabica.jpg").status(1).build(),
                    Category.builder().name("Cà phê Robusta Honey").images("robusta.jpg").status(1).build(),
                    Category.builder().name("Dụng cụ Pour Over V60").images("pour-over.jpg").status(1).build(),
                    Category.builder().name("Phin & Bình pha Espresso").images("phin-espresso.jpg").status(1).build(),
                    Category.builder().name("Cà phê Cold Brew Đặc sản").images("cold-brew.jpg").status(1).build(),
                    Category.builder().name("Máy xay cà phê thủ công").images("grinder.jpg").status(1).build(),
                    Category.builder().name("Ấm cổ ngỗng rót nước").images("kettle.jpg").status(1).build()
            ));
        } else if (categoryRepository.count() == 4) {
            categoryRepository.saveAll(List.of(
                    Category.builder().name("Cà phê Cold Brew Đặc sản").images("cold-brew.jpg").status(1).build(),
                    Category.builder().name("Máy xay cà phê thủ công").images("grinder.jpg").status(1).build(),
                    Category.builder().name("Ấm cổ ngỗng rót nước").images("kettle.jpg").status(1).build()
            ));
        }
    }

    private void seedUserIfMissing(String username, String email, String fullname,
                                   String rawPassword, Role role, int status) {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty()) {
            userRepository.save(User.builder()
                    .username(username)
                    .email(email)
                    .fullname(fullname)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(role)
                    .status(status)
                    .build());
        }
    }
}
