/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.bean;

import com.elixir.commons.cdi.BeanManager;
import com.nibss.nipreport.context.AppConstant;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author oogunjimi
 */
@Named
@SessionScoped
public class NavigatorBean implements Serializable {

    @Inject
    private HttpServletRequest request;
    private boolean navCollapsed = false;
    private String moduleName;

    public static NavigatorBean instance() {
        return BeanManager.INSTANCE.getReference(NavigatorBean.class);
    }

    public String getContentPath() {
        Object contentPath = request.getAttribute(AppConstant.CONTENT_PATH_PARAM);
        if (!(contentPath instanceof String) || contentPath.toString().isEmpty()) {
            return AppConstant.HOME_PAGE;
        }
        return contentPath.toString();
    }

    public void setContentPath(String contentPath) {
        request.setAttribute(AppConstant.CONTENT_PATH_PARAM, contentPath);
        if (contentPath == null) {
            setModuleName(null);
        }
    }

    public boolean isNavCollapsed() {
        return navCollapsed;
    }

    public void setNavCollapsed(boolean navCollapsed) {
        this.navCollapsed = navCollapsed;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

}
