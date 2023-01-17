package mapoints.payment.service;

import mapoints.account.form.LedgerAction;
import mapoints.payment.form.PostedPaymentForm;
import javax.annotation.Nullable;

public interface OnPostPaymentListener {
    @Nullable
    LedgerAction onReceiptOfPayment(PostedPaymentForm payment);
}
