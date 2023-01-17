package mapoints.lib.service;

import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.model.BaseModel;
import mapoints.lib.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

public abstract  class BaseService<T extends BaseModel, R extends BaseRepository<T>>{

    protected R repository;

    public T save(T model){
        model.setTimeLastUpdated(new Date());
        return repository.save(model);
    }

    public T findByEntityId(String entityId) {
        Optional<T> opt = repository.findByEntityId(entityId);
        if(opt.isEmpty()){
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "entity.not-found"
            );
        }
        return opt.get();
    }


    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }
}
