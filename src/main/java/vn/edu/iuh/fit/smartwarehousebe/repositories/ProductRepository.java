package vn.edu.iuh.fit.smartwarehousebe.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
    JpaSpecificationExecutor<Product> {

  @Query("SELECT DISTINCT p FROM Product p " +
      "JOIN Inventory i ON i.product.id = p.id " +
      "JOIN StorageLocation s ON i.storageLocation.id = s.id " +
      "WHERE s.warehouseShelf.warehouse.id = :warehouseId " +
      "AND i.status = :status")
  List<Product> findAllByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("status") InventoryStatus status);

  @Query("SELECT DISTINCT p FROM Product p " +
      "JOIN Inventory i ON i.product.id = p.id " +
      "JOIN StorageLocation s ON i.storageLocation.id = s.id " +
      "WHERE s.warehouseShelf.warehouse.id = :warehouseId and p.partner.id = :partnerId " +
      "AND i.status = :status")
  List<Product> findAllByWarehouseIdAAndPartnerId(@Param("warehouseId") Long warehouseId,
      @Param("partnerId") Long partnerId, @Param("status") InventoryStatus status);

  List<Product> findByIdInAndDeletedFalse(Collection<Long> ids);

  List<Product> findByIdIn(Collection<Long> ids);

  Optional<Product> findByCode(String productCode);

}