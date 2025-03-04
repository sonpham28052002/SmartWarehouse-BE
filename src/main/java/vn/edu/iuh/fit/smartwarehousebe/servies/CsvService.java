package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CsvService {

    public String readCsv(MultipartFile file) throws IOException, CsvValidationException {
        String data ="";
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                System.out.println("Line: " + String.join(", ", line));
                data += String.join(", ", line)+"###";
            }
        }
        return data;
    }
}
