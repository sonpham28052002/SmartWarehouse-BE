package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location.CreateStorageLocationRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location.GetStorageLocationRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location.StorageLocationResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StorageLocationMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StorageLocationRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UnitRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseShelfRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.StorageLocationSpecification;

@Service
public class StorageLocationService {

  @Autowired
  private StorageLocationRepository storageLocationRepository;

  @Autowired
  private InventoryRepository inventoryRepository;

  @Autowired
  private UnitRepository unitRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private WarehouseShelfRepository warehouseShelfRepository;

  @Transactional
  public StorageLocationResponse create(Long shelfId, CreateStorageLocationRequest request) {
    WarehouseShelf shelf = warehouseShelfRepository.findById(shelfId)
        .orElseThrow(() -> new NotFoundException("Shelf not found"));

    StorageLocation storageLocation;
    if (request.getId() == null) {
      storageLocation = StorageLocation.builder()
          .columnIndex(request.getColumnIndex())
          .rowIndex(request.getRowIndex())
          .name(request.getName())
          .warehouseShelf(shelf)
          .maxCapacity(request.getMaxCapacity())
          .build();
    } else {
      storageLocation = storageLocationRepository.findById(request.getId())
          .orElseThrow(() -> new NotFoundException("Storage Location not found"));
      storageLocation.setMaxCapacity(request.getMaxCapacity());
    }

    storageLocation = storageLocationRepository.save(storageLocation);
    Inventory inventory = null;
    if (request.getInventoryResponses().getId() == null) {
      inventory = inventoryRepository.save(Inventory.builder()
          .id(request.getInventoryResponses().getId())
          .quantity(request.getInventoryResponses().getQuantity())
          .unit(unitRepository.findById(request.getInventoryResponses().getUnit().getId())
              .orElseThrow(() -> new NotFoundException("unit not fond")))
          .product(productRepository.findById(request.getInventoryResponses().getProduct().getId())
              .orElseThrow(() -> new NotFoundException("product not fond")))
          .storageLocation(storageLocation)
          .build());
    } else {
      inventory = inventoryRepository.findById(request.getInventoryResponses().getId())
          .orElseThrow(() -> new NotFoundException("inventory not fond"));
      inventory.setQuantity(request.getInventoryResponses().getQuantity());
      inventory.setUnit(unitRepository.findById(request.getInventoryResponses().getUnit().getId())
          .orElseThrow(() -> new NotFoundException("unit not fond")));
      inventory.setProduct(
          productRepository.findById(request.getInventoryResponses().getProduct().getId())
              .orElseThrow(() -> new NotFoundException("product not fond")));
      inventory.setStorageLocation(storageLocation);
      inventoryRepository.save(inventory);
    }

    List<Inventory> inventories = Optional.ofNullable(storageLocation.getInventories())
        .orElse(new ArrayList<>());
    inventories.add(inventory);
    storageLocation.setInventories(inventories);

    storageLocation = storageLocationRepository.save(storageLocation);

    return StorageLocationMapper.INSTANCE.toDto(storageLocation);
  }


  public Page<StorageLocationResponse> getAll(PageRequest pageRequest,
      GetStorageLocationRequest request, Long shelfId) {
    Specification<StorageLocation> spec = Specification.where(null);
    if (request.getName() != null) {
      spec = spec.and(StorageLocationSpecification.hasName(request.getName()));
    }

    spec = spec.and(StorageLocationSpecification.hasShelf(shelfId));

    return storageLocationRepository.findAll(spec, pageRequest)
        .map(i -> StorageLocationMapper.INSTANCE.toDto(i));
  }

  public List<StorageLocationResponse> getAll(
      GetStorageLocationRequest request, Long shelfId) {
    Specification<StorageLocation> spec = Specification.where(null);
    if (request.getName() != null) {
      spec = spec.and(StorageLocationSpecification.hasName(request.getName()));
    }

    spec = spec.and(StorageLocationSpecification.hasShelf(shelfId));

    return storageLocationRepository.findAll(spec)
        .stream()
        .map(i -> StorageLocationMapper.INSTANCE.toDto(i))
        .collect(Collectors.toList());
  }
}


