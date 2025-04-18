package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class StorageService {

  @Value("${cloud.aws.s3.bucket_name}")
  private String bucketName;

  @Autowired
  private AmazonS3 s3Client;

  public String uploadFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File upload không được rỗng!");
    }

    try {
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

      // Thiết lập metadata
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());
      metadata.setContentType(file.getContentType());

      // Upload file từ InputStream
      s3Client.putObject(
          new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

      // Trả về URL của file
      return s3Client.getUrl(bucketName, fileName).toString();
    } catch (IOException e) {
      throw new RuntimeException("Lỗi khi đọc file: " + e.getMessage(), e);
    } catch (AmazonServiceException e) {
      throw new RuntimeException("Lỗi khi upload lên S3: " + e.getMessage(), e);
    }
  }


  public byte[] downloadFile(String fileName) {
    S3Object s3Object = s3Client.getObject(bucketName, fileName);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    try {
      byte[] content = IOUtils.toByteArray(inputStream);
      return content;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public Boolean deleteFile(String fileName) {
    try {
      s3Client.deleteObject(bucketName, fileName);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;
  }

  private File convertMultiPartFileToFile(MultipartFile file) {
    File convertedFile = new File(file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(file.getBytes());
    } catch (IOException e) {
    }
    return convertedFile;
  }
}
