package vn.edu.iuh.fit.smartwarehousebe.constants;

import vn.edu.iuh.fit.smartwarehousebe.enums.Role;

import java.util.List;

public class RuleConstant {
    public static List<String> fullRole = List.of(Role.ADMIN.name(), Role.MANAGER.name(), Role.SUPERVISOR.name(), Role.USER.name());
}
