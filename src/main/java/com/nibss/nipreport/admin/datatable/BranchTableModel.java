/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.Branch;
import com.nibss.nipreport.admin.repository.BranchFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.AdminResource.BRANCH;
import static com.nibss.nipreport.model.AdminResource.LIST_BRANCH;
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
@RolesAllowed(LIST_BRANCH)
@Path(BRANCH + "/datatable")
@Named
@SessionScoped
public class BranchTableModel extends AbstractTableModel<Branch> {

    @Inject
    private BranchFacade branchFacade;

    @PostConstruct
    protected void init() {
        String filterNames[] = {"institution.institutionName", "branchCode", "branchName", "location", "flag"};
        super.init(filterNames, null);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Branch> data = branchFacade.search(sortOrderMap, filterMap);
        if (data.size() > 0) {
            String[] cols = {"Institution", "Branch Code", "Branch Name", "Location", "Status"};
            String rows[] = {"institution.institutionName", "branchCode", "branchName", "location", "flagName"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, rows) : toExcel(data, cols, rows);
        }
        return null;
    }

    @Override
    protected List<Branch> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(branchFacade.count(filterMap));
        return branchFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("branchId", this.getLoggedInUser().getBranch().getBranchId());
        }
        return filterMap;
    }
}
