package vn.edu.iuh.fit.smartwarehousebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.iuh.fit.smartwarehousebe.enums.Rule;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import net.datafaker.Faker;

@SpringBootTest
class SmartWarehouseBeApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            userRepository.save(User.builder()
                            .rule(Rule.ADMIN)
                            .name(faker.twitter().userName())
                            .lastName(faker.name().lastName())
                            .firstName(faker.name().firstName())
                            .password(passwordEncoder.encode(faker.numerify("10")))
                    .build());
        }
    }

}
