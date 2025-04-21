package vn.edu.iuh.fit.smartwarehousebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SmartWarehouseBeApplicationTests {

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @Test
    void contextLoads() {
        LocalDateTime from = LocalDateTime.of(2020, 4, 4, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 4, 23, 59);
        List<TransactionType> types = List.of(TransactionType.EXPORT_TO_WAREHOUSE);
        System.out.println(transactionDetailRepository.findByInventoryIdAndTransactionTypeInAndLastModifiedDateBetween(2L, types ,from, to).size());
    }

}
