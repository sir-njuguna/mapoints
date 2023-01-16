package mapoints.user.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class VerificationCode extends BaseModel {
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private Date expiryTime;

    @Getter
    @Setter
    private Integer codeCount = 0;

    @Getter
    @Setter
    private Date timeLastSent = new Date();
}
