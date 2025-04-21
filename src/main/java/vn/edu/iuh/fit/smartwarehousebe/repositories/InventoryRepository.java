package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

import java.util.Optional;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  List<Inventory> findByProduct_Id(Long productId);

  Optional<Inventory> findByProduct_IdAndStorageLocation_IdAndUnitId(Long productId,
      Long storageLocationId, Long unitId);

  Optional<Inventory> findByProduct_IdAndStorageLocation_Id(Long productId,
      Long storageLocationId);

  List<Inventory> findByStorageLocationWarehouseShelfIdIn(List<Long> warehouseShelfIds);

  List<Inventory> findByProductIdIn(List<Long> warehouseShelfIds);

  Inventory findByProductIdAndUnitId(Long productId, Long unitId);

  @Query("SELECT DISTINCT i FROM Inventory i " +
          "JOIN TransactionDetail td ON i.id = td.inventory.id " +
          "JOIN Transaction t ON td.transaction.id = t.id " +
          "WHERE t.lastModifiedDate BETWEEN :from AND :to " +
          "AND t.transactionType IN :types " +
          "AND i.status IN :status")
  List<Inventory> findInventoriesByTransactionDateAndTypesAndStatus(@Param("from") LocalDateTime from,
                                                                      @Param("to") LocalDateTime to,
                                                                      @Param("types") List<TransactionType> types,
                                                                      @Param("status") InventoryStatus status);



}