package mapoints.user.form;

import lombok.Getter;
import lombok.Setter;
import mapoints.user.model.UserType;

import javax.validation.constraints.NotBlank;

public class PhoneNumberForm {
    @NotBlank(message = "error.empty-phone-number")
    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private UserType userType;
}
