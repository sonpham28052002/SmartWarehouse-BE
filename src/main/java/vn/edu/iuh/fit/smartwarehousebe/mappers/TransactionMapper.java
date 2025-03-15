package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
   @Mapping(target = "transaction", ignore = true)
   @Mapping(target = "storageLocation", ignore = true)
   @Mapping(target = "product", ignore = true)
   @Mapping(target = "id", ignore = true)
   TransactionDetail toEntity(TransactionRequest.TransactionDetailRequest detailRequest);

   @Mapping(target = "warehouse", ignore = true)
   @Mapping(target = "transfer", ignore = true)
   @Mapping(target = "supplier", ignore = true)
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "executor", ignore = true)
   Transaction toEntity(TransactionRequest transactionResponse);

   @Mapping(target = "warehouseId", source = "warehouse.id")
   @Mapping(target = "transferId", source = "transfer.id")
   @Mapping(target = "supplierId", source = "supplier.id")
   @Mapping(target = "executorId", source = "executor.id")
   TransactionResponse toDto(Transaction transaction);

   @Mapping(target = "warehouseId", source = "warehouse.id")
   @Mapping(target = "transferId", source = "transfer.id")
   @Mapping(target = "supplierId", source = "supplier.id")
   @Mapping(target = "executorId", source = "executor.id")
   TransactionWithDetailResponse toDtoWithDetail(Transaction transaction);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Transaction partialUpdate(TransactionResponse transactionResponse, @MappingTarget Transaction transaction);
}
