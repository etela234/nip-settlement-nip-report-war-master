/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.datatable;

import com.elixir.commons.web.datamodel.SortOrder;
import com.nibss.nipreport.admin.entity.Institution;
import com.nibss.nipreport.admin.repository.InstitutionFacade;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.data.Exportable;
import static com.nibss.nipreport.model.AdminResource.INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.LIST_INSTITUTION;
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
@RolesAllowed(LIST_INSTITUTION)
@Path(INSTITUTION + "/datatable")
@Named
@SessionScoped
public class InstitutionTableModel extends AbstractTableModel<Institution> {

    @Inject
    private InstitutionFacade institutionFacade;

    @PostConstruct
    protected void init() {
       String filterNames[] = {"institutionCode", "institutionName", "institutionType.institutionTypeName", "flag"};
        String filterIdNames[] = {"institutionType"};
        super.init(filterNames, filterIdNames);
    }

    @Override
    public Exportable export(String exportType, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) throws IOException {
        List<Institution> data = institutionFacade.search(sortOrderMap, filterMap);
        if (data.size() > 0) {
            String rows[] = {"institutionCode", "institutionName", "institutionType.institutionTypeName", "flagName"};
            String[] cols = {"Institution Code", "Institution Name", "Institution Type", "Status"};
            return AppConstant.CSV_EXPORT_TYPE.equalsIgnoreCase(exportType) ? toCSV(data, cols, rows) : toExcel(data, cols, rows);
        }
        return null;
    }

    @Override
    protected List<Institution> load(int startIndex, int pageSize, Map<String, SortOrder> sortOrderMap, Map<String, Object> filterMap) {
        setRowCount(institutionFacade.count(filterMap));
        return institutionFacade.search(startIndex, pageSize, sortOrderMap, filterMap);
    }

    @Override
    public Map<String, Object> preFilter(Map<String, Object> filterMap) {
        if (!isNibss()) {
            filterMap.put("institutionId", this.getLoggedInUser().getBranch().getInstitution().getInstitutionId());
        }
        return filterMap;
    }
}
