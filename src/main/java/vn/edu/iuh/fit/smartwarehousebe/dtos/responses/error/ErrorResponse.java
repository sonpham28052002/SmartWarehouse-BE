package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.error;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private String message;
    private List<String> details;
}
