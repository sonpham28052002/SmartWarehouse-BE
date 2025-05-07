package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.Ids.ExchangeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange.CreateExchangeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange.GetExchangeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ExchangeMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.ExchangeDetail;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.DamagedProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ExchangeDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ExchangeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.ExchangeSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

@Service
public class ExchangeService {

  @Autowired
  private ExchangeRepository exchangeRepository;
  @Autowired
  private ExchangeDetailRepository exchangeDetailRepository;
  @Autowired
  private DamagedProductRepository damagedProductRepository;
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private StockTakeRepository stockTakeRepository;

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

  @Transactional
  public ExchangeResponse createExchange(CreateExchangeRequest request, User user) {
    DamagedProduct damagedProductFirst = damagedProductRepository.findById(
        request.getDamagedProducts().get(0).getId()).get();
    Partner partner = null;
    if (damagedProductFirst.getStockTakeDetail() != null) {
      partner = damagedProductFirst.getStockTakeDetail().getInventory().getProduct().getPartner();
    } else {
      partner = damagedProductFirst.getTransactionDetail().getInventory().getProduct().getPartner();
    }

    Transaction transaction = null;
    StockTake stockTake = null;
    if (request.getStockTakeCode() != null) {
      stockTake = stockTakeRepository.getByCode(request.getStockTakeCode()).get();
      System.out.println(stockTake.getCode());
    } else if (request.getTransactionCode() != null) {
      transaction = transactionRepository.getByCode(request.getTransactionCode()).get();
    }

    Exchange exchange = exchangeRepository.save(Exchange.builder()
        .creator(user)
        .code(generateExchangeCode())
        .supplier(partner)
        .note(request.getNote())
        .stockTake(stockTake)
        .originalTransaction(transaction)
        .type(ExchangeType.UNSPECIFIED)
        .build());
    List<ExchangeDetail> exchangeDetails = new ArrayList<>();
    for (DamagedProductResponse damagedProductResponse : request.getDamagedProducts()) {
      ExchangeDetail exchangeDetail = exchangeDetailRepository.save(ExchangeDetail.builder()
          .exchange(exchange)
          .id(ExchangeDetailId.builder()
              .exchangeId(exchange.getId())
              .damagedProductId(damagedProductResponse.getId())
              .build())
          .damagedProduct(
              damagedProductRepository.findById(damagedProductResponse.getId()).orElse(null))
          .quantity((int) damagedProductResponse.getQuantity())
          .type(ExchangeType.UNSPECIFIED)
          .build());
      exchangeDetails.add(exchangeDetail);
    }
    exchange.setExchangeDetails(exchangeDetails);

    return ExchangeMapper.INSTANCE.toDto(exchange);
  }

  public String generateExchangeCode() {
    LocalDateTime from = LocalDate.now().atStartOfDay();
    LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);

    int sequence = exchangeRepository.findTodaySequence(from, to);

    String prefix = "EXCH";
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String number = String.format("%03d", sequence);

    return String.format("%s-%s-%s", prefix, date, number);
  }

  public ExchangeResponse returnExchange(Long id, User user) {
    Exchange exchange = exchangeRepository.findById(id).orElse(null);
    if (exchange == null) {
      return null;
    }

    exchange.setType(ExchangeType.RETURN);
    exchange.setApprover(user);
    exchange.getExchangeDetails().stream().map((i) -> {
          i.setType(ExchangeType.RETURN);
          DamagedProduct damagedProduct = i.getDamagedProduct();
          damagedProduct.setExchangeType(ExchangeType.RETURN);
          damagedProductRepository.save(damagedProduct);
          return exchangeDetailRepository.save(i);
        }
    ).collect(Collectors.toList());

    return ExchangeMapper.INSTANCE.toDto(exchangeRepository.save(exchange));
  }


}
