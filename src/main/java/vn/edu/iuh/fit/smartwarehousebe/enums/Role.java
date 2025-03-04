package vn.edu.iuh.fit.smartwarehousebe.enums;


import lombok.Getter;

@Getter
public enum Role {
    ADMIN(0), USER(1), SUPERVISOR(2), MANAGER(3);
    private final int role;

    Role(int role) {
        this.role = role;
    }
}
