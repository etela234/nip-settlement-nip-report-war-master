/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.repository;

import com.nibss.nipreport.admin.entity.ActivityLog;
import javax.ejb.Stateless;

/**
 *
 * @author oogunjimi
 */
@Stateless
public class ActivityLogFacade extends AbstractFacade<ActivityLog> {

    public ActivityLogFacade() {
        super(ActivityLog.class);
    }

    public static ActivityLogFacade instance() {
        return AbstractFacade.instance(ActivityLogFacade.class);
    }

    public void save(ActivityLog log, int statusCode, String statusType) {
        log.setEventStatus(statusType == null
                ? StatusFacade.instance().findByCode(statusCode)
                : StatusFacade.instance().findByCodeAndType(statusCode, statusType));
        getEntityManager().merge(log);
    }
}
