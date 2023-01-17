package mapoints.mpesa.view;

import mapoints.lib.view.BaseView;
import mapoints.mpesa.model.MpesaC2BCallback;
import mapoints.payment.model.TransactionVerificationStatus;

import java.math.BigDecimal;

public class MpesaC2BView extends BaseView<MpesaC2BCallback> {

    public MpesaC2BView(MpesaC2BCallback entity) {
        super(entity);
    }

    public String getBillRefNumber(){
        return entity.getBillRefNumber();
    }

    public String getMsisdn(){
        return entity.getPhoneNumber();
    }

    public String getFullName(){
        return entity.getFirstName()
            +" "
            + entity.getMiddleName()
            +" "
            +entity.getLastName();
    }

   public BigDecimal getNewOrgBalance(){
        return entity.getOrgAccountBalance();
   }

   public BigDecimal getTransactionAmount(){
        return entity.getTransactionAmount();
   }

    public String getTransactionCode(){
        return entity.getTransactionCode();
    }

    public TransactionVerificationStatus getVerificationStatus(){
        return entity.getVerificationStatus();
    }

    public String getValidationResultDesc(){
        if(entity.getValidationResponse() != null) {
            return entity.getValidationResponse().getResultDesc();
        }else{
            return null;
        }
    }
}
