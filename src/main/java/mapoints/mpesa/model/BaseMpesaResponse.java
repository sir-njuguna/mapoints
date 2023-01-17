package mapoints.mpesa.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mapoints.lib.model.BaseModel;
import net.minidev.json.JSONObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@MappedSuperclass
abstract class BaseMpesaResponse extends BaseModel {
    @Column(precision = 19,scale = 10)
    protected BigDecimal amount;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private JSONObject payloadDump;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public JSONObject getPayloadDump() {
        return payloadDump;
    }

    public void setPayloadDump(JSONObject payloadDump) {
        this.payloadDump = payloadDump;
    }

    public void setPayloadDump(String payloadDump) {
        try {
            this.setPayloadDump(new ObjectMapper().readValue(payloadDump, JSONObject.class));
        }catch ( Exception e){
            e.printStackTrace();
        }
    }
}
