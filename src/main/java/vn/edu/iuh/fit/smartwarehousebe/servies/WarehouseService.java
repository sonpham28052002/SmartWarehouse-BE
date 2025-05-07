package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.GetWarehouseQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.WarehouseSpecification;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @description
 * @author: vie
 * @date: 1/3/25
 */
@Service
public class WarehouseService extends CommonService<Warehouse> {

  @Autowired
  private WarehouseRepository warehouseRepository;

  @Autowired
  private UserService userService;
  @Autowired
  private WarehouseMapper warehouseMapper;
  @Autowired
  private StorageService storageService;

  @Transactional
  @Cacheable(value = "warehouse", key = "#request + '_' + #page + '_' + #size + '_' + #sortBy", unless = "#result == null")
  public Page<WarehouseResponse> getAll(int page, int size, String sortBy, GetWarehouseQuest request) {
    Specification<Warehouse> spec = Specification.where(null);
    if (request.getName() != null) {
      spec = spec.and(WarehouseSpecification.nameLike(request.getName()));
    }
    if (request.getLocation() != null) {
      spec = spec.and(WarehouseSpecification.addressLike(request.getLocation()));
    }
    if (request.getManagerId() != null) {
      spec = spec.and(WarehouseSpecification.hasManager(request.getManagerId()));
    }
    if (request.getCode() != null) {
      spec = spec.and(WarehouseSpecification.hasCode(request.getCode()));
    }
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);
    return warehouseRepository.findWareHouseAll(request.isDeleted(), spec, pageable).map(warehouseMapper::toDto);
  }

  @Transactional
  @Cacheable(value = "warehouse", key = "#request", unless = "#result == null")
  public List<WarehouseResponse> getAll(GetWarehouseQuest request) {
    Specification<Warehouse> spec = Specification.where(null);
    if (request.getName() != null) {
      spec = spec.and(WarehouseSpecification.nameLike(request.getName()));
    }
    if (request.getLocation() != null) {
      spec = spec.and(WarehouseSpecification.addressLike(request.getLocation()));
    }
    if (request.getManagerId() != null) {
      spec = spec.and(WarehouseSpecification.hasManager(request.getManagerId()));
    }
    if (request.getCode() != null) {
      spec = spec.and(WarehouseSpecification.hasCode(request.getCode()));
    }

    spec = spec.and(WarehouseSpecification.hasDeleted(false));
    return warehouseRepository.findAll(spec).stream().map(warehouseMapper::toDto).toList();
  }

  @Transactional
  @Cacheable(value = "warehouse", key = "#id", unless = "#result == null")
  public WarehouseResponse getById(Long id) {
    return warehouseRepository.findById(id).map(warehouseMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("Warehouse not found"));
  }

  @CacheEvict(value = {"warehouse", "user"}, allEntries = true)
  @Transactional
  public WarehouseResponse create(Warehouse createWarehouse) {
    Long managerId = createWarehouse.getManager().getId();
    Set<User> staffs = createWarehouse.getStaffs();
    Warehouse temp = createWarehouse;
    temp.setManager(null);
    Warehouse newWarehouse = warehouseRepository.save(temp);

    User manager = userService.getUserById(managerId);
    manager.setWarehouseManager(newWarehouse);
    newWarehouse.setManager(userService.updateUser(manager));

    for (User user : staffs) {
      User staff = userService.getUserById(user.getId());
      staff.setWarehouse(newWarehouse);
      userService.updateUser(staff);
    }

    return warehouseMapper.toDto(warehouseRepository.save(newWarehouse));
  }

  @CacheEvict(value = "warehouse", allEntries = true)
  @Transactional
  public WarehouseResponse update(Long id, Warehouse updateWarehouse) {
    Warehouse oldWarehouse = warehouseRepository.findById(id).orElseThrow(null);

    oldWarehouse.setAddress(updateWarehouse.getAddress());
    oldWarehouse.setName(updateWarehouse.getName());
    oldWarehouse.setCode(updateWarehouse.getCode());

    if (oldWarehouse == null) {
      throw new NotFoundException("no found warehouse");
    }

    Long oldManagerId = oldWarehouse.getManager() != null ? oldWarehouse.getManager().getId() : null;
    Long newManagerId = updateWarehouse.getManager() != null ? updateWarehouse.getManager().getId() : null;

    if (!Objects.equals(oldManagerId, newManagerId)) {
      if (oldManagerId != null) {
        User oldManager = userService.getUserById(oldManagerId);
        oldManager.setWarehouseManager(null);
        userService.updateUser(oldManager);
      }

      if (newManagerId != null) {
        User newManager = userService.getUserById(newManagerId);
        newManager.setWarehouseManager(oldWarehouse);
        userService.updateUser(newManager);
        oldWarehouse.setManager(newManager);
      } else {
        oldWarehouse.setManager(null);
      }
    }

    // update staffs
    Set<User> oldStaffs = oldWarehouse.getStaffs();
    Set<User> newStaffs = updateWarehouse.getStaffs();
    Set<User> allStaff = new HashSet<>(oldStaffs);
    allStaff.addAll(newStaffs);
    for (User user : allStaff) {
      if (oldStaffs.contains(user) && !newStaffs.contains(user)) {
        User staff = userService.getUserById(user.getId());
        staff.setWarehouse(null);
        userService.updateUser(staff);
      } else if (!oldStaffs.contains(user) && newStaffs.contains(user)) {
        User staff = userService.getUserById(user.getId());
        staff.setWarehouse(oldWarehouse);
        userService.updateUser(staff);
      }
    }

    return warehouseMapper.toDto(warehouseRepository.save(oldWarehouse));
  }

  @CacheEvict(value = {"warehouse", "users"}, allEntries = true)
  @Transactional
  public boolean delete(Long id) {
    try {
      Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(null);
      if (warehouse == null) {
        return false;
      }

      User manager = userService.getUserById(warehouse.getManager().getId());
      manager.setWarehouseManager(null);
      userService.updateUser(manager);

      for (User user : warehouse.getStaffs()) {
        User staff = userService.getUserById(user.getId());
        staff.setWarehouse(null);
        userService.updateUser(staff);
      }
      warehouseRepository.deleteById(id);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;
  }

  public WarehouseResponse getByCode(String warehouseCode) {
    return warehouseRepository.findByCode(warehouseCode)
        .map(warehouseMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("Warehouse not found"));
  }

  @CacheEvict(value = {"warehouse", "users"}, allEntries = true)
  @Transactional
  public boolean restore(Long id) {
    try {
      Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(null);
      if (warehouse == null) {
        return false;
      }
      warehouse.setDeleted(false);

      warehouseRepository.save(warehouse);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;

  }


  public WarehouseResponse getWarehouseByStaffId(Long id) {
      User user = userService.getUserById(id);
      if (user.getWarehouse() == null) {
         throw new NoSuchElementException("Warehouse not found");
      }
      return warehouseMapper.toDto(user.getWarehouse());
  }
}
