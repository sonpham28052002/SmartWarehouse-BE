package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
}