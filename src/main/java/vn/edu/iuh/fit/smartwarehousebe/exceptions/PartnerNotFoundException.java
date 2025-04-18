package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class PartnerNotFoundException extends NoSuchElementException {

  public PartnerNotFoundException() {
    super("partner not found");
  }
}
