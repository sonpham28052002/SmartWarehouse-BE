package vn.edu.iuh.fit.smartwarehousebe.utils.helpers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserImportRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 9/3/25
 */
@Slf4j
public class UserCsvHelper {
    UserCsvHelper() {}

    public static final String TYPE = "text/csv";
    static final String[] HEADERs = {"id", "code", "userName", "password", "email", "phoneNumber", "fullName", "address", "sex", "dateOfBirth", "profilePicture", "status", "role"};

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<UserImportRequest> csvToUserRequest(InputStream is) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser =
                     new CSVParser(fileReader, CSVFormat.DEFAULT.builder()
                             .setHeader(HEADERs)
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build());
        ) {
            List<UserImportRequest> users = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                UserImportRequest user = new UserImportRequest(
                        csvRecord.get(HEADERs[1]),
                        csvRecord.get(HEADERs[2]),
                        csvRecord.get(HEADERs[3]),
                        csvRecord.get(HEADERs[4]),
                        csvRecord.get(HEADERs[5]),
                        csvRecord.get(HEADERs[6]),
                        csvRecord.get(HEADERs[7]),
                        Boolean.parseBoolean(csvRecord.get(HEADERs[8])),
                        LocalDateTime.parse(csvRecord.get(HEADERs[9])),
                        csvRecord.get(HEADERs[10]),
                        UserStatus.valueOf(csvRecord.get(HEADERs[11])),
                        Role.valueOf(csvRecord.get(HEADERs[12]))
                );
                users.add(user);
            }

            return users;
        } catch (IOException e) {
           throw new IOException("Failed to parse CSV file", e);
        }
    }

    public static ByteArrayInputStream usersToCSV(List<UserResponse> userResponses) throws IOException {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERs)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .setQuoteMode(QuoteMode.MINIMAL)
                .build();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), csvFormat);) {
            for (UserResponse user : userResponses) {
                List<String> data = Arrays.asList(
                        String.valueOf(user.getId()),
                        user.getCode(),
                        user.getUserName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        user.getAddress(),
                        String.valueOf(user.isSex()),
                        String.valueOf(user.getDateOfBirth()),
                        user.getProfilePicture(),
                        user.getStatus().name(),
                        user.getRole().name()
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new IOException("Failed to import data to CSV file: " + e.getMessage(), e);
        }
    }

}