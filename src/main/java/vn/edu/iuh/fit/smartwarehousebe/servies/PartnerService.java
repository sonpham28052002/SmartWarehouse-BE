package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.CreatePartnerRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.PartnerNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.PartnerMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.repositories.PartnerRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.PartnerSpecification;

import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class PartnerService extends CommonService<Partner> {

  private final PartnerRepository partnerRepository;
  private final PartnerMapper partnerMapper;

  public PartnerService(PartnerRepository partnerRepository,
      PartnerMapper partnerMapper) {
    super();
    this.partnerRepository = partnerRepository;
    this.partnerMapper = partnerMapper;
  }

  @Cacheable(value = "partners", key = "#partnerQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize + '_' +#partnerQuest")
  public Page<PartnerResponse> getAll(PageRequest pageRequest, GetPartnerQuest partnerQuest) {
    Specification<Partner> specification = SpecificationBuilder.<Partner>builder()
        .with(PartnerSpecification.hasActive(partnerQuest.getActive()))
        .with(PartnerSpecification.hasCode(partnerQuest.getCode()))
        .with(PartnerSpecification.hasName(partnerQuest.getName()))
        .with(PartnerSpecification.hasLocation(partnerQuest.getLocation()))
        .with(PartnerSpecification.hasPhone(partnerQuest.getPhone()))
        .with(PartnerSpecification.hasEmail(partnerQuest.getEmail()))
        .with(PartnerSpecification.hasType(partnerQuest.getType()))
        .build();

    return partnerRepository.findAll(specification, pageRequest).map(partnerMapper::toDto);
  }

  @Cacheable(value = "partners", key = "#partnerQuest")
  public List<PartnerResponse> getAll(GetPartnerQuest partnerQuest) {
    Specification<Partner> specification = SpecificationBuilder.<Partner>builder()
        .with(PartnerSpecification.hasCode(partnerQuest.getCode()))
        .with(PartnerSpecification.hasName(partnerQuest.getName()))
        .with(PartnerSpecification.hasLocation(partnerQuest.getLocation()))
        .with(PartnerSpecification.hasPhone(partnerQuest.getPhone()))
        .with(PartnerSpecification.hasEmail(partnerQuest.getEmail()))
        .with(PartnerSpecification.hasEmail(partnerQuest.getEmail()))
        .with(PartnerSpecification.hasType(partnerQuest.getType()))
        .build();
    return partnerRepository.findAll(specification).stream()
        .map(partnerMapper::toDto)
        .toList();
  }

  @Cacheable(value = "partners", key = "#id")
  public PartnerResponse getById(Long id) {
    return partnerRepository.findById(id).map(partnerMapper::toDto)
        .orElseThrow(PartnerNotFoundException::new);
  }

  @CacheEvict(value = "partners", allEntries = true)
  public PartnerResponse create(CreatePartnerRequest partnerRequest) {
    Partner partner = partnerMapper.toEntity(partnerRequest);
    partner.setType(partnerRequest.getType());
    return partnerMapper.toDto(partnerRepository.save(partner));
  }

  @CacheEvict(value = "partners", allEntries = true)
  public PartnerResponse update(Long id, CreatePartnerRequest partnerRequest) {
    Partner partner = partnerRepository.findById(id).orElseThrow(PartnerNotFoundException::new);
    partnerMapper.partialUpdate(partnerRequest, partner);
    partner.setType(partnerRequest.getType());
    return partnerMapper.toDto(partnerRepository.save(partner));
  }

  @CacheEvict(value = "partners", allEntries = true)
  public boolean delete(Long id) {
    return partnerRepository.findById(id)
        .map(partner -> {
          partnerRepository.delete(partner);
          return true;
        })
        .orElseThrow(PartnerNotFoundException::new);
  }

  public PartnerResponse getByCode(String partnerCode) {
    return partnerRepository.findByCode(partnerCode)
        .map(partnerMapper::toDto)
        .orElseThrow(PartnerNotFoundException::new);
  }
}
