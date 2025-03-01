package vn.edu.iuh.fit.smartwarehousebe.enums;


import lombok.Getter;

@Getter
public enum Role {
    ADMIN(1), USER(2), SUPERVISOR(3), MANAGER(4);
    private final int role;

    Role(int role) {
        this.role = role;
    }
}
