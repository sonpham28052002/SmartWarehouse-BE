package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user;

import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUserQuest {
    private String code;
    private String fullName;
    private UserStatus status;
    private boolean getAll;
}
