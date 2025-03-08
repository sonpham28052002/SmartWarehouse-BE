package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

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
}
