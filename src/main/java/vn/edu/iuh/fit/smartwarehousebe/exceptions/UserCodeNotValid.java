package vn.edu.iuh.fit.smartwarehousebe.exceptions;

public class UserCodeNotValid extends IllegalArgumentException {
    public UserCodeNotValid() {
        super("Code must be follow the format: USER-####. # is a number");
    }
}
