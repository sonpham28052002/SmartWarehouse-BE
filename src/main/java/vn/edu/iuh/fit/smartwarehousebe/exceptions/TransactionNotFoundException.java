package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class TransactionNotFoundException extends NoSuchElementException {
    public TransactionNotFoundException() {
        super("Transaction not found");
    }
}
