/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.bean;

import com.nibss.nipreport.model.Entity;
import com.nibss.nipreport.model.ReportResource;
import com.nibss.nipreport.model.Resource;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
public abstract class AbstractBean<T extends Entity> extends com.nibss.nipreport.admin.bean.AbstractBean<T> implements ReportResource {

    public AbstractBean() {
    }

    public AbstractBean(String pathName) {
        super(pathName);
    }

    @Override
    public T getEntity() {
        return super.getEntity();
    }

    @Override
    protected View renderView(Long entityId, String contentPath, String roleName) {
        return renderView(entityId, Resource.REPORT, contentPath, roleName);
    }

}
