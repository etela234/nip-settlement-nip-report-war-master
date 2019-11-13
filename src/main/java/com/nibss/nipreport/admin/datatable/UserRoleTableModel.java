/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.UserRole;
import com.nibss.nipreport.admin.repository.UserRoleFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.AdminResource.LIST_USER_ROLE;
import static com.nibss.nipreport.model.AdminResource.USER_ROLE;
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
@RolesAllowed(LIST_USER_ROLE)
@Path(USER_ROLE + "/datatable")
@Named
@SessionScoped
public class UserRoleTableModel extends AbstractTableModel<UserRole> {

    @Inject
    private UserRoleFacade userRoleFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"institutionType.institutionTypeName", "userRoleName", "flag"};
        String filterIdNames[] = {"institutionType"};
        super.init(filterNames, filterIdNames);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<UserRole> data = userRoleFacade.search(sortOrderMap, filterMap);
        if (data.size() > 0) {
            String[] cols = {"Institution Type", "Role Name", "Status"};
            String rows[] = {"institutionType.institutionTypeName", "userRoleName", "flagName"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, rows) : toExcel(data, cols, rows);
        }
        return null;
    }

    @Override
    protected List<UserRole> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(userRoleFacade.count(filterMap));
        return userRoleFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("institutionType.institutionTypeId", this.getLoggedInUser().getBranch().getInstitution().getInstitutionType().getInstitutionTypeId());
        }
        return filterMap;
    }
}
