package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  @Mapping(target = "transaction", ignore = true)
  @Mapping(target = "inventory", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "id", ignore = true)
  TransactionDetail toEntity(TransactionRequest.TransactionDetailRequest detailRequest);

  @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "warehouse", ignore = true)
  @Mapping(target = "transfer", ignore = true)
  @Mapping(target = "supplier", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "executor", ignore = true)
  @Mapping(target = "transactionType", source = "transactionType")
  Transaction toEntity(TransactionRequest transactionResponse);

  @Mapping(target = "warehouse", source = "warehouse")
  @Mapping(target = "transfer", source = "transfer")
  @Mapping(target = "supplier", source = "supplier")
  @Mapping(target = "executor", source = "executor")
  @Mapping(target = "createdDate", source = "createdDate")
  TransactionResponse toDto(Transaction transaction);

  @Mapping(target = "warehouse.code", source = "warehouse.code")
  @Mapping(target = "transferCode", source = "transfer.code")
  @Mapping(target = "supplierCode", source = "supplier.code")
  @Mapping(target = "executorCode", source = "executor.code")
  @Mapping(target = "details", source = "details")
  TransactionWithDetailResponse toDtoWithDetail(Transaction transaction);

  @Mapping(target = "productCode", source = "product.code")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "quantity", source = "quantity")
  TransactionWithDetailResponse.TransactionDetailResponse toDetailResponse(
      TransactionDetail transactionDetail);

  default List<TransactionWithDetailResponse.TransactionDetailResponse> mapDetails(
      Set<TransactionDetail> details) {
    return details.stream().map(this::toDetailResponse).toList();
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Transaction partialUpdate(TransactionResponse transactionResponse,
      @MappingTarget Transaction transaction);
}
