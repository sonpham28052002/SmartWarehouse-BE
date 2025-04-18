package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import lombok.Value;

import java.time.LocalDate;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */
@Value
public class GetTransactionBetweenRequest {
  LocalDate startDate;
  LocalDate endDate;
}
