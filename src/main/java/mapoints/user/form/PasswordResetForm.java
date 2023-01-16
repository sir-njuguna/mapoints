package mapoints.user.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class PasswordResetForm extends LoginForm {
    @NotBlank(message = "error.empty-verification-code")
    @Getter
    @Setter
    private String verificationCode;
}
