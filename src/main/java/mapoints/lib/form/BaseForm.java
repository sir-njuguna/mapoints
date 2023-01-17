package mapoints.lib.form;

import javax.validation.constraints.NotNull;

public class BaseForm {
    @NotNull(message = "error.empty-id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
