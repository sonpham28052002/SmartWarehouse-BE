package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import lombok.Value;
/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
@Value
public class GetTransactionQuest {
    String transactionType;
    String transactionDate;
    Long executor;
    Long warehouse;
    Long transfer;
    Long supplier;
}