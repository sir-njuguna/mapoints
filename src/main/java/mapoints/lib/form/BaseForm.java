package mapoints.lib.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class BaseForm {
    @NotNull(message = "error.empty-id")
    @Getter
    @Setter
    private String id;
}
