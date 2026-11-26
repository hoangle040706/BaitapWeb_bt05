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
        if (userRepository.findByUsernameIgnoreCase("admin").isEmpty()) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@rangxay.vn")
                    .fullname("Quản trị viên")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .status(1)
                    .build());
        }

        if (userRepository.findByUsernameIgnoreCase("user").isEmpty()) {
            userRepository.save(User.builder()
                    .username("user")
                    .email("user@rangxay.vn")
                    .fullname("Khách hàng mẫu")
                    .password(passwordEncoder.encode("User@123"))
                    .role(Role.USER)
                    .status(1)
                    .build());
        }

        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                    Category.builder().name("Cà phê Arabica Cầu Đất").images("arabica.jpg").status(1).build(),
                    Category.builder().name("Cà phê Robusta Honey").images("robusta.jpg").status(1).build(),
                    Category.builder().name("Dụng cụ Pour Over V60").images("pour-over.jpg").status(1).build(),
                    Category.builder().name("Phin & Bình pha Espresso").images("phin-espresso.jpg").status(1).build()
            ));
        }
    }
}
