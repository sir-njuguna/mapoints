package mapoints.user.view;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.view.BaseView;
import mapoints.user.model.User;

public class AuthUserView extends BaseView<User> {

    @Getter
    @Setter
    private String authToken;

    public AuthUserView(User entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }

    public String getPhoneNumber(){
        return entity.getPhoneNumber();
    }
}
