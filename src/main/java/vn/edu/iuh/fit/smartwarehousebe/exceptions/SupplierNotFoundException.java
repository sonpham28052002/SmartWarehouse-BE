package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class SupplierNotFoundException extends NoSuchElementException {
    public SupplierNotFoundException() {
        super("Supplier not found");
    }
}
