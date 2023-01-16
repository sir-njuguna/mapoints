package mapoints.lib.view;

import mapoints.lib.model.BaseModel;

import java.util.Date;

public abstract class BaseView<T extends BaseModel> {
    protected T entity;

    public BaseView(T entity) {
        this.entity = entity;
    }

    public Date getTimeCreated(){
        return entity.getTimeCreated();
    }
}
