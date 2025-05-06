package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.inventory.GetInventoryRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.InventoryMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.InventorySpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

@Service
public class InventoryService {

  @Autowired
  private InventoryRepository inventoryRepository;

  public Page<InventoryResponse> getAll(PageRequest pageRequest, GetInventoryRequest request) {
    System.out.println(request.getProductCode());
    Specification<Inventory> specification = SpecificationBuilder.<Inventory>builder()
        .with(InventorySpecification.hasWarehouseName(request.getWarehouseName()))
        .with(InventorySpecification.hasWarehouseCode(request.getWarehouseCode()))
        .with(InventorySpecification.hasProductCode(request.getProductCode()))
        .with(InventorySpecification.hasProductName(request.getProductName()))
        .build();
    return inventoryRepository.findAll(specification, pageRequest)
        .map((i) -> InventoryMapper.INSTANCE.toDto(i));
  }

  }
