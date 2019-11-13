/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.admin.repository.UserIdentityFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.AdminResource.LIST_USER;
import static com.nibss.nipreport.model.AdminResource.USER;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

/**
 *
 * @author oogunjimi
 */
@RolesAllowed(LIST_USER)
@Path(USER + "/datatable")
@Named
@SessionScoped
public class UserTableModel extends AbstractTableModel<UserIdentity> {

    @Inject
    private UserIdentityFacade userIdentityFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"branch.institution.institutionName", "branch.branchName", "userRole.userRoleName", "firstName", "lastName", "email", "phoneNo", "flag"};
        super.init(filterNames, null);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<UserIdentity> data = userIdentityFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Institution", "Branch", "User Role", "First Name", "Last Name", "Email", "Phone Number", "Status"};
            String rows[] = {"branch.institution.institutionName", "branch.branchName", "userRole.userRoleName", "firstName", "lastName", "email", "phoneNo", "flagName"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, rows) : toExcel(data, cols, rows);
        }
        return null;
    }

    @Override
    protected List<UserIdentity> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(userIdentityFacade.count(filterMap));
        return userIdentityFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("branch.branchId", this.getLoggedInUser().getBranch().getBranchId());
        }
        return filterMap;
    }
}
