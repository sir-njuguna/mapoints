package mapoints.user.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class PasswordForm extends PhoneNumberForm{

    @NotBlank(message = "error.empty-password")
    @Getter
    @Setter
    private String password;
}
