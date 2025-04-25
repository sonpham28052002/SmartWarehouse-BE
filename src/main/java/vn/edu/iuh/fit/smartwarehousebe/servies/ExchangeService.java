package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange.GetExchangeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ExchangeMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ExchangeRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.ExchangeSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.PartnerSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

@Service
public class ExchangeService {

  @Autowired
  private ExchangeRepository exchangeRepository;

  public Page<ExchangeResponse> getAll(PageRequest pageRequest, GetExchangeRequest request) {
    Specification<Exchange> specification = SpecificationBuilder.<Exchange>builder()
        .with(ExchangeSpecification.hasSupplierName(request.getSupplierName()))
        .with(ExchangeSpecification.hasSupplierCode(request.getSupplierCode()))
        .with(ExchangeSpecification.hasTransactionCode(request.getTransactionCode()))
        .with(ExchangeSpecification.hasCode(request.getCode()))
        .with(ExchangeSpecification.hasType(request.getType()))
        .build();

    return exchangeRepository.findAll(specification, pageRequest)
        .map((i) -> ExchangeMapper.INSTANCE.toDto(i));
  }


}
