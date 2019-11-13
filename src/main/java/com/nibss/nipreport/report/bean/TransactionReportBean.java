/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.model.ReportResource;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION_REPORT;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION_REPORT_BATCH;
import static com.nibss.nipreport.model.ReportResource.VIEW_TRANSACTION_REPORT;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import com.nibss.nipreport.report.entity.TransactionReport;
import com.nibss.nipreport.report.repository.TransactionReportFacade;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
@Path(ReportResource.TRANSACTION_REPORT)
@Named
@RequestScoped
public class TransactionReportBean extends AbstractBean<TransactionReport> {

    @Inject
    private TransactionReportFacade transactionReportFacade;

    public TransactionReportBean() {
        super(ReportResource.TRANSACTION_REPORT);
    }

    public static TransactionReportBean instance() {
        return instance(TransactionReportBean.class);
    }

    @Override
    public AbstractFacade<TransactionReport> getRepository() {
        return transactionReportFacade;
    }

    @RolesAllowed({LIST_TRANSACTION_REPORT})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    @Override
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("transactionReportId") Long transactionReportId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_TRANSACTION_REPORT;
                roleName = LIST_TRANSACTION_REPORT;
                break;
            }
            default: {
                resourceName = LIST_TRANSACTION_REPORT;
                break;
            }
        }
        return renderView(transactionReportId, resourceName, roleName);
    }

}
