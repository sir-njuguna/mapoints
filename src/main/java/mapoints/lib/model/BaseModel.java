package mapoints.lib.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@TypeDef(name = "json", typeClass = JsonStringType.class)
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @Getter
    @Setter
    private Long id;


    @Column(unique = true)
    @Getter
    @Setter
    private String entityId;

    @Column(updatable = false)
    @Getter
    @Setter
    private Date timeCreated = new Date();

    @Column(updatable = false)
    @Getter
    @Setter
    private Long timeCreatedInMilliSeconds = new Date().getTime();

    @Getter
    @Setter
    private Date timeLastUpdated = new Date();

    public BaseModel(){
        generateEntityId();
    }

    private void generateEntityId(){
        if(getEntityId() == null){
            String entityId = getClass().getSimpleName().toLowerCase()
                    + "-"
                    + UUID.randomUUID()
                    + "-"
                    + RandomStringUtils.randomAlphanumeric(7).toLowerCase();

            setEntityId(entityId);
        }
    }
}
