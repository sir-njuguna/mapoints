package mapoints.payment.service;


import mapoints.payment.model.PaymentChannel;
import mapoints.payment.model.PaymentException;
import mapoints.payment.model.PaymentResponse;
import net.minidev.json.JSONObject;

import java.math.BigDecimal;

/**
 * Definition of methods to be implemented by any payment implementation.
 */
public interface PaymentProvider {

    /**
     * Initializes requests to receive payments
     * @param recipientAccount account number to be debited. This can be a phone number for mobile payment services
     * @param amount to be paid
     * @return {@link PaymentResponse}
     */
    PaymentResponse initPayIn(String recipientAccount, BigDecimal amount) throws PaymentException;

    /**
     * Handles payin callback response
     * @param response a {@link JSONObject} posted back by the payments service after a payin has been donne
     * @return {@link PaymentResponse}
     */
    PaymentResponse onPayInCallback(JSONObject response);

    PaymentChannel getPaymentChannel();

}
