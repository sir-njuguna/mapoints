package mapoints.mpesa.repository;


import mapoints.lib.repository.BaseRepository;
import mapoints.mpesa.model.MpesaC2BCallback;
import mapoints.payment.model.TransactionVerificationStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MpesaC2BCallbackRepository extends BaseRepository<MpesaC2BCallback>,
        JpaSpecificationExecutor<MpesaC2BCallback> {
    Optional<MpesaC2BCallback> findByValidationOccasion(String validationOccasion);
    boolean existsByTransactionCodeAndVerificationStatusIn(String transactionCode,
                                                         List<TransactionVerificationStatus> statuses);

    static Specification<MpesaC2BCallback> timeCreatedBetween(Date startDate, Date endDate){
        return (root, cb, cq) -> cq.between(root.get("timeCreated"), startDate, endDate);
    }

    static Specification<MpesaC2BCallback> phoneNumberLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("phoneNumber"), "%"+keyWord+"%");
    }

    static Specification<MpesaC2BCallback> transactionCodeLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("transactionCode"), "%"+keyWord+"%");
    }

    static Specification<MpesaC2BCallback> billingReferenceNumberLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("billRefNumber"), "%"+keyWord+"%");
    }
}
