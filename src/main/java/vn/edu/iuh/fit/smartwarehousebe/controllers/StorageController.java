package vn.edu.iuh.fit.smartwarehousebe.controllers;


import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.servies.CsvService;
import vn.edu.iuh.fit.smartwarehousebe.servies.StorageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException, CsvValidationException {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @PostMapping(value = "/uploadMulti", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMultiFile(@RequestParam(value = "file") List<MultipartFile> files) {
        files.parallelStream().forEach(file -> {
            service.uploadFile(file);
        });
        return ResponseEntity.ok("Upload all file success");
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @Transactional
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }

    @DeleteMapping("/deleteMulti")
    @Transactional
    public ResponseEntity<String> deleteMultiFile(@RequestBody  List<String> fileNames) {
        fileNames.parallelStream().forEach(file -> {
            service.deleteFile(file);
        });
        return ResponseEntity.ok("Delete all file success");
    }
}