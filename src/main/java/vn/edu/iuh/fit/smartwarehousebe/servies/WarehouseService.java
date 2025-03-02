package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
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
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.GetWarehouseQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.UpdateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.WarehouseSpecification;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @description
 * @author: vie
 * @date: 1/3/25
 */
@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserService userService;

    @Cacheable(value = "warehouse", key = "#request + '_' + #page + '_' + #size + '_' + #sortBy", unless = "#result == null")
    public Page<Warehouse> getAll(int page, int size, String sortBy, GetWarehouseQuest request) {
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
        System.out.println(request.isDeleted());
        return warehouseRepository.findWareHouseAll(request.isDeleted(), spec, pageable);
    }

    @Cacheable(value = "warehouse", key = "#id", unless = "#result == null")
    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Warehouse not found"));
    }

    @CacheEvict(value = "warehouse", allEntries = true)
    @Transactional
    public Warehouse create(Warehouse createWarehouse) {
        Long managerId = createWarehouse.getManager().getId();
        Set<User> staffs = createWarehouse.getStaffs();
        System.out.println(managerId);
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

        return warehouseRepository.save(newWarehouse);
    }

    @CacheEvict(value = "warehouse", allEntries = true)
    @Transactional
    public Warehouse update(Long id, Warehouse updateWarehouse) {
        Warehouse oldWarehouse = warehouseRepository.findById(id).orElseThrow(null);

        oldWarehouse.setAddress(updateWarehouse.getAddress());
        oldWarehouse.setName(updateWarehouse.getName());
        oldWarehouse.setCode(updateWarehouse.getCode());

        if (oldWarehouse == null) throw new NotFoundException("no found warehouse");

        if (oldWarehouse.getManager().getId() != updateWarehouse.getManager().getId()){

            // update warehouse manager for the old manager
            User oldManager = userService.getUserById(oldWarehouse.getManager().getId());
            oldManager.setWarehouseManager(null);
            userService.updateUser(oldManager);

            // update warehouse manager for the old manager
            User newManager = userService.getUserById(updateWarehouse.getManager().getId());
            newManager.setWarehouseManager(oldWarehouse);
            oldWarehouse.setManager(userService.updateUser(newManager));
        }


        // update staffs
        Set<User> oldStaffs = oldWarehouse.getStaffs();
        Set<User> newStaffs = updateWarehouse.getStaffs();
        Set<User> allStaff = new HashSet<>(oldStaffs);
        allStaff.addAll(newStaffs);
        for (User user : allStaff) {
            if (oldStaffs.contains(user) && !newStaffs.contains(user))
            {
                User staff = userService.getUserById(user.getId());
                staff.setWarehouse(null);
                userService.updateUser(staff);
            } else if (!oldStaffs.contains(user) && newStaffs.contains(user))
            {
                User staff = userService.getUserById(user.getId());
                staff.setWarehouse(oldWarehouse);
                userService.updateUser(staff);
            }
        }

        return warehouseRepository.save(oldWarehouse);
    }

    @CacheEvict(value = "warehouse", allEntries = true)
    @Transactional
    public boolean delete(Long id) {
        try {
            Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(null);
            if (warehouse == null) return false;

            User manager = userService.getUserById(warehouse.getManager().getId());
            manager.setWarehouseManager(null);
            userService.updateUser(manager);

            for (User user: warehouse.getStaffs()) {
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
}
