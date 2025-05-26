package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
  public UserNotFoundException() {
    super("User not found");
  }
}
