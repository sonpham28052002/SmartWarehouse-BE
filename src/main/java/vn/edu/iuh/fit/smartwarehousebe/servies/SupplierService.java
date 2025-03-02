package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.GetSupplierQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.SupplierMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.repositories.SupplierRepository;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class SupplierService extends SoftDeleteService<Supplier> {
   private final SupplierRepository supplierRepository;
   private final SupplierMapper supplierMapper;

   public SupplierService(SupplierRepository supplierRepository,
                          SupplierMapper supplierMapper) {
      super();
      this.supplierRepository = supplierRepository;
      this.supplierMapper = supplierMapper;
   }

   public Page<SupplierResponse> getAll(PageRequest pageRequest, GetSupplierQuest supplierQuest) {
      Specification<Supplier> specification = Specification.where(null);
      // TODO: add filter
      return supplierRepository.findAll(pageRequest).map(supplierMapper::toDto);
   }

   public SupplierResponse getById(Long id) {
      return supplierRepository.findById(id).map(supplierMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("Supplier not found"));
   }

   public SupplierResponse create(CreateSupplierRequest supplierRequest) {
      Supplier supplier = supplierMapper.toEntity(supplierRequest);
      return supplierMapper.toDto(supplierRepository.save(supplier));
   }
}
