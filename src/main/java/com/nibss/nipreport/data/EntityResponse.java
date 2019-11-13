/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import com.nibss.nipreport.model.Entity;

/**
 *
 * @author oogunjimi
 */
public class EntityResponse<T extends Entity> extends DataResponse {

    public T entity;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

}
