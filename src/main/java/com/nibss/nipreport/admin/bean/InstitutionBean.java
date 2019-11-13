/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import com.nibss.nipreport.admin.entity.Institution;
import com.nibss.nipreport.model.AdminResource;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.InstitutionFacade;
import com.nibss.nipreport.admin.repository.InstitutionTypeFacade;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import static com.nibss.nipreport.model.AdminResource.CREATE_INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.DELETE_INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.EDIT_INSTITUTION;
import static com.nibss.nipreport.model.AdminResource.VIEW_INSTITUTION;
import com.nibss.nipreport.model.Flag;
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
 * @param <T>
 */
@Path(AdminResource.INSTITUTION)
@Named
@RequestScoped
public class InstitutionBean extends AbstractBean<Institution> {

    @Inject
    private InstitutionFacade institutionFacade;

    public InstitutionBean() {
        super(AdminResource.INSTITUTION);
    }

    public static InstitutionBean instance() {
        return instance(InstitutionBean.class);
    }

    @Override
    public AbstractFacade<Institution> getRepository() {
        return institutionFacade;
    }

    @RolesAllowed({LIST_INSTITUTION, CREATE_INSTITUTION, EDIT_INSTITUTION})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("institutionId") Long institutionId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case LIST: {
                resourceName = LIST_INSTITUTION;
                break;
            }
            case VIEW: {
                resourceName = VIEW_INSTITUTION;
                roleName = LIST_INSTITUTION;
                break;
            }
            case EDIT: {
                resourceName = EDIT_INSTITUTION;
                break;
            }
            case CREATE: {
                resourceName = CREATE_INSTITUTION;
                Institution institution = new Institution();
                institution.setInstitutionType(InstitutionTypeFacade.instance().find(Flag.NIBSS_INSTITUTION_TYPE));
                setEntity(institution);
                break;
            }
            default: {
                resourceName = LIST_INSTITUTION;
                break;
            }
        }
        return renderView(institutionId, resourceName, roleName);
    }

    @RolesAllowed({CREATE_INSTITUTION})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid Institution institution) {
        DataResponse dataResponse = new DataResponse();
        try {
            institution.setEnabled(true);
            List<Message> msgs = validate(institution);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                getRepository().create(institution, createActivityLog(institution));
                dataResponse.addMessage(new Message("Institution created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Institution creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({EDIT_INSTITUTION})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid Institution institution) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(institution);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                institution = sanitize(institution.getInstitutionId(), institution);
                getRepository().edit(institution, createActivityLog(institution));
                dataResponse.addMessage(new Message("Institution modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Institution modification failed!", Message.SEVERITY_ERROR));
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
            Institution institution = sanitize(institutionId, null);
            institution.setFlag(Flag.DELETED);
            getRepository().delete(institution, createActivityLog(institution));
            dataResponse.addMessage(new Message("Institution deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Institution deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected Institution sanitize(Long institutionId, Institution _institution) {
        if (institutionId != null) {
            Institution institution = getRepository().find(institutionId);
            if (institution != null) {
                if (_institution != null) {
                    institution.setInstitutionCode(_institution.getInstitutionCode());
                    institution.setInstitutionName(_institution.getInstitutionName());
                    institution.setFlag(_institution.getFlag());
                }
                return institution;
            }
        }
        throw new IllegalArgumentException("Institution identity could not be found.");
    }

    @Override
    protected List<Message> validate(Institution institution) {
        if (institution == null) {
            return null;
        }

        List<Message> messages = super.validate(institution);
        if (messages.isEmpty()) {
            if (institution.getInstitutionTypeId() != null && institution.getInstitutionTypeId().equals(Flag.NIBSS_INSTITUTION_TYPE)) {
                institution.setInstitutionType(getEntity() != null && getEntity().getInstitutionType() != null && institution.getInstitutionTypeId().equals(getEntity().getInstitutionType().getInstitutionTypeId()) ? getEntity().getInstitutionType() : InstitutionTypeFacade.instance().find(institution.getInstitutionTypeId()));
            } else {
                institution.setInstitutionType(null);
            }
            if (institution.getInstitutionType() == null) {
                messages.add(new FieldMessage("institutionTypeId", "Institution Type could not be determined", Message.SEVERITY_ERROR));
            }
        }
        return messages;
    }

}
