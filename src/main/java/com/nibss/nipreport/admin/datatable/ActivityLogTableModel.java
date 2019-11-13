/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.ActivityLog;
import com.nibss.nipreport.admin.repository.ActivityLogFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.AdminResource.ACTIVITY_LOG;
import static com.nibss.nipreport.model.AdminResource.LIST_ACTIVITY_LOG;
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
@RolesAllowed(LIST_ACTIVITY_LOG)
@Path(ACTIVITY_LOG + "/datatable")
@Named
@SessionScoped
public class ActivityLogTableModel extends AbstractTableModel<ActivityLog> {

    @Inject
    private ActivityLogFacade activityLogFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"branch.institution.institutionName", "branch.branchName", "eventBy.email", "eventStatus.description", "eventIp", "eventDate"};
        super.init(filterNames, null);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Object[]> data = activityLogFacade.search(sortOrderMap, filterMap, getFilterNames());
        if (data.size() > 0) {
            String[] cols = {"Institution", "Branch", "Event By", "Description", "Event IP", "Event Status"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, null) : toExcel(data, cols, null);
        }
        return null;
    }

    @Override
    protected List<ActivityLog> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(activityLogFacade.count(filterMap));
        return activityLogFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("branch.institution.institutionId", this.getLoggedInUser().getBranch().getInstitution().getInstitutionId());
        }
        return filterMap;
    }

}
