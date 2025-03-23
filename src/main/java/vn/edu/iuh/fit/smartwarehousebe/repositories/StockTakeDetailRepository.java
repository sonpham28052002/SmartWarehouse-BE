package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.Ids.StockTakeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;

@Repository
public interface StockTakeDetailRepository extends
    JpaRepository<StockTakeDetail, StockTakeDetailId>, JpaSpecificationExecutor<StockTakeDetail> {

}