/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.report.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.context.AppConfig;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.model.Flag;
import com.nibss.nipreport.model.ReportResource;
import static com.nibss.nipreport.model.ReportResource.LIST_TRANSACTION_REPORT_BATCH;
import static com.nibss.nipreport.model.ReportResource.VIEW_TRANSACTION_REPORT_BATCH;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import com.nibss.nipreport.report.entity.TransactionReportBatch;
import com.nibss.nipreport.report.repository.TransactionReportBatchFacade;
import com.nibss.nipreport.report.repository.TransactionReportTypeFacade;
import com.nibss.nipreport.ws.impl.NipReportWS;
import com.nibss.nipreport.ws.impl.NipReportWS_Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@RolesAllowed({LIST_TRANSACTION_REPORT_BATCH})
@Path(ReportResource.TRANSACTION_REPORT_BATCH)
@Named
@RequestScoped
public class TransactionReportBatchBean extends AbstractBean<TransactionReportBatch> {

    @Inject
    private TransactionReportBatchFacade transactionReportBatchFacade;

    public TransactionReportBatchBean() {
        super(ReportResource.TRANSACTION_REPORT_BATCH);
    }

    public static TransactionReportBatchBean instance() {
        return instance(TransactionReportBatchBean.class);
    }

    @Override
    public AbstractFacade<TransactionReportBatch> getRepository() {
        return transactionReportBatchFacade;
    }

    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    @Override
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case CREATE: {
                resourceName = CREATE_TRANSACTION_REPORT_BATCH;
                roleName = LIST_TRANSACTION_REPORT_BATCH;
                break;
            }
            case VIEW: {
                resourceName = VIEW_TRANSACTION_REPORT_BATCH;
                roleName = LIST_TRANSACTION_REPORT_BATCH;
                break;
            }
            default: {
                resourceName = LIST_TRANSACTION_REPORT_BATCH;
                break;
            }
        }
        return renderView(transactionReportBatchId, resourceName, roleName);
    }

    // @RolesAllowed({CREATE_USER_ROLE})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid TransactionReportBatch transactionReportBatch) {
        DataResponse dataResponse = new DataResponse();
        try {
            transactionReportBatch.setFlag(Flag.NEW);
            transactionReportBatch.setCreatedDate(new Date());
            List<Message> msgs = validate(transactionReportBatch);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                transactionReportBatch.setBatchName(AppUtil.formatAsTimestamp(transactionReportBatch.getFromDate()) + "-" + AppUtil.formatAsTimestamp(transactionReportBatch.getToDate()));
                getRepository().create(transactionReportBatch, createActivityLog(transactionReportBatch));
                dataResponse.addMessage(new Message("Session created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Session creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("close-session")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse closeSession(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            TransactionReportBatch batch = getRepository().find(transactionReportBatchId);
            batch.setFlag(Flag.CLOSED);
            getRepository().edit(batch, createActivityLog(batch));
            dataResponse.addMessage(new Message("Session closed successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Session closing failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("compare-report")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse compareReport(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            //TransactionReportBatch batch = getRepository().find(transactionReportBatchId);
            if (createNipReportWS().compareSettlementReport(transactionReportBatchId)) {
                dataResponse.addMessage(new Message("Report comparison in progress.", Message.SEVERITY_INFO));
                dataResponse.setValid(true);
            } else {
                dataResponse.addMessage(new Message("Report comparison cannot be processed at this time!", Message.SEVERITY_WARNING));
                dataResponse.setValid(false);
            }
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Report comparison failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("pull-exception")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse pullException(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            if (createNipReportWS().pullExcepionLog(transactionReportBatchId)) {
                dataResponse.addMessage(new Message("Pull Exception in progress.", Message.SEVERITY_INFO));
                dataResponse.setValid(true);
            } else {
                dataResponse.addMessage(new Message("Pull Exception cannot be processed at this time!", Message.SEVERITY_WARNING));
                dataResponse.setValid(false);
            }
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Pull Exception failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("generate-settlement-report")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse generateSettlementReport(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            if (createNipReportWS().generateSettlementReport(transactionReportBatchId)) {
                dataResponse.addMessage(new Message("Settlement report generation in progress.", Message.SEVERITY_INFO));
                dataResponse.setValid(true);
            } else {
                dataResponse.addMessage(new Message("Settlement report generation cannot be processed at this time!", Message.SEVERITY_WARNING));
                dataResponse.setValid(false);
            }
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Settlement report generation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("regenerate-settlement-report")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse regenerateSettlementReport(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            if (createNipReportWS().regenerateSettlementReport(transactionReportBatchId)) {
                dataResponse.addMessage(new Message("Settlement report regeneration in progress.", Message.SEVERITY_INFO));
                dataResponse.setValid(true);
            } else {
                dataResponse.addMessage(new Message("Settlement report regeneration cannot be processed at this time!", Message.SEVERITY_WARNING));
                dataResponse.setValid(false);
            }
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Settlement report regeneration failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("generate-billing-report")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse generateBillingReport(@QueryParam("transactionReportBatchId") Long transactionReportBatchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            if (createNipReportWS().generateBillingReport(transactionReportBatchId)) {
                dataResponse.addMessage(new Message("Billing report generation in progress.", Message.SEVERITY_INFO));
                dataResponse.setValid(true);
            } else {
                dataResponse.addMessage(new Message("Billing report generation cannot be processed at this time!", Message.SEVERITY_WARNING));
                dataResponse.setValid(false);
            }
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Billing report generation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    private NipReportWS createNipReportWS() {
        AppConfig config = AppConfig.instance();
        try {
            if (config.getWsPort() == null || config.getWsHostname() == null) {
                return new NipReportWS_Service().getNipReportWSPort();
            } else {
                URL url = new URL("http://" + config.getWsHostname() + ":" + config.getWsPort() + "/nip-report-ws?wsdl");
                return new NipReportWS_Service(url).getNipReportWSPort();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(TransactionReportBatchBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected List<Message> validate(TransactionReportBatch transactionReportBatch) {
        if (transactionReportBatch == null) {
            return null;
        }
        List<Message> messages = super.validate(transactionReportBatch);
        if (transactionReportBatch.getFromDate() == null) {
            messages.add(new FieldMessage("fromDate", "[From Date] value is required", Message.SEVERITY_ERROR));
        }
        if (transactionReportBatch.getToDate() == null) {
            messages.add(new FieldMessage("toDate", "[To Date] value is required", Message.SEVERITY_ERROR));
        }
        if (transactionReportBatch.getTransactionReportTypeCode() == null) {
            messages.add(new FieldMessage("transactionReportTypeCode", "Report Type value is required", Message.SEVERITY_ERROR));
        }
        if (messages.isEmpty()) {
            if (!transactionReportBatch.getToDate().after(transactionReportBatch.getFromDate())) {
                messages.add(new FieldMessage("fromDate", "[From Date] must be a future date with respect to [To Date]", Message.SEVERITY_ERROR));
            }
            if (transactionReportBatch.getTransactionReportTypeCode() != null) {
                transactionReportBatch.setTransactionReportType(TransactionReportTypeFacade.instance().findByReportTypeCode(transactionReportBatch.getTransactionReportTypeCode()));
            } else {
                transactionReportBatch.setTransactionReportType(null);
            }
            if (transactionReportBatch.getTransactionReportType() == null) {
                messages.add(new FieldMessage("transactionReportTypeCode", "Transaction Type value is required", Message.SEVERITY_ERROR));
            }
        }

        return messages;
    }
}
