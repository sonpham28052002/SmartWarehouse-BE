package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.smartwarehousebe.annotations.CsvField;

/**
 * DTO for export transactions from CSV with the format:
 * description,warehouse_code,transfer_code,product_code,quantity,unit_code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionExportCsvRequest {
    @CsvField("description")
    private String description;
    
    @CsvField("warehouse_code")
    private String warehouseCode;

    @CsvField("transfer_code")
    private String transferCode;

    @CsvField("product_code")
    private String productCode;
    
    @CsvField("quantity")
    private Integer quantity;
    
    @CsvField("unit_code")
    private String unitCode;
}
