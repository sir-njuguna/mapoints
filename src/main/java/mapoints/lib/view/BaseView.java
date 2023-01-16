package mapoints.lib.view;

import mapoints.lib.model.BaseModel;

public abstract class BaseView<T extends BaseModel> {
    protected T entity;

    public BaseView(T entity) {
        this.entity = entity;
    }
}
