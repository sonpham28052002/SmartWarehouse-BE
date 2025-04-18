package vn.edu.iuh.fit.smartwarehousebe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long>,
    JpaSpecificationExecutor<Partner> {

  Optional<Partner> findByCode(String partnerCode);
}