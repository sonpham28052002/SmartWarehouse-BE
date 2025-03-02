package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.GetWarehouseQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.UpdateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.WarehouseSpecification;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 1/3/25
 */
@Service
public class WarehouseService extends SoftDeleteService<Warehouse> {
   private final WarehouseMapper warehouseMapper;
   private final WarehouseRepository warehouseRepository;

   public WarehouseService(WarehouseMapper warehouseMapper, WarehouseRepository warehouseRepository) {
      super();
      this.warehouseMapper = warehouseMapper;
      this.warehouseRepository = warehouseRepository;
   }

   @Cacheable(value = "warehouse", unless = "#result == null")
   public List<WarehouseResponse> getAll(int page, int size, String sortBy, GetWarehouseQuest request) {
      Specification<Warehouse> spec = Specification.where(null);
      if (request.name() != null) {
         spec = spec.and(WarehouseSpecification.nameLike(request.name()));
      }
      if (request.location() != null) {
         spec = spec.and(WarehouseSpecification.addressLike(request.location()));
      }
      if (request.managerId() != null) {
         spec = spec.and(WarehouseSpecification.hasManager(request.managerId()));
      }
      if (request.code() != null) {
         spec = spec.and(WarehouseSpecification.hasCode(request.code()));
      }

      Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
      Pageable pageable = PageRequest.of(page, size, sort);

      return warehouseRepository.findAllByDeleted(request.deleted(), spec, pageable).map(warehouseMapper::toDto)
            .getContent();
   }

   @Cacheable(value = "warehouse", key = "#id", unless = "#result == null")
   public WarehouseResponse getById(Integer id) {
      return warehouseRepository.findById(id).map(warehouseMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("Warehouse not found"));
   }

   @Cacheable(value = "warehouse", unless = "#result == null")
   public WarehouseResponse create(CreateWarehouseRequest createWarehouse) {
      Warehouse warehouse = warehouseMapper.toEntity(createWarehouse);
      return warehouseMapper.toDto(warehouseRepository.save(warehouse));
   }

   @Cacheable(value = "warehouse", key = "#id", unless = "#result == null")
   public WarehouseResponse update(Integer id, UpdateWarehouseRequest updateWarehouse) {
      Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Warehouse not found"));
      warehouseMapper.partialUpdate(updateWarehouse, warehouse);
      return warehouseMapper.toDto(warehouseRepository.save(warehouse));
   }

   @CacheEvict(value = "warehouse", allEntries = true)
   public void delete(Integer id) {
      warehouseRepository.deleteById(id);
   }
}
