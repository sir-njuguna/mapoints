package mapoints.lib.service;

import mapoints.lib.model.BaseModel;
import mapoints.lib.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract  class BaseService<T extends BaseModel, R extends BaseRepository<T>>{

    protected R repository;

    public T save(T model){
        model.setTimeLastUpdated(new Date());
        return repository.save(model);
    }

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }
}
