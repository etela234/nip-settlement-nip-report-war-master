/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.elixir.commons.web.context.UserRegistry;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.admin.repository.ActivityLogFacade;
import com.nibss.nipreport.admin.repository.StatusFacade;
import com.nibss.nipreport.model.StatusFlag;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author oogunjimi
 */
@WebListener
public class AppContextListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            AppConfig.instance().init(sce.getServletContext());
        } catch (IOException ex) {
           AppUtil.log(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        UserRegistry ur = AppConfig.instance().getUserRegistry();
        Object u = se.getSession().getAttribute(AppConstant.USER_PARAM);

        if (u instanceof UserIdentity && ur != null) {
            UserIdentity loggedInUser = (UserIdentity) u;
            ur.unRegister(loggedInUser);
            try {
                ActivityLogFacade activityLogFacade = ActivityLogFacade.instance();
                ActivityLog activityLog = createActivityLog(loggedInUser, se);
                activityLogFacade.save(activityLog, StatusFlag.LOGOUT, StatusFlag.ACTIVITY_TYPE);
            } catch (Exception ex) {
                 AppUtil.log(ex);
            }
        }
    }

    private ActivityLog createActivityLog(UserIdentity e, HttpSessionEvent hse) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEventDate(new Date());
        activityLog.setEntityID(e.getEntityId() != null ? e.getEntityId().toString() : null);
        activityLog.setEntityClass(e.getClass().getName());
        Object remoteIP = hse.getSession().getAttribute(AppConstant.REMOTE_IP_PARAM);
        activityLog.setEventIp(remoteIP != null ? remoteIP.toString() : null);
        activityLog.setEventBy(e);
        activityLog.setBranch(e.getBranch());
        activityLog.setEventStatus(StatusFacade.instance().findByCode(StatusFlag.LOGOUT));
        return activityLog;
    }

}
