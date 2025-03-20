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
  @Mapping(target = "storageLocation", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "id", ignore = true)
  TransactionDetail toEntity(TransactionRequest.TransactionDetailRequest detailRequest);

  @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "warehouse", ignore = true)
  @Mapping(target = "transfer", ignore = true)
  @Mapping(target = "supplier", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "executor", ignore = true)
  Transaction toEntity(TransactionRequest transactionResponse);

  @Mapping(target = "warehouseCode", source = "warehouse.code")
  @Mapping(target = "transferCode", source = "transfer.code")
  @Mapping(target = "supplierCode", source = "supplier.code")
  @Mapping(target = "executorCode", source = "executor.code")
  TransactionResponse toDto(Transaction transaction);

  @Mapping(target = "warehouseCode", source = "warehouse.code")
  @Mapping(target = "transferCode", source = "transfer.code")
  @Mapping(target = "supplierCode", source = "supplier.code")
  @Mapping(target = "executorCode", source = "executor.code")
  @Mapping(target = "details", source = "details")
  TransactionWithDetailResponse toDtoWithDetail(Transaction transaction);

  @Mapping(target = "productCode", source = "product.code")
  @Mapping(target = "storageLocationId", source = "storageLocation.id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "unitCode", source = "unit.code")
  TransactionWithDetailResponse.TransactionDetailResponse toDetailResponse(TransactionDetail transactionDetail);

  default List<TransactionWithDetailResponse.TransactionDetailResponse> mapDetails(Set<TransactionDetail> details) {
    return details.stream().map(this::toDetailResponse).toList();
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Transaction partialUpdate(TransactionResponse transactionResponse, @MappingTarget Transaction transaction);
}
