package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.inventory.GetInventoryRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.InventoryService;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

  @Autowired
  private InventoryService inventoryService;

  @GetMapping
  public Page<InventoryResponse> getAll(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetInventoryRequest request) {

    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    return inventoryService.getAll(pageRequest, request);
  }

}
