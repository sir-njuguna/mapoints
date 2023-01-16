package mapoints.user.form;

import lombok.Getter;
import lombok.Setter;
import mapoints.user.model.UserType;

import javax.validation.constraints.NotBlank;

public class UserForm {
    @NotBlank(message = "error.empty-phone-number")
    @Getter
    @Setter
    private String phoneNumber;

    @NotBlank(message = "error.empty-password")
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private UserType userType;
}
