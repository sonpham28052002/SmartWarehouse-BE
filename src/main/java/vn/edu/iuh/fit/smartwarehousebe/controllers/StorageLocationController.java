package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location.CreateStorageLocationRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location.GetStorageLocationRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location.StorageLocationResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.StorageLocationService;

@RestController
@RequestMapping("storage_location")
public class StorageLocationController {

  @Autowired
  private StorageLocationService storageLocationService;

  @PostMapping("/{shelfId}/create")
  public ResponseEntity<StorageLocationResponse> create(@PathVariable Long shelfId,
      @RequestBody CreateStorageLocationRequest request) {
    System.out.println(request);
    return ResponseEntity.ok(storageLocationService.create(shelfId, request));
  }

  @GetMapping("/{shelfId}/storageLocation")
  public ResponseEntity<Page<StorageLocationResponse>> getPage(
      @PathVariable Long shelfId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetStorageLocationRequest request
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
    return ResponseEntity.ok(storageLocationService.getAll(pageRequest, request, shelfId));
  }

  @GetMapping("/{shelfId}/storageLocation/all")
  public ResponseEntity<List<StorageLocationResponse>> getPage(
      @PathVariable Long shelfId,
      GetStorageLocationRequest request
  ) {
    return ResponseEntity.ok(storageLocationService.getAll(request, shelfId));
  }
}
