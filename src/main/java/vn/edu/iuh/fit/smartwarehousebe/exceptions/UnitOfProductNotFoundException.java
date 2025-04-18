package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class UnitOfProductNotFoundException extends NoSuchElementException {
  public UnitOfProductNotFoundException() {
    super("Unit of product not found");
  }
}
