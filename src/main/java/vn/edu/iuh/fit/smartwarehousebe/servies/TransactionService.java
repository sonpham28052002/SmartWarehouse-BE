package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionSpecification;

/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
         * Retrieves a transaction by its ID.
         *
         * @param id the ID of the transaction
         * @return the transaction response
         * @throws TransactionNotFoundException if the transaction is not found
         */
    @Cacheable(value = "transactions", key = "#id")
    public TransactionResponse getTransaction(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toDto)
                .orElseThrow(TransactionNotFoundException::new);
    }

    /**
         * Creates a new transaction.
         *
         * @param request the transaction request
         * @return the transaction response
         */
    @Cacheable(value = "transactions", key = "#request.transactionType + '_' + #request.transactionDate")
    public TransactionResponse create(TransactionRequest request) {
        Transaction transaction = transactionMapper.toEntity(request);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    /**
     * Retrieves a paginated list of transactions based on the provided criteria.
     *
     * @param pageRequest the pagination information
     * @param quest the criteria for filtering transactions
     * @return a paginated list of transaction responses
     */
    public Page<TransactionResponse> getTransactions(PageRequest pageRequest, GetTransactionQuest quest) {
        Specification<Transaction> specification = SpecificationBuilder.<Transaction>builder()
                .with(TransactionSpecification.hasTransactionType(quest.getTransactionType()))
                .with(TransactionSpecification.hasTransactionDate(quest.getTransactionDate()))
                .with(TransactionSpecification.hasTransactionExecutor(quest.getExecutor()))
                .with(TransactionSpecification.hasTransactionWarehouse(quest.getWarehouse()))
                .with(TransactionSpecification.hasTransactionTransfer(quest.getTransfer()))
                .with(TransactionSpecification.hasTransactionSupplier(quest.getSupplier()))
                .build();

        return transactionRepository.findAll(specification, pageRequest)
                .map(transactionMapper::toDto);
    }
}
