package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

public interface TransactionDetailRepository extends CrudRepository<TransactionDetail, Long>, PagingAndSortingRepository<TransactionDetail, Long> {
}