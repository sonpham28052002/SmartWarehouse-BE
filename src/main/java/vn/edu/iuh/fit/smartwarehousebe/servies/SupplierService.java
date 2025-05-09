package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.GetSupplierQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.SupplierNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.SupplierMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.SupplierRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SupplierSpecification;

import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class SupplierService extends CommonService<Supplier>{
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository,
                           SupplierMapper supplierMapper) {
        super();
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }
    @Cacheable(value = "suppliers", key = "#supplierQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize + '_' +#supplierQuest")
    public Page<SupplierResponse> getAll(PageRequest pageRequest, GetSupplierQuest supplierQuest) {
        Specification<Supplier> specification = SpecificationBuilder.<Supplier>builder()
                .with(SupplierSpecification.hasActive(supplierQuest.getActive()))
                .with(SupplierSpecification.hasCode(supplierQuest.getCode()))
                .with(SupplierSpecification.hasName(supplierQuest.getName()))
                .with(SupplierSpecification.hasLocation(supplierQuest.getLocation()))
                .with(SupplierSpecification.hasPhone(supplierQuest.getPhone()))
                .with(SupplierSpecification.hasEmail(supplierQuest.getEmail()))
                .build();

        return supplierRepository.findAll(specification, pageRequest).map(supplierMapper::toDto);
    }

    @Cacheable(value = "suppliers", key = "#supplierQuest")
    public List<SupplierResponse> getAll(GetSupplierQuest supplierQuest) {
        Specification<Supplier> specification = SpecificationBuilder.<Supplier>builder()
                .with(SupplierSpecification.hasCode(supplierQuest.getCode()))
                .with(SupplierSpecification.hasName(supplierQuest.getName()))
                .with(SupplierSpecification.hasLocation(supplierQuest.getLocation()))
                .with(SupplierSpecification.hasPhone(supplierQuest.getPhone()))
                .with(SupplierSpecification.hasEmail(supplierQuest.getEmail()))
                .with(SupplierSpecification.hasEmail(supplierQuest.getEmail()))
                .build();

        return supplierRepository.findAll(specification).stream()
                .map(supplierMapper::toDto)
                .toList();
    }
    @Cacheable(value = "suppliers", key = "#id")
    public SupplierResponse getById(Long id) {
        return supplierRepository.findById(id).map(supplierMapper::toDto)
                .orElseThrow(SupplierNotFoundException::new);
    }

    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierResponse create(CreateSupplierRequest supplierRequest) {
        Supplier supplier = supplierMapper.toEntity(supplierRequest);
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierResponse update(Long id, CreateSupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(SupplierNotFoundException::new);
        supplierMapper.partialUpdate(supplierRequest, supplier);
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @CacheEvict(value = "suppliers", allEntries = true)
    public boolean delete(Long id) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplierRepository.delete(supplier);
                    return true;
                })
                .orElseThrow(SupplierNotFoundException::new);
    }
}
