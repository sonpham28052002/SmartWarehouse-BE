package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;

@Repository
public interface WarehouseShelfRepository extends JpaRepository<WarehouseShelf, Long>,
    JpaSpecificationExecutor<WarehouseShelf> {

  WarehouseShelf findByShelfName(String name);

}
