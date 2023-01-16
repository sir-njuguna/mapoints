package mapoints.user.form;


import lombok.Setter;
import org.apache.commons.lang.WordUtils;

import javax.validation.constraints.NotBlank;

public class UserRegistrationForm extends UserForm{
    @NotBlank(message = "error.blank-user-name")
    @Setter
    private String name;

    public String getName() {
        if(name != null){
            name = WordUtils.capitalize(name.toLowerCase());
        }
        return name;
    }
}
