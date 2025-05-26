package vn.edu.iuh.fit.smartwarehousebe.utils.helpers;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */
public class MultipartFileUtil {
  public static MultipartFile createMultipartFile(ByteArrayOutputStream byteArrayOutputStream, String fileName, String contentType) throws IOException {
    return new MockMultipartFile(fileName, fileName, contentType, byteArrayOutputStream.toByteArray());
  }
}