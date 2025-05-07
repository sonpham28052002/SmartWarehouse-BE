package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.CreatePartnerRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;

import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.servies.PartnerService;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@RestController
@RequestMapping("/partners")
public class PartnerController {

  private final PartnerService partnerService;

  public PartnerController(PartnerService partnerService) {
    this.partnerService = partnerService;
  }

  @GetMapping()
  public ResponseEntity<Page<PartnerResponse>> getPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetPartnerQuest partnerQuest
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(partnerService.getAll(pageRequest, partnerQuest));
  }

  @GetMapping("/all")
  public ResponseEntity<List<PartnerResponse>> getAll(GetPartnerQuest partnerQuest) {
    return ResponseEntity.ok(partnerService.getAll(partnerQuest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PartnerResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(partnerService.getById(id));
  }

  @PostMapping()
  public ResponseEntity<PartnerResponse> create(
      @RequestBody @Valid CreatePartnerRequest request) {
    return ResponseEntity.ok(partnerService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PartnerResponse> update(@PathVariable Long id,
      @RequestBody @Valid CreatePartnerRequest request) {
    return ResponseEntity.ok(partnerService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(@PathVariable Long id) {
    return ResponseEntity.ok(partnerService.delete(id));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(partnerService.checkCodeIsExist(Partner.class, code));
  }
}
