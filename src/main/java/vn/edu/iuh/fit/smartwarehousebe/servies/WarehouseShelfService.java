package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.WarehouseShelf.WarehouseShelfRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.WarehouseShelf.WarehouseShelfResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseShelfMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseShelfRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.WarehouseShelfSpecification;

@Service
public class WarehouseShelfService {

  @Autowired
  private WarehouseShelfRepository warehouseShelfRepository;

  @Autowired
  private WarehouseRepository warehouseRepository;

  public List<WarehouseShelfResponse> getAllShelfIdWareHouseID(Long wareHouseID) {
    Specification<WarehouseShelf> spec = Specification.where(null);
    spec = spec.and(WarehouseShelfSpecification.hasWareHouse(wareHouseID));
    return warehouseShelfRepository.findAll(spec).stream()
        .map((i) -> WarehouseShelfMapper.INSTANCE.toDto(i)).collect(
            Collectors.toList());
  }

  public WarehouseShelfResponse getAllShelfByID(Long shelfId) {
    return WarehouseShelfMapper.INSTANCE.toDto(warehouseShelfRepository.findById(shelfId)
        .orElseThrow(() -> new NotFoundException("shelf not fond")));
  }

  @Transactional
  public List<WarehouseShelfResponse> createAndUpdate(Long wareHouseID,
      List<WarehouseShelfRequest> requests) {
    Warehouse warehouse = warehouseRepository.findById(wareHouseID)
        .orElseThrow(() -> new NotFoundException("Warehouse not fond"));
    List<WarehouseShelf> oldWarehouseShelf = warehouse.getWarehouseShelves();
    List<WarehouseShelf> requestWarehouseShelf = new ArrayList<>();
    for (WarehouseShelfRequest shelfRequest : requests) {
      requestWarehouseShelf.add(WarehouseShelf.builder()
          .shelfName(shelfRequest.getShelfName())
          .id(shelfRequest.getId())
          .maxCapacity(shelfRequest.getMaxCapacity())
          .description(shelfRequest.getDescription())
          .rowNum(shelfRequest.getRowNum())
          .columnNum(shelfRequest.getColumnNum())
          .warehouse(warehouse)
          .build());
    }
    List<WarehouseShelf> allWarehouseShelves = new ArrayList<>(oldWarehouseShelf);
    allWarehouseShelves.addAll(requestWarehouseShelf);
    List<WarehouseShelf> response = new ArrayList<>();

    for (WarehouseShelf warehouseShelf : allWarehouseShelves) {
      if (warehouseShelf.getId() == null) {
        response.add(warehouseShelfRepository.save(warehouseShelf));
      } else if (oldWarehouseShelf.contains(warehouseShelf) && !requestWarehouseShelf.contains(
          warehouseShelf)) {
        System.out.println(warehouseShelf.getId());
        warehouseShelfRepository.deleteById(warehouseShelf.getId());
      } else if (!oldWarehouseShelf.contains(warehouseShelf) && requestWarehouseShelf.contains(
          warehouseShelf)) {
        response.add(warehouseShelfRepository.save(warehouseShelf));
      } else {
        response.add(warehouseShelfRepository.save(warehouseShelf));
      }
    }

    return response.stream().map((i) -> WarehouseShelfMapper.INSTANCE.toDto(i))
        .collect(Collectors.toList());
  }

}
