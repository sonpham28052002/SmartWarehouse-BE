package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;

public interface WarehouseRepository extends CrudRepository<Warehouse, Integer>, PagingAndSortingRepository<Warehouse, Integer>, JpaSpecificationExecutor<Warehouse> {
   Page<Warehouse> findAllByDeleted(boolean deleted, Specification<Warehouse> spec, Pageable pageable);
}