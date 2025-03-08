package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}