package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.servies.StatisticsService;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  @Autowired
  private StatisticsService statisticsService;

  @GetMapping("/statisticsPartner")
  public Map<String, Object> statisticsPartner(GetPartnerQuest quest) {
    return statisticsService.statisticsPartner(quest);
  }

  @GetMapping("/statisticsUser")
  public Map<String, Object> statisticsUser(GetUserQuest quest) {
    return statisticsService.statisticsUser(quest);
  }

  @GetMapping("/statisticsTransaction")
  public Map<String, Object> statisticsTransaction(GetTransactionQuest quest) {
    return statisticsService.statisticsTransaction(quest);
  }

  @GetMapping("/statisticsStockTake")
  public Map<String, Object> statisticsStockTake(GetStockTakeRequest quest) {
    return statisticsService.statisticsStockTake(quest);
  }

  @GetMapping("/statisticsPartnerTransaction")
  public Map<String, Object> statisticsPartner(GetTransactionQuest quest) {
    return statisticsService.statisticsPartner(quest);
  }

  @GetMapping("/getCompletedTransactionCountByDateRange")
  public Map<String, Long> getCompletedTransactionCountByDateRange(GetTransactionQuest quest) {
    return statisticsService.getCompletedTransactionCountByDateRange(quest);
  }

  @GetMapping("/statisticsExchange")
  public Map<String, Object> statisticsExchange(GetTransactionQuest quest) {
    return statisticsService.statisticsExchange(quest);
  }

  @GetMapping("/findTop10ExportedProducts")
  public List<Object> findTop10ExportedProducts(GetTransactionQuest quest) {
    return statisticsService.findTop10ExportedProducts(quest);
  }

  @GetMapping("/findTop10ImportedProducts")
  public List<Object> findTop10ImportedProducts(GetTransactionQuest quest) {
    return statisticsService.findTop10ImportedProducts(quest);
  }
}
