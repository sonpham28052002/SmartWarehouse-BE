package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

import java.util.Optional;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByProduct_Id(Long productId);

  Optional<Inventory> findByProduct_IdAndStorageLocation_Id(Long productId, Long storageLocationId);

  List<Inventory> findByStorageLocationWarehouseShelfIdIn(List<Long> warehouseShelfIds);

  List<Inventory> findByProductIdIn(List<Long> warehouseShelfIds);
}