package mapoints.user.form;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;

public class UserRegistrationForm extends LoginForm {
    @NotBlank(message = "error.blank-user-name")
    @Setter
    private String name;

    @NotBlank(message = "error.empty-verification-code")
    @Getter
    @Setter
    private String verificationCode;

    public String getName() {
        if(name != null){
            name = WordUtils.capitalize(name.toLowerCase());
        }
        return name;
    }
}
