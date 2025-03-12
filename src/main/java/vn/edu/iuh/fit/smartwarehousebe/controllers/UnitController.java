package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.GetUnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.UnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.servies.UnitService;

@RestController
@RequestMapping("/units")
public class UnitController {

  @Autowired
  private UnitService unitService;

  @GetMapping
  public ResponseEntity<Page<UnitResponse>> index(
      @RequestParam(value = "per_page", defaultValue = "10") int perPage,
      @RequestParam(value = "current_page", defaultValue = "1") int currentPage,
      GetUnitRequest request) {
    return ResponseEntity.ok(unitService.getUnits(
        PageRequest.of(currentPage - 1, perPage, Sort.by(Sort.Direction.DESC, "id")), request));
  }

  @PostMapping
  public ResponseEntity<UnitResponse> create(@RequestBody UnitRequest unitRequest) {
    return ResponseEntity.ok(unitService.createAndUpdate(unitRequest));
  }

  @PutMapping("{id}")
  public ResponseEntity<UnitResponse> update(@PathVariable Long id,
      @RequestBody UnitRequest unitRequest) {
    unitRequest.setId(id);
    return ResponseEntity.ok(unitService.createAndUpdate(unitRequest));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Boolean> delete(@PathVariable Long id) {
    return ResponseEntity.ok(unitService.deletedUnit(id));
  }

  @GetMapping("/all")
  public ResponseEntity<List<UnitResponse>> getAllUnit(GetUnitRequest request) {
    return ResponseEntity.ok(unitService.getAllUnit(request));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(unitService.checkCodeIsExist(Unit.class, code));
  }
}
