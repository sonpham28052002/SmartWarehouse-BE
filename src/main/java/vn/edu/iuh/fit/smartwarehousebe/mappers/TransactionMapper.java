package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
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
