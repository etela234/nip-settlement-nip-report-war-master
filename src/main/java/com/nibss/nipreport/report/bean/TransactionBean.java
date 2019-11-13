/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.model.ReportResource;
import com.nibss.nipreport.report.entity.Transaction;
import com.nibss.nipreport.report.repository.TransactionFacade;
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
@Path(ReportResource.TRANSACTION)
@Named
@RequestScoped
public class TransactionBean extends AbstractBean<Transaction> {

    @Inject
    private TransactionFacade transactionFacade;

    public TransactionBean() {
        super(ReportResource.TRANSACTION);
    }

    public static TransactionBean instance() {
        return instance(TransactionBean.class);
    }

    @Override
    public AbstractFacade<Transaction> getRepository() {
        return transactionFacade;
    }

    @RolesAllowed({LIST_TRANSACTION})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    @Override
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("transactionId") Long transactionId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_TRANSACTION;
                roleName = LIST_TRANSACTION;
                break;
            }
            default: {
                resourceName = LIST_TRANSACTION;
                break;
            }
        }
        return renderView(transactionId, resourceName, roleName);
    }
}
