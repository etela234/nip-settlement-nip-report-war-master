/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@javax.ws.rs.ApplicationPath("core")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addClasses(resources);
        addDefaultClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     *
     * @param resources
     */
    protected void addClasses(Set<Class<?>> resources) {
        resources.add(com.nibss.nipreport.bean.LoginBean.class);
        resources.add(com.nibss.nipreport.bean.ProfileBean.class);
    }

    private void addDefaultClasses(Set<Class<?>> resources) {
        resources.add(com.nibss.nipreport.context.SecurityInterceptor.class);
        resources.add(com.nibss.nipreport.context.ValidationExceptionMapper.class);

    }

}
