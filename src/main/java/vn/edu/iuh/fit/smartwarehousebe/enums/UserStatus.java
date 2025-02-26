package vn.edu.iuh.fit.smartwarehousebe.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
        ACTIVE(1),
        DELETED(2);
    private final int status;

    UserStatus(int status) {
        this.status = status;
    }
}
