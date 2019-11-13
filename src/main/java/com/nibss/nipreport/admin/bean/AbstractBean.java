/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import com.elixir.commons.cdi.BeanManager;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.model.AdminResource;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.BankFacade;
import com.nibss.nipreport.admin.repository.BranchFacade;
import com.nibss.nipreport.admin.repository.InstitutionFacade;
import com.nibss.nipreport.admin.repository.StatusFacade;
import com.nibss.nipreport.annotation.LoggedIn;
import com.nibss.nipreport.bean.NavigatorBean;
import com.nibss.nipreport.bean.ProfileBean;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.data.NameValuePair;
import com.nibss.nipreport.model.Entity;
import com.nibss.nipreport.model.Flag;
import com.nibss.nipreport.model.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
public abstract class AbstractBean<T extends Entity> implements AdminResource, Serializable {

    @Inject
    @LoggedIn
    private UserIdentity loggedInUser;
    @Inject
    private HttpServletRequest request;
    private String pathName;
    private String resource;
    private String action;
    protected T entity;
    protected final View DEFAULT_VIEW = new View(AppConstant.MAIN_PAGE);

    public AbstractBean() {
    }

    public AbstractBean(String pathName) {
        this.pathName = pathName;
    }

    public static <F> F instance(Class<F> clazz) {
        return BeanManager.INSTANCE.getReference(clazz);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    protected UserIdentity getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public abstract AbstractFacade<T> getRepository();

    //***********
    @GET
    @Produces({MediaType.TEXT_HTML})
    public View view() {
        return view(null, null);
    }

    public abstract View view(String resouceName, Long entityId);

    //***********
    protected String resolvePath(String moduleName, String contentPath) {
        return "/WEB-INF/views/" + (moduleName != null ? moduleName + "/" : "") + (pathName != null ? pathName + "/" : "") + contentPath + ".jspx";
    }

    protected View renderView(String contentPath) {
        return renderView(null, contentPath);
    }

    protected View renderView(Long entityId, String contentPath) {
        return renderView(entityId, contentPath, contentPath);
    }

    protected View renderView(Long entityId, String contentPath, String roleName) {
        return renderView(entityId, Resource.ADMIN, contentPath, roleName);
    }

    protected View renderView(Long entityId, String moduleName, String contentPath, String roleName) {
        if (!isRoleAllowed(roleName == null ? contentPath : roleName)) {
            View errorView = ProfileBean.instance().errorView(Response.Status.UNAUTHORIZED.getStatusCode());
//            if (AppUtil.isAjaxRequest(request)){
//                throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).entity(errorView).type(MediaType.TEXT_HTML_TYPE));
//            }
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).entity(errorView).type(MediaType.TEXT_HTML_TYPE).build());
        }
        if (entityId != null) {
            AbstractFacade<T> repository = getRepository();
            if (repository != null) {
                T _entity = repository.find(entityId);
                if (_entity == null) {
                    return null;
                }
                setEntity(_entity);
            }
        }
        if (AppUtil.isAjaxRequest(request)) {
            return new View(resolvePath(moduleName, contentPath), this.getEntity(), "entity");
        }
        NavigatorBean navBean = NavigatorBean.instance();
        navBean.setModuleName(moduleName);
        navBean.setContentPath(resolvePath(moduleName, contentPath));
        return DEFAULT_VIEW;
    }

    protected boolean isRoleAllowed(String roleName) {
        UserIdentity user = this.getLoggedInUser();
        return (user != null && user.getRoleNames() != null && user.getRoleNames().contains(roleName));
    }

    //**********
    public boolean isNibss() {
        try {
            return getLoggedInUser().getBranch().getInstitution().getInstitutionType().getInstitutionTypeId().equals(Flag.NIBSS_INSTITUTION_TYPE);
        } catch (Exception e) {
            log(e);
        }
        return false;
    }

    protected ActivityLog createActivityLog(T e) {
        return createActivityLog(e, -1, null);
    }

    protected ActivityLog createActivityLog(T e, int status) {
        return createActivityLog(e, status, null);
    }

    protected ActivityLog createActivityLog(T e, int status, String type) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEventDate(new Date());
        activityLog.setEntityID(e.getEntityId() != null ? e.getEntityId().toString() : null);
        activityLog.setEntityClass(e.getClass().getName());
        activityLog.setEventIp(getRemoteIP());
        UserIdentity user = getLoggedInUser();
        if (user != null) {
            activityLog.setEventBy(user);
            activityLog.setBranch(user.getBranch());
        }
        activityLog.setEventStatus(type == null ? (status > -1 ? StatusFacade.instance().findByCode(status) : null)
                : (status > -1 ? StatusFacade.instance().findByCodeAndType(status, type) : null));
        return activityLog;
    }

    private String getRemoteIP() {
        return request != null ? request.getRemoteAddr() : null;
    }

    protected void log(String msg) {
        AppUtil.log(msg);
    }

    protected void log(Throwable e) {
        AppUtil.log(e);
    }

    //**********
    @GET
    @Path("institutions")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getInstitutions() {
        return getEntities(InstitutionFacade.instance(), false, "institutionId", "institutionName");
    }

    @GET
    @Path("banks")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getBanks() {
        return getEntities(BankFacade.instance(), false, "institutionId", "institutionName");
    }

    @GET
    @Path("branches")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getBranches(@QueryParam("institutionId") Long institutionId) {

        if (institutionId == -1) {
//            UserIdentity loggedInUser = this.getLoggedInUser();
//            if (loggedInUser != null) {
//                institutionId = loggedInUser.getBranch().getInstitution().getInstitutionId();
//            }
        }
        DataResponse dataResponse = new DataResponse();
        try {
            List<Object[]> data = BranchFacade.instance().findByInstitutionId(institutionId);
            dataResponse.setData(toNameValuePair(data));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Fetching Records did not perform as intended. Please refresh", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected DataResponse getEntities(AbstractFacade repository, Boolean enabled, String... fields) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Object[]> data = repository.findAll(enabled != null ? enabled : false, fields);
            dataResponse.setData(toNameValuePair(data));
            dataResponse.setValid(true);
        } catch (Exception e) {
            e.printStackTrace();
            dataResponse.addMessage(new Message("Fetching Records did not perform as intended. Please refresh", e.getMessage(), Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected List<NameValuePair> toNameValuePair(List<Object[]> data) {
        if (data == null) {
            return null;
        }
        List<NameValuePair> list = new ArrayList<>();
        for (Object[] ob : data) {
            NameValuePair nvp = new NameValuePair();
            for (int x = 0; x < (ob == null ? 0 : ob.length); x++) {
                String value = ob[x] != null ? ob[x].toString() : null;
                if (x == 0) {
                    nvp.setValue(value);
                } else {
                    nvp.setLabel(nvp.getLabel() != null ? nvp.getLabel() + '-' + value : value);
                }
            }
            list.add(nvp);
        }
        return list;
    }

    protected List<Message> validate(T entity) {
        List<Message> messages = new ArrayList<>();
        if (entity instanceof Flag) {
            if (((Flag) entity).getFlag() == null) {
                messages.add(new FieldMessage("flag", "Status is required", Message.SEVERITY_ERROR));
            }
        }
        return messages;
    }
}
