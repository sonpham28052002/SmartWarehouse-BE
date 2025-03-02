package vn.edu.iuh.fit.smartwarehousebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.SupplierRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
class SmartWarehouseBeApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SupplierRepository supplierRepository;
    @Test
    void contextLoads() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            userRepository.save(User.builder()
                            .role(Role.MANAGER)
                            .code(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
                            .userName(faker.twitter().userName())
                            .email(faker.theItCrowd().emails())
                            .phoneNumber(faker.phoneNumber().phoneNumber())
                            .address(faker.address().fullAddress())
                            .dateOfBirth(LocalDateTime.now())
                            .profilePicture(faker.internet().image())
                            .fullName(faker.name().fullName())
                            .password(passwordEncoder.encode(faker.numerify("10")))
                    .build());
        }
    }

    @Test
    void supplier() {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            supplierRepository.save(Supplier.builder()
                            .phone(faker.phoneNumber().phoneNumber())
                            .address(faker.address().fullAddress())
                            .name(faker.company().name())
                            .email(faker.internet().emailAddress())
                    .build());
        }
    }
}
