package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.GetUnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.UnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UnitRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.UnitSpecification;

import java.util.List;

@Service
public class UnitService extends CommonService<Unit>{

    @Autowired
    private UnitRepository unitRepository;
    public Page<UnitResponse> getUnits(PageRequest pageRequest, GetUnitRequest unitRequest) {
        Specification<Unit> specification = Specification.where(null);
        if (unitRequest.getName() != null) {
            specification = specification.and(UnitSpecification.hasName(unitRequest.getName()));
        }
        if (unitRequest.getCode() != null) {
            specification = specification.and(UnitSpecification.hasCode(unitRequest.getCode()));
        }
        return unitRepository.findAll(specification, pageRequest).map(UnitMapper.INSTANCE::toDto);
    }

    public List<UnitResponse> getAllUnit(GetUnitRequest unitRequest) {
        Specification<Unit> specification = Specification.where(null);
        if (unitRequest.getName() != null) {
            specification = specification.and(UnitSpecification.hasName(unitRequest.getName()));
        }
        if (unitRequest.getCode() != null) {
            specification = specification.and(UnitSpecification.hasCode(unitRequest.getCode()));
        }
        specification = specification.and(UnitSpecification.hasActive(true));

        return UnitMapper.INSTANCE.toDtoList(unitRepository.findAll(specification));
    }

    public UnitResponse createAndUpdate(UnitRequest unitRequest) {
        if (unitRequest.getId() != null) {
            Unit unit = unitRepository.findById(unitRequest.getId()).orElseThrow(null);
            if (unit == null) throw new NotFoundException("unit not fond");
            unit.setCode(unitRequest.getCode());
            unit.setName(unitRequest.getName());
            return UnitMapper.INSTANCE.toDto(unitRepository.save(unit));
        }
        return UnitMapper.INSTANCE.toDto(unitRepository.save(UnitMapper.INSTANCE.toEntity(unitRequest)));
    }
    public boolean deletedUnit(Long id) {
        try {
            unitRepository.deleteById(id);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public UnitResponse getUnitById(Long id) {
        return UnitMapper.INSTANCE.toDto(unitRepository.findById(id).orElseThrow(null));
    }
}
