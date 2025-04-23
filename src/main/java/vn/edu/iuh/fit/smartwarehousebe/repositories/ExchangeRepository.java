package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>,
    JpaSpecificationExecutor<Exchange> {

}