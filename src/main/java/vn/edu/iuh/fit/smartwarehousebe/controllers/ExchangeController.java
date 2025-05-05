package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange.CreateExchangeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange.GetExchangeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.servies.ExchangeService;

@RestController
@RequestMapping("/exchanges")
public class ExchangeController {

  @Autowired
  private ExchangeService exchangeService;

  @GetMapping()
  public ResponseEntity<Page<ExchangeResponse>> getPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetExchangeRequest request
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(exchangeService.getAll(pageRequest, request));
  }

  @PostMapping("/create")
  public ExchangeResponse createExchange(@RequestBody CreateExchangeRequest request, @AuthenticationPrincipal User user) {
    return exchangeService.createExchange(request, user);
  }

  @PostMapping("{exchangeId}/return")
  public ExchangeResponse createExchange(@PathVariable Long exchangeId, @AuthenticationPrincipal User user) {
    return exchangeService.returnExchange(exchangeId, user);
  }

}
