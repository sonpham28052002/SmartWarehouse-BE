package vn.edu.iuh.fit.smartwarehousebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SmartWarehouseBeApplicationTests {

    @Autowired
    private StockTakeRepository stockTakeRepository;

    @Test
    void contextLoads() {
        LocalDateTime from = LocalDateTime.of(2025, 4, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 1, 23, 59);
        List<TransactionType> types = List.of(TransactionType.EXPORT_TO_WAREHOUSE);
        System.out.println(stockTakeRepository.findTodaySequence(from, to));
    }

}
