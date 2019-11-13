/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.bean;

import com.elixir.commons.cdi.BeanManager;
import com.elixir.commons.crypto.Encoder;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.admin.repository.ActivityLogFacade;
import com.nibss.nipreport.admin.repository.StatusFacade;
import com.nibss.nipreport.admin.repository.UserIdentityFacade;
import com.nibss.nipreport.context.AppConfig;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.EntityResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.model.Flag;
import com.nibss.nipreport.model.StatusFlag;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author oogunjimi
 */
@Path("login")
@Named
@RequestScoped
public class LoginBean {

    @Inject
    private HttpServletRequest request;
    @Inject
    private UserIdentityFacade userIdentityFacade;
    private static final int MAX_LOGIN_ATTEMPS = 0;

    public static LoginBean instance() {
        return BeanManager.INSTANCE.getReference(LoginBean.class);
    }

    private UserIdentityFacade getRepository() {
        return userIdentityFacade;
    }

    @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse logout() {
        DataResponse dataResponse = new DataResponse();
        Object session = request.getSession(false);
        if (session instanceof HttpSession) {
            ((HttpSession) session).invalidate();
            dataResponse.setValid(true);
            dataResponse.addMessage(new Message("Logout Successful, Goodbye!", Message.SEVERITY_INFO));
        } else {
            dataResponse.addMessage(new Message("Logout Failed, Try again!", Message.SEVERITY_WARNING));
        }
        return dataResponse;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse login(@FormParam("email") String email, @FormParam("password") String password) {
        EntityResponse<UserIdentity> dataResponse = authenticate(email, password);
        if (dataResponse.isValid()) {
            this.loginSuccessful(dataResponse.getEntity());
        } else {
            this.loginFailed();
        }
        return dataResponse;
    }

    //***********
    private EntityResponse<UserIdentity> authenticate(String email, String password) {
        UserIdentity user;
        EntityResponse<UserIdentity> entityResponse = new EntityResponse<>();
        try {
            if (email == null || email.isEmpty()) {
                entityResponse.addMessage(new FieldMessage("email", "Email is required", Message.SEVERITY_WARNING));
            }
            if (password == null || password.isEmpty()) {
                entityResponse.addMessage(new FieldMessage("password", "Password is required", Message.SEVERITY_WARNING));
            }
            List<UserIdentity> userIdentityList = entityResponse.getMessages() == null || entityResponse.getMessages().isEmpty() ? getRepository().findByEmail(email) : null;
            if (userIdentityList == null || userIdentityList.isEmpty()) {
                entityResponse.addMessage(new Message("Wrong Combination of Credientials. Login Failed", Message.SEVERITY_WARNING));
                return entityResponse;
            }
            if (userIdentityList.size() > 1) {
                entityResponse.addMessage(new Message("Duplicate Credientials Found.Please contact the Administrator", Message.SEVERITY_WARNING));
                return entityResponse;
            }
            user = userIdentityList.get(0);

            if (user instanceof UserIdentity) {
                boolean auth = (user.getPasswordHash() != null && user.getPasswordHash().equals(
                        new Encoder().encode(password, user.getUserId().toString())));
                if (auth) {
                    if (null != user.getFlag()) {
                        switch (user.getFlag()) {
                            case Flag.ENABLED:
                                break;
                            case Flag.LOCKED:
                                entityResponse.addMessage(new Message("Your Account has been Locked due to inactivity. Please contact the Administrator", Message.SEVERITY_WARNING));
                                break;
                            case Flag.INACTIVE:
//                                if (isDormantUser(user)) {
//                                    entityData.addMessage(new Message("User Profile is Dormant. Please contact the Administrator", Message.SEVERITY_WARN));
//                                }
                                break;
                            default:
                                entityResponse.addMessage(new Message("Your Account is Disabled. Please contact the Administrator", Message.SEVERITY_WARNING));
                                break;
                        }
                    }
                    if (entityResponse.getMessages() == null || entityResponse.getMessages().isEmpty()) {
                        entityResponse.setValid(true);
                        entityResponse.setEntity(user);
                    } else {
                        entityResponse.setValid(false);
                    }
                } else {
                    entityResponse.addMessage(new Message("Login Failed", Message.SEVERITY_WARNING));
                    entityResponse.setValid(false);
                    if (MAX_LOGIN_ATTEMPS > 0) {
                        if (user.getAccessCount() < 0) {
                            user.setAccessCount(0);
                        }
                        user.setAccessCount(user.getAccessCount() + 1);
                        if (user.getAccessCount() >= MAX_LOGIN_ATTEMPS) {
                            user.setFlag(Flag.LOCKED);
                        }
                        getRepository().save(user);
                        String loginFailedMsg = null;
                        if (user.getAccessCount() < MAX_LOGIN_ATTEMPS) {
                            int attempMore = MAX_LOGIN_ATTEMPS - user.getAccessCount();
                            loginFailedMsg = attempMore + (attempMore > 1 ? " More Attempts" : " More Attempt");
                        } else if (Flag.LOCKED.equals(user.getFlag())) {
                            loginFailedMsg = "This Account has been Locked. Please contact the Administrator";
                        }
                        if (loginFailedMsg != null) {
                            entityResponse.addMessage(new Message(loginFailedMsg, Message.SEVERITY_WARNING));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            AppUtil.log(ex);
        }
        return entityResponse;
    }

    private void loginSuccessful(UserIdentity loggedInUserIdentity) {
        Date lastAccessedDate = loggedInUserIdentity.getLastAccessedDate();
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConstant.LAST_ACCESSED_DATE_PARAM, lastAccessedDate == null ? new Date() : lastAccessedDate);
        session.setAttribute(AppConstant.REMOTE_IP_PARAM, request instanceof HttpServletRequest ? ((HttpServletRequest) request).getRemoteAddr() : null);
//        Settings settings = SettingsBean.instance().getEntity();
//        Config config = Config.instance();
//        Integer sessionTimeout = (settings != null && settings.getInactivityTimeout() != null) ? settings.getInactivityTimeout() : config.getSessionTimeout();
//        if (sessionTimeout != null && sessionTimeout > 0) {
//            ((HttpSession) session).setMaxInactiveInterval(sessionTimeout * 60);
//        }
        AppConfig config = AppConfig.instance();
        Integer sessionTimeout = config.getSessionTimeout();
        if (sessionTimeout != null && sessionTimeout > 0) {
            ((HttpSession) session).setMaxInactiveInterval(sessionTimeout * 60);
        }
        loggedInUserIdentity.setLastAccessedDate(new Date());
        loggedInUserIdentity.setAccessCount(0);
        loggedInUserIdentity = getRepository().save(loggedInUserIdentity);
        session.setAttribute(AppConstant.USER_PARAM, loggedInUserIdentity);
        config.getUserRegistry().register(loggedInUserIdentity);
        try {
            ActivityLog activityLog = createActivityLog(loggedInUserIdentity);
            ActivityLogFacade.instance().save(activityLog, StatusFlag.LOGIN, StatusFlag.ACTIVITY_TYPE);
        } catch (Exception ex) {
             AppUtil.log(ex);
        }
    }

    private ActivityLog createActivityLog(UserIdentity e) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEventDate(new Date());
        activityLog.setEntityID(e.getEntityId() != null ? e.getEntityId().toString() : null);
        activityLog.setEntityClass(e.getClass().getName());
        Object remoteIP = request.getSession().getAttribute(AppConstant.REMOTE_IP_PARAM);
        activityLog.setEventIp(remoteIP != null ? remoteIP.toString() : null);
        activityLog.setEventBy(e);
        activityLog.setBranch(e.getBranch());
        activityLog.setEventStatus(StatusFacade.instance().findByCode(StatusFlag.LOGIN));
        return activityLog;
    }

    private void loginFailed() {

    }

    private boolean isDormantUser(UserIdentity ui) {
        try {
            Date date = ui.getLastAccessedDate();
            if (date == null) {
                return false;
            }
            if (ui.getLastPasswordChangeDate() != null) {
                date = date.after(ui.getLastPasswordChangeDate()) ? date : ui.getLastPasswordChangeDate();
            }
            AppConfig config = AppConfig.instance();
            Integer x = config != null ? config.getInactivityPeriod() : null;
            if (x != null) {
                return false;
            }
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, x);
            return (c.before(new Date()));
        } catch (Exception e) {
        }
        return false;
    }

}
