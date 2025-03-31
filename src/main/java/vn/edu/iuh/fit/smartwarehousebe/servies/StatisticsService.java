package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;

@Service
public class StatisticsService {

  @Autowired
  private PartnerService partnerService;

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

}
