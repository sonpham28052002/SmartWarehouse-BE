package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange;

import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;

@Value
public class GetExchangeRequest {

  String supplierName;
  String supplierCode;
  String transactionCode;
  String code;
  String type;
}