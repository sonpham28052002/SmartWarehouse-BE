package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
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
}
