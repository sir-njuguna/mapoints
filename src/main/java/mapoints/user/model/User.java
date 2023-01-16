package mapoints.user.model;

import lombok.Getter;
import lombok.Setter;
import mapoints.lib.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"userType", "phoneNumber"})
})
public class User extends BaseModel {
    @Column(length = 100)
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private UserType userType;


    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String recentAuthId;
}
