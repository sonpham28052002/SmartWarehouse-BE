package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTakeDetail.GetStockTakeDetailRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.StockTakeDetailSpecification;

@Service
public class StockTakeDetailService {

  @Autowired
  private StockTakeDetailRepository stockTakeDetailRepository;

  @Transactional(readOnly = true)
  public Page<StockTakeDetailResponse> getAll(Long stockTakeId, PageRequest pageRequest,
      GetStockTakeDetailRequest request) {
    Specification<StockTakeDetail> specification = Specification.where(null);
    if (request.getProductCode() != null) {
      specification = specification.and(
          StockTakeDetailSpecification.hasCode(request.getProductCode()));
    }

    specification = specification.and(
        StockTakeDetailSpecification.hasStockTakeId(stockTakeId));

    return stockTakeDetailRepository.findAll(specification, pageRequest)
        .map((i) -> StockTakeDetailMapper.INSTANCE.toDto(i));
  }
}
