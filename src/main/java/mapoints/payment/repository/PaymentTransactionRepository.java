package mapoints.payment.repository;

import mapoints.lib.repository.BaseRepository;
import mapoints.payment.model.PaymentChannel;
import mapoints.payment.model.PaymentTransaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends BaseRepository<PaymentTransaction> {
    Optional<PaymentTransaction> findByTransactionIdAndPaymentChannel(String transactionId,
                                                                             PaymentChannel paymentChannel);
}
