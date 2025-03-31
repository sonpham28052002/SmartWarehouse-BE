package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.Ids.StockTakeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.CreateStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeDetailStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeStatus;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.StockTakeSpecification;

@Service
public class StockTakeService {

  @Autowired
  @Lazy
  private StockTakeRepository stockTakeRepository;

  @Autowired
  @Lazy
  private StockTakeDetailRepository stockTakeDetailRepository;

  @Autowired
  @Lazy
  private InventoryRepository inventoryRepository;


  @Autowired
  @Lazy
  private WarehouseRepository warehouseRepository;

  @Transactional(readOnly = true)
  public Page<StockTakeResponse> getAll(PageRequest pageRequest, GetStockTakeRequest request) {
    Specification<StockTake> specification = Specification.where(null);
    if (request.getWarehouseCode() != null) {
      specification = specification.and(StockTakeSpecification.hasCode(request.getWarehouseCode()));
    }

    if (request.getStatus() != null) {
      specification = specification.and(
          StockTakeSpecification.hasStatus(request.getStatus().getStatus()));
    }
    return stockTakeRepository.findAll(specification, pageRequest)
        .map(StockTakeMapper.INSTANCE::toDto);
  }

  @Transactional
  public StockTakeResponse getStockTakeById(Long id) {
    return StockTakeMapper.INSTANCE.toDto(stockTakeRepository.findById(id)
        .orElseThrow(null));
  }

  @Transactional
  public boolean deleteStockTakeById(Long id) {
    try {
      StockTake stockTake = stockTakeRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("stock take not fond"));
      for (StockTakeDetail detail : stockTake.getStockTakeDetails()) {
        stockTakeDetailRepository.delete(detail);
      }
      stockTakeRepository.deleteById(id);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;
  }


  @Transactional
  public StockTakeResponse createStockTake(CreateStockTakeRequest request) {
    StockTake stockTake = stockTakeRepository.save(
        StockTake.builder().description(request.getDescription()).warehouse(
                warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new NotFoundException("warehouse not fond")))
            .status(StockTakeStatus.PENDING).build());

    List<Inventory> inventories = new ArrayList<>();

    if (request.getProductIds() != null) {
      inventories = inventoryRepository.findByProductIdIn(
          request.getProductIds());
    } else {
      inventories = inventoryRepository.findByStorageLocationWarehouseShelfIdIn(
          request.getShelfIds());
    }

    List<StockTakeDetail> stockTakeDetails = new ArrayList<>();

    for (Inventory inventory : inventories) {
      if (inventory.getStatus() == InventoryStatus.ACTIVE) {
        stockTakeDetails.add(stockTakeDetailRepository.save(
            StockTakeDetail.builder()
                .id(StockTakeDetailId.builder().stockTakeId(stockTake.getId())
                    .inventoryId(inventory.getId()).build())
                .stockTake(stockTake).status(StockTakeDetailStatus.UNVERIFIED)
                .actualQuantity(0L).expectedQuantity(0L).damagedQuantity(0L).inventory(inventory)
                .description(null).build()));
      }
    }
    stockTake.setStockTakeDetails(stockTakeDetails);
    return StockTakeMapper.INSTANCE.toDto(stockTake);
  }

  @Transactional
  public StockTakeResponse startStockTake(Long stockTakeId) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stoke take not found"));
    stockTake.setStatus(StockTakeStatus.IN_PROGRESS);
    StockTake newStockTake = stockTakeRepository.save(stockTake);
    List<StockTakeDetail> stockTakeDetails = new ArrayList<>();
    for (StockTakeDetail detail : stockTake.getStockTakeDetails()) {
      if (detail.getStatus() == StockTakeDetailStatus.UNVERIFIED) {
        detail.setExpectedQuantity(detail.getInventory().getQuantity());
        stockTakeDetails.add(stockTakeDetailRepository.save(detail));
      } else {
        stockTakeDetails.add(detail);
      }
    }
    newStockTake.setStockTakeDetails(stockTakeDetails);
    return StockTakeMapper.INSTANCE.toDto(newStockTake);
  }

  @Transactional
  public StockTakeResponse saveStockTake(Long stockTakeId, StockTakeResponse response) {
    List<StockTakeDetailResponse> stockTakeDetailResponses = new ArrayList<>();
    for (StockTakeDetailResponse detail : response.getStockTakeDetails()) {
      StockTakeDetail stockTakeDetail = stockTakeDetailRepository.findById(
              StockTakeDetailId.builder()
                  .inventoryId(detail.getInventory().getId())
                  .stockTakeId(stockTakeId).build())
          .orElseThrow(() -> new NotFoundException("stoke take detail not found"));
      stockTakeDetail.setActualQuantity(detail.getActualQuantity());
      stockTakeDetail.setDamagedQuantity(detail.getDamagedQuantity());
      stockTakeDetail.setStatus(detail.getStatus());
      stockTakeDetail.setDescription(detail.getDescription());
      stockTakeDetailResponses.add(
          StockTakeDetailMapper.INSTANCE.toDto(stockTakeDetailRepository.save(stockTakeDetail)));
    }
    response.setStockTakeDetails(stockTakeDetailResponses);
    return response;
  }

  @Transactional
  public StockTakeResponse completeStockTake(Long stockTakeId) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stoke take not found"));
    stockTake.setStatus(StockTakeStatus.COMPLETED);
    stockTakeRepository.save(stockTake);
    return StockTakeMapper.INSTANCE.toDto(stockTake);
  }

}
