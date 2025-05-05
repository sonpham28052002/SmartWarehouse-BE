package vn.edu.iuh.fit.smartwarehousebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.damagedProduct.GetDamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.repositories.DamagedProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.iuh.fit.smartwarehousebe.servies.DamagedProductService;

@SpringBootTest
class SmartWarehouseBeApplicationTests {

    @Autowired
    private DamagedProductRepository damagedProductRepository;
    @Autowired
    private DamagedProductService damagedProductService;

    @Test
    void contextLoads() {
        PageRequest pageRequest = PageRequest.of(10, 10 , Sort.by("id"));
//        System.out.println(damagedProductService.getAll(pageRequest, new GetDamagedProduct()));
    }

}
