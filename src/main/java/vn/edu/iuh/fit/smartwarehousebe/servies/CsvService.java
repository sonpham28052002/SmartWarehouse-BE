package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.annotations.CsvField;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public CsvService() {
  }

  public String[][] readCsv(MultipartFile file) throws IOException, CsvValidationException {
    List<String[]> dataList = new ArrayList<>();

    try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      String[] line;
      while ((line = csvReader.readNext()) != null) {
        dataList.add(line);
      }
    }

    String[][] dataArray = new String[dataList.size()][];
    dataList.toArray(dataArray);

    return dataArray;
  }

  public <T> List<T> parseCsv(InputStream csvInputStream, Class<T> clazz) throws Exception {
    List<T> resultList = new ArrayList<>();

    try (Reader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
          .setHeader()
          .setSkipHeaderRecord(true)
          .build());
      for (CSVRecord record : csvParser) {
        resultList.add(mapCsvToObject(record, clazz));
      }
    }
    return resultList;
  }

  private <T> T mapCsvToObject(CSVRecord record, Class<T> clazz) throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();

    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(CsvField.class)) {
        CsvField csvField = field.getAnnotation(CsvField.class);
        String csvColumn = csvField.value();

        field.setAccessible(true);
        Object value = convertValue(record.get(csvColumn), field.getType());
        field.set(instance, value);
      }
    }
    return instance;
  }

  public Object convertValue(String value, Class<?> type) {
    if (value == null || value.isBlank()) return null;
    if (type == String.class) return value;
    if (type == Integer.class || type == int.class) return Integer.parseInt(value);
    if (type == Long.class || type == long.class) return Long.parseLong(value);
    if (type == Double.class || type == double.class) return Double.parseDouble(value);
    if (type.isEnum()) {
      try {
        Method valueOfMethod = type.getMethod("valueOf", String.class);
        return valueOfMethod.invoke(null, value);
      } catch (Exception e) {
        throw new RuntimeException("Error converting to enum: " + e.getMessage(), e);
      }
    }
    if (type == LocalDateTime.class) return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    return value;
  }
}

