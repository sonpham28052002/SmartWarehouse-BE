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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CsvService {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


  public String[][] readCsv(MultipartFile file) throws IOException, CsvValidationException {
    List<String[]> dataList = new ArrayList<>();

    try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
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
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
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
        if (List.class.isAssignableFrom(field.getType())) {
          List<?> details = parseListField(record, field);
          field.set(instance, details);
        } else {
          Object value = convertValue(record.get(csvColumn), field.getType());
          field.set(instance, value);
        }
      }
    }
    return instance;
  }

  private List<?> parseListField(CSVRecord record, Field field) throws Exception {
    Type genericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    Class<?> elementType = (Class<?>) genericType;

    List<Object> list = new ArrayList<>();
    int index = 1;
    while (record.isMapped("detail" + index + "_productId")) {
      Object detailInstance = elementType.getDeclaredConstructor().newInstance();
      for (Field subField : elementType.getDeclaredFields()) {
        if (subField.isAnnotationPresent(CsvField.class)) {
          CsvField csvField = subField.getAnnotation(CsvField.class);
          String column = "detail" + index + "_" + csvField.value();
          subField.setAccessible(true);
          subField.set(detailInstance, convertValue(record.get(column), subField.getType()));
        }
      }
      list.add(detailInstance);
      index++;
    }
    return list;
  }

  private Object convertValue(String value, Class<?> type) {
    if (value == null || value.isBlank()) return null;
    if (type == String.class) return value;
    if (type == Integer.class || type == int.class) return Integer.parseInt(value);
    if (type == Long.class || type == long.class) return Long.parseLong(value);
    if (type == Double.class || type == double.class) return Double.parseDouble(value);
    if (type.isEnum()) return Enum.valueOf((Class<Enum>) type, value);
    if (type == LocalDateTime.class) return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    return value;
  }
}
