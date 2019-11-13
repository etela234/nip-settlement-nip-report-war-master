/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import com.nibss.nipreport.admin.entity.ThirdParty;
import com.nibss.nipreport.model.AdminResource;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.BankFacade;
import com.nibss.nipreport.admin.repository.InstitutionTypeFacade;
import com.nibss.nipreport.admin.repository.ThirdPartyFacade;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import static com.nibss.nipreport.model.AdminResource.CREATE_INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.EDIT_INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.LIST_INSTITUTION;
import com.nibss.nipreport.model.Flag;
import static com.nibss.nipreport.model.Resource.CREATE;
import static com.nibss.nipreport.model.Resource.EDIT;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path(AdminResource.THIRD_PARTY)
@Named
@RequestScoped
public class ThirdPartyBean extends AbstractBean<ThirdParty> {

    @Inject
    private ThirdPartyFacade thirdPartyFacade;

    public ThirdPartyBean() {
        super(AdminResource.THIRD_PARTY);
    }

    public static ThirdPartyBean instance() {
        return instance(ThirdPartyBean.class);
    }

    @Override
    public AbstractFacade<ThirdParty> getRepository() {
        return thirdPartyFacade;
    }

    @RolesAllowed({LIST_INSTITUTION, CREATE_INSTITUTION, EDIT_INSTITUTION})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("institutionId") Long institutionId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_THIRD_PARTY;
                roleName = LIST_INSTITUTION;
                break;
            }
            case EDIT: {
                resourceName = EDIT_THIRD_PARTY;
                roleName = EDIT_INSTITUTION;
                break;
            }
            case CREATE: {
                resourceName = CREATE_THIRD_PARTY;
                roleName = CREATE_INSTITUTION;
                ThirdParty thirdParty = new ThirdParty();
                thirdParty.setInstitutionType(InstitutionTypeFacade.instance().find(Flag.THIRDPARTY_INSTITUTION_TYPE));
                setEntity(thirdParty);
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

    @RolesAllowed({EDIT_INSTITUTION})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid ThirdParty thirdParty) {
        DataResponse dataResponse = new DataResponse();
        try {
            thirdParty.setEnabled(true);
            List<Message> msgs = validate(thirdParty);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                getRepository().create(thirdParty, createActivityLog(thirdParty));
                dataResponse.addMessage(new Message("Third Party created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Third Party creation failed!", e.getMessage(), Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({CREATE_INSTITUTION})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid ThirdParty thirdParty) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(thirdParty);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                thirdParty = sanitize(thirdParty.getInstitutionId(), thirdParty);
                getRepository().edit(thirdParty, createActivityLog(thirdParty));
                dataResponse.addMessage(new Message("Third Party modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Third Party modification failed!", Message.SEVERITY_ERROR));
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
            ThirdParty thirdParty = sanitize(institutionId, null);
            thirdParty.setFlag(Flag.DELETED);
            getRepository().delete(thirdParty, createActivityLog(thirdParty));
            dataResponse.addMessage(new Message("Third Party deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Third Party deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected ThirdParty sanitize(Long institutionId, ThirdParty _thirdParty) {
        if (institutionId != null) {
            ThirdParty thirdParty = getRepository().find(institutionId);
            if (thirdParty != null) {
                if (_thirdParty != null) {
                    thirdParty.setInstitutionCode(_thirdParty.getInstitutionCode());
                    thirdParty.setInstitutionName(_thirdParty.getInstitutionName());
                    thirdParty.setFlag(_thirdParty.getFlag());
                }
                return thirdParty;
            }
        }
        throw new IllegalArgumentException("Third Party identity could not be found.");
    }

    @Override
    protected List<Message> validate(ThirdParty thirdParty) {
        if (thirdParty == null) {
            return null;
        }
        List<Message> messages = super.validate(thirdParty);
        if (thirdParty.getThirdPartyCode() != null && thirdParty.getThirdPartyCode().isEmpty()) {
            thirdParty.setThirdPartyCode(null);
        }
        if (thirdParty.getThirdPartyType() == null) {
              messages.add(new FieldMessage("thirdPartyType", "ThirdParty Type is required", Message.SEVERITY_ERROR));
        }
        if (messages.isEmpty()) {
            if (thirdParty.getInstitutionTypeId() != null && thirdParty.getInstitutionTypeId().equals(Flag.THIRDPARTY_INSTITUTION_TYPE)) {
                thirdParty.setInstitutionType(getEntity() != null && getEntity().getInstitutionType() != null && thirdParty.getInstitutionTypeId().equals(getEntity().getInstitutionType().getInstitutionTypeId()) ? getEntity().getInstitutionType() : InstitutionTypeFacade.instance().find(thirdParty.getInstitutionTypeId()));
            } else {
                thirdParty.setInstitutionType(null);
            }
            if (thirdParty.getInstitutionType() == null) {
                messages.add(new FieldMessage("institutionTypeId", "Institution Type could not be determined", Message.SEVERITY_ERROR));
            }
        }
        if (messages.isEmpty()) {
            if (thirdParty.getSettlementBankId() != null) {
                thirdParty.setSettlementBank(getEntity() != null && getEntity().getSettlementBank() != null && thirdParty.getSettlementBankId().equals(getEntity().getSettlementBank().getInstitutionId()) ? getEntity().getSettlementBank() : BankFacade.instance().find(thirdParty.getSettlementBankId()));
            } else {
                thirdParty.setSettlementBank(null);
            }
            if (thirdParty.getSettlementBank() == null) {
                messages.add(new FieldMessage("settlementBankId", "Settlement ThirdParty is Required", Message.SEVERITY_ERROR));
            }
        }

        return messages;
    }
}
