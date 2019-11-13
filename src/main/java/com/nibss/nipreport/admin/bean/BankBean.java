/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;

import com.nibss.nipreport.admin.entity.Bank;
import com.nibss.nipreport.model.AdminResource;
import static com.nibss.nipreport.model.AdminResource.CREATE_BANK;
import static com.nibss.nipreport.model.AdminResource.EDIT_BANK;
import static com.nibss.nipreport.model.AdminResource.VIEW_BANK;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.BankFacade;
import com.nibss.nipreport.admin.repository.InstitutionTypeFacade;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.model.Flag;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 * REST Web Service
 *
 * @author oogunjimi
 */
@RolesAllowed({AdminResource.VIEW_BANK, AdminResource.CREATE_BANK, AdminResource.EDIT_BANK})
@Path(AdminResource.BANK)
@Named
@RequestScoped
public class BankBean extends AbstractBean<Bank> {

    @Inject
    private BankFacade bankFacade;

    public BankBean() {
        super(AdminResource.BANK);
    }

    public static BankBean instance() {
        return instance(BankBean.class);
    }

    @Override
    public AbstractFacade<Bank> getRepository() {
        return bankFacade;
    }

    @RolesAllowed({LIST_INSTITUTION, CREATE_INSTITUTION, EDIT_INSTITUTION})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("institutionId") Long institutionId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_BANK;
                roleName = LIST_INSTITUTION;
                break;
            }
            case EDIT: {
                resourceName = EDIT_BANK;
                roleName = EDIT_INSTITUTION;
                break;
            }
            case CREATE: {
                resourceName = CREATE_BANK;
                roleName = CREATE_INSTITUTION;
                Bank bank = new Bank();
                bank.setInstitutionType(InstitutionTypeFacade.instance().find(Flag.BANK_INSTITUTION_TYPE));
                setEntity(bank);
                break;
            }
            default: {
                resourceName = LIST_INSTITUTION;
                break;
            }
        }
        View renderView = renderView(institutionId, resourceName, roleName);
        InstitutionBean.instance().setEntity(this.getEntity());
        return renderView;
    }

    @RolesAllowed({CREATE_INSTITUTION})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid Bank bank) {
        DataResponse dataResponse = new DataResponse();
        try {
            bank.setEnabled(true);
            List<Message> msgs = validate(bank);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                getRepository().create(bank, createActivityLog(bank));
                dataResponse.addMessage(new Message("Bank created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Bank creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({EDIT_INSTITUTION})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid Bank bank) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(bank);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                bank = sanitize(bank.getInstitutionId(), bank);
                getRepository().edit(bank, createActivityLog(bank));
                dataResponse.addMessage(new Message("Bank modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Bank modification failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({DELETE_INSTITUTION})
    @DELETE
    @Path("delete.action")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse delete(@QueryParam("institutionId") Long institutionId) {
        DataResponse dataResponse = new DataResponse();
        try {
            Bank bank = sanitize(institutionId, null);
            bank.setFlag(Flag.DELETED);
            getRepository().delete(bank, createActivityLog(bank));
            dataResponse.addMessage(new Message("Bank deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Bank deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected Bank sanitize(Long institutionId, Bank _bank) {
        if (institutionId != null) {
            Bank bank = getRepository().find(institutionId);
            if (bank != null) {
                if (_bank != null) {
                    bank.setInstitutionCode(_bank.getInstitutionCode());
                    bank.setInstitutionName(_bank.getInstitutionName());
                    bank.setFlag(_bank.getFlag());
                }
                return bank;
            }
        }
        throw new IllegalArgumentException("Bank identity could not be found.");
    }

    @Override
    protected List<Message> validate(Bank bank) {
        if (bank == null) {
            return null;
        }
//        if (bank.getBankCode() != null && bank.getBankCode().isEmpty()) {
//            bank.setBankCode(null);
//        }
        List<Message> messages = super.validate(bank);
        if (messages.isEmpty()) { 
            if (bank.getInstitutionTypeId() != null && bank.getInstitutionTypeId().equals(Flag.BANK_INSTITUTION_TYPE)) {
                bank.setInstitutionType(getEntity() != null && getEntity().getInstitutionType() != null && bank.getInstitutionTypeId().equals(getEntity().getInstitutionType().getInstitutionTypeId()) ? getEntity().getInstitutionType() : InstitutionTypeFacade.instance().find(bank.getInstitutionTypeId()));
            } else {
                bank.setInstitutionType(null);
            }
            if (bank.getInstitutionType() == null) {
                messages.add(new FieldMessage("institutionTypeId", "Institution Type could not be determined", Message.SEVERITY_ERROR));
            }
        }
        return messages;
    }

}
