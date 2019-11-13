/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.model.AdminResource;
import static com.nibss.nipreport.model.AdminResource.LIST_ACTIVITY_LOG;
import static com.nibss.nipreport.model.AdminResource.VIEW_ACTIVITY_LOG;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.ActivityLogFacade;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
@Path(AdminResource.ACTIVITY_LOG)
@Named
@RequestScoped
public class ActivityLogBean extends AbstractBean<ActivityLog> {

    @Inject
    private ActivityLogFacade activityLogFacade;

    public ActivityLogBean() {
        super(AdminResource.ACTIVITY_LOG);
    }

    public static ActivityLogBean instance() {
        return instance(ActivityLogBean.class);
    }

    @Override
    public AbstractFacade<ActivityLog> getRepository() {
        return activityLogFacade;
    }

    @RolesAllowed({LIST_ACTIVITY_LOG})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("activityLogId") Long activityLogId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_ACTIVITY_LOG;
                roleName = LIST_ACTIVITY_LOG;
                break;
            }
            default: {
                resourceName = LIST_ACTIVITY_LOG;
                break;
            }
        }
        return renderView(activityLogId, resourceName, roleName);
    }
}
