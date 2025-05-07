package vn.edu.iuh.fit.smartwarehousebe.repositories;

import io.lettuce.core.Value;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;

@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long>,
    JpaSpecificationExecutor<StorageLocation> {

  Optional<StorageLocation> findByNameAndRowIndexAndColumnIndex(String name, Long rowIndex,
      Long columnIndex);
  Optional<StorageLocation> findByName(String name);
}