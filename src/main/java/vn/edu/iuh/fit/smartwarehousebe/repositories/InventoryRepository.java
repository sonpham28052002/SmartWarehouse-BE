package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

import java.util.Optional;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>,
    JpaSpecificationExecutor<Inventory> {

  List<Inventory> findByProduct_Id(Long productId);

  Optional<Inventory> findByProduct_IdAndStorageLocation_NameAndUnitIdAndStatus(Long productId,
      String storageLocationName, Long unitId, InventoryStatus status);

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
  List<Inventory> findInventoriesByTransactionDateAndTypesAndStatus(
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to,
      @Param("types") List<TransactionType> types,
      @Param("status") InventoryStatus status);


  @Query("SELECT DISTINCT p FROM Product p " +
      "JOIN Inventory i ON i.product.id = p.id " +
      "JOIN StorageLocation s ON i.storageLocation.id = s.id " +
      "WHERE s.warehouseShelf.warehouse.id = :warehouseId")
  Page<Product> findAllByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);



}