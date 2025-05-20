package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ExchangeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;

@Service
public class StatisticsService {

  @Autowired
  private PartnerService partnerService;

  @Autowired
  private UserService userService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private StockTakeService stockTakeService;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ExchangeRepository exchangeRepository;

  @Autowired
  private ProductRepository productRepository;

  public Map<String, Object> statisticsPartner(GetPartnerQuest quest) {
    List<PartnerResponse> responses = partnerService.getAll(quest);

    long supplierCount = responses.stream().filter((i) -> i.getType() == PartnerType.SUPPLIER)
        .count();
    long agentCount = responses.stream().filter((i) -> i.getType() == PartnerType.AGENT).count();
    long totalCount = responses.size();

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("supplier_count", supplierCount);
    statistics.put("agent_count", agentCount);
    statistics.put("total_count", totalCount);

    return statistics;
  }

  public Map<String, Object> statisticsUser(GetUserQuest quest) {
    List<User> responses = userService.getAllUser(quest);

    long activeCount = responses.stream().filter((i) -> i.getStatus() == UserStatus.ACTIVE)
        .count();
    long deleteCount = responses.stream().filter((i) -> i.getStatus() == UserStatus.DELETED)
        .count();
    long totalCount = responses.size();

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("active_count", activeCount);
    statistics.put("delete_count", deleteCount);
    statistics.put("total_count", totalCount);

    return statistics;
  }

  public Map<String, Object> statisticsTransaction(GetTransactionQuest quest) {
    List<TransactionResponse> responses = transactionService.getTransactions(quest);
    long importFormWHCount = responses.stream()
        .filter((i) -> i.getTransactionType() == TransactionType.IMPORT_FROM_WAREHOUSE)
        .count();
    long importFormSupplierCount = responses.stream()
        .filter((i) -> i.getTransactionType() == TransactionType.IMPORT_FROM_SUPPLIER)
        .count();
    long exportCount = responses.stream()
        .filter((i) -> i.getTransactionType() == TransactionType.EXPORT_FROM_WAREHOUSE)
        .count();
    long waitingCount = responses.stream().filter((i) -> i.getStatus() == TransactionStatus.WAITING)
        .count();
    long inProcessCount = responses.stream()
        .filter((i) -> i.getStatus() == TransactionStatus.IN_PROCESS)
        .count();
    long pending_approval_count = responses.stream()
        .filter((i) -> i.getStatus() == TransactionStatus.PENDING_APPROVAL)
        .count();
    long completeCount = responses.stream()
        .filter((i) -> i.getStatus() == TransactionStatus.COMPLETE)
        .count();

    long export_exchange_count = responses.stream()
        .filter((i) -> i.getTransactionType() == TransactionType.EXPORT_EXCHANGE)
        .count();
    ;

    long import_exchange_count = responses.stream()
        .filter((i) -> i.getTransactionType() == TransactionType.IMPORT_EXCHANGE)
        .count();
    ;

    long totalCount = responses.stream()
        .filter((i) -> i.getTransactionType() != TransactionType.INVENTORY)
        .count();
    ;

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("import_form_wh_count", importFormWHCount);
    statistics.put("import_form_supplier_count", importFormSupplierCount);
    statistics.put("waiting_count", waitingCount);
    statistics.put("in_process_count", inProcessCount);
    statistics.put("export_count", exportCount);
    statistics.put("complete_count", completeCount);
    statistics.put("pending_approval_count", pending_approval_count);
    statistics.put("import_exchange_count", import_exchange_count);
    statistics.put("export_exchange_count", export_exchange_count);
    statistics.put("total_count", totalCount);

    return statistics;
  }

  public Map<String, Object> statisticsStockTake(GetStockTakeRequest quest) {
    List<StockTakeResponse> responses = stockTakeService.getAll(quest);
    long damageCount = 0;
    long calculateDifference = 0;

    Set<Long> productCount = new HashSet<>();
    for (StockTakeResponse stockTakeResponse : responses) {
      for (StockTakeDetailResponse detailResponse : stockTakeResponse.getStockTakeDetails()) {
        productCount.add(detailResponse.getInventory().getProduct().getId());
        damageCount += detailResponse.getDamagedQuantity();
        calculateDifference += Math.abs(
            detailResponse.getExpectedQuantity() - detailResponse.getActualQuantity());
      }
    }

    long totalCount = responses.size();

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("damage_count", damageCount);
    statistics.put("product_count", productCount.size());
    statistics.put("calculateDifference", calculateDifference);
    statistics.put("total_count", totalCount);

    return statistics;
  }

  public Map<String, Object> statisticsPartner(GetTransactionQuest quest) {
    List<TransactionResponse> responses = transactionService.getTransactions(quest);

    long agentCount = responses.stream()
        .filter((i) -> i.getPartner() != null && i.getPartner().getType() == PartnerType.AGENT)
        .count();
    long supplierCount = responses.stream()
        .filter((i) -> i.getPartner() != null && i.getPartner().getType() == PartnerType.SUPPLIER)
        .count();

    long totalCount = responses.stream()
        .filter((i) -> i.getPartner() != null)
        .count();

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("agent_count", agentCount);
    statistics.put("supplier_count", supplierCount);
    statistics.put("total_count", totalCount);

    return statistics;
  }

  public Map<String, Long> getCompletedTransactionCountByDateRange(GetTransactionQuest quest) {
    String startDate = quest.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String endDate = quest.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    List<Object[]> result = transactionRepository.countTransactionsByDateRangeAndStatus(
        startDate, endDate, TransactionStatus.COMPLETE.name(), quest.getTransactionType());
    Map<String, Long> transactionCountByDate = new HashMap<>();
    System.out.println(result.size());
    for (Object[] row : result) {
      if (row != null && row.length == 2) {
        Date day = (Date) row[0];
        Long count = (Long) row[1];

        if (day != null) {
          LocalDate localDate = day.toLocalDate();
          transactionCountByDate.put(localDate.toString(), count);
        }
      }
    }

    return transactionCountByDate;
  }

  public Map<String, Object> statisticsExchange(GetTransactionQuest quest) {
    LocalDateTime from = quest.getStartDate();
    LocalDateTime to = quest.getEndDate();

    Map<String, Object> statistics = new HashMap<>();
    List<Exchange> exchanges = exchangeRepository.findAllByCreatedDateBetweenAndTypeIn(from, to,
        List.of(
            ExchangeType.EXCHANGE, ExchangeType.RETURN, ExchangeType.UNSPECIFIED));
    int countExchange = (int) exchanges.stream().filter((i) -> i.getType() == ExchangeType.EXCHANGE)
        .count();
    int countReturn = (int) exchanges.stream().filter((i) -> i.getType() == ExchangeType.RETURN)
        .count();
    int countUnspecified = (int) exchanges.stream().filter((i) -> i.getType() == ExchangeType.UNSPECIFIED)
        .count();
    statistics.put("exchange", countExchange);
    statistics.put("return", countReturn);
    statistics.put("total", exchanges.size());
    statistics.put("unspecified", countUnspecified);

    return statistics;
  }

  public List<Object> findTop10ExportedProducts(GetTransactionQuest quest) {
    LocalDateTime from = quest.getStartDate();
    LocalDateTime to = quest.getEndDate();
    Pageable topTen = PageRequest.of(0, 10);
    List<Object[]> list = transactionRepository.findTop10Products(
        List.of(TransactionType.EXPORT_FROM_WAREHOUSE, TransactionType.EXPORT_EXCHANGE), from, to,
        topTen);

    List<Object> results = new ArrayList<>();
    int count = 0;
    for (Object[] objects:list) {
      Map<Integer, Map<String, Object>> result = new HashMap<>();
      ProductResponse productResponse = ProductMapper.INSTANCE.toDto(productRepository.findByCode(String.valueOf(objects[0])).get());
      result.put(++count, Map.of("product", productResponse, "count", Integer.parseInt(objects[1].toString())));
      results.add(result);
    }
    return results;
  }

  public List<Object> findTop10ImportedProducts(GetTransactionQuest quest) {
    LocalDateTime from = quest.getStartDate();
    LocalDateTime to = quest.getEndDate();
    Pageable topTen = PageRequest.of(0, 10);
    List<Object[]> list = transactionRepository.findTop10Products(
        List.of(TransactionType.IMPORT_FROM_WAREHOUSE, TransactionType.IMPORT_FROM_SUPPLIER), from, to,
        topTen);

    List<Object> results = new ArrayList<>();
    int count = 0;
    for (Object[] objects:list) {
      Map<Integer, Map<String, Object>> result = new HashMap<>();
      ProductResponse productResponse = ProductMapper.INSTANCE.toDto(productRepository.findByCode(String.valueOf(objects[0])).get());
      result.put(++count, Map.of("product", productResponse, "count", Integer.parseInt(objects[1].toString())));
      results.add(result);
    }

    return results;
  }

}
