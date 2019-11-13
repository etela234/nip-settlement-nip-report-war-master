/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.nibss.nipreport.model.Resource;
import java.util.Set;

/**
 *
 * @author oogunjimi
 */
@javax.ws.rs.ApplicationPath(Resource.ADMIN)
public class RestAdminModule extends RestApplication {

    protected void addClasses(Set<Class<?>> resources) {
        resources.add(com.nibss.nipreport.admin.bean.InstitutionBean.class);
        resources.add(com.nibss.nipreport.admin.bean.BankBean.class);
        resources.add(com.nibss.nipreport.admin.bean.ThirdPartyBean.class);
        resources.add(com.nibss.nipreport.admin.bean.BranchBean.class);
        resources.add(com.nibss.nipreport.admin.bean.UserRoleBean.class);
        resources.add(com.nibss.nipreport.admin.bean.UserBean.class);
        resources.add(com.nibss.nipreport.admin.bean.ActivityLogBean.class);
        resources.add(com.nibss.nipreport.admin.datatable.InstitutionTableModel.class);
        resources.add(com.nibss.nipreport.admin.datatable.BranchTableModel.class);
        resources.add(com.nibss.nipreport.admin.datatable.UserRoleTableModel.class);
        resources.add(com.nibss.nipreport.admin.datatable.UserTableModel.class);
        resources.add(com.nibss.nipreport.admin.datatable.ActivityLogTableModel.class);
    }

}
