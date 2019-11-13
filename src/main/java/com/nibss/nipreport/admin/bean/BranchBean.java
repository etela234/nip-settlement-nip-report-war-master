/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.entity.Branch;
import com.nibss.nipreport.model.AdminResource;
import static com.nibss.nipreport.model.AdminResource.CREATE_BRANCH;
import static com.nibss.nipreport.model.AdminResource.EDIT_BRANCH;
import static com.nibss.nipreport.model.AdminResource.VIEW_BRANCH;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.BranchFacade;
import com.nibss.nipreport.admin.repository.InstitutionFacade;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.model.Flag;
import static com.nibss.nipreport.model.Resource.CREATE;
import static com.nibss.nipreport.model.Resource.EDIT;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import java.util.Date;
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
@Path(AdminResource.BRANCH)
@Named
@RequestScoped
public class BranchBean extends AbstractBean<Branch> {

    @Inject
    private BranchFacade branchFacade;

    public BranchBean() {
        super(AdminResource.BRANCH);
    }

    public static BranchBean instance() {
        return instance(BranchBean.class);
    }

    @Override
    public AbstractFacade<Branch> getRepository() {
        return branchFacade;
    }

    @RolesAllowed({LIST_BRANCH, CREATE_BRANCH, EDIT_BRANCH})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    @Override
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("branchId") Long branchId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_BRANCH;
                roleName = LIST_BRANCH;
                break;
            }
            case EDIT: {
                resourceName = EDIT_BRANCH;
                break;
            }
            case CREATE: {
                resourceName = CREATE_BRANCH;
                Branch branch = new Branch();
                //branch.setInstitution(InstitutionTypeFacade.instance().find(Flag.BRANCH_INSTITUTION_TYPE));
                setEntity(branch);
                break;
            }
            default: {
                resourceName = LIST_BRANCH;
                break;
            }
        }
        return renderView(branchId, resourceName, roleName);
    }

    @RolesAllowed({CREATE_BRANCH})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid Branch branch) {
        DataResponse dataResponse = new DataResponse();
        try {
            branch.setEnabled(true);
            List<Message> msgs = validate(branch);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                branch.setCreatedDate(new Date());
                getRepository().create(branch, createActivityLog(branch));
                dataResponse.addMessage(new Message("Branch created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Branch creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({EDIT_BRANCH})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid Branch branch) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(branch);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                branch = sanitize(branch.getBranchId(), branch);
                getRepository().edit(branch, createActivityLog(branch));
                dataResponse.addMessage(new Message("Branch modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Branch modification failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({DELETE_BRANCH})
    @DELETE
    @Path("delete.action")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse delete(@QueryParam("branchId") Long branchId) {
        DataResponse dataResponse = new DataResponse();
        try {
            Branch branch = sanitize(branchId, null);
            branch.setFlag(Flag.DELETED);
            getRepository().delete(branch, createActivityLog(branch));
            dataResponse.addMessage(new Message("Branch deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("Branch deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected Branch sanitize(Long branchId, Branch _branch) {
        if (branchId != null) {
            Branch branch = getRepository().find(branchId);
            if (branch != null) {
                if (_branch != null) {
                    branch.setBranchCode(_branch.getBranchCode());
                    branch.setBranchName(_branch.getBranchName());
                    branch.setLocation(_branch.getLocation());
                    branch.setFlag(_branch.getFlag());
                }
                return branch;
            }
        }
        throw new IllegalArgumentException("Branch identity could not be found.");
    }

    @Override
    protected List<Message> validate(Branch branch) {
        if (branch == null) {
            return null;
        }
        List<Message> messages = super.validate(branch);
        if (messages.isEmpty()) {
            if (branch.getInstitutionId() != null) {
                branch.setInstitution(getEntity() != null && getEntity().getInstitution() != null && branch.getInstitutionId().equals(getEntity().getInstitution().getInstitutionId()) ? getEntity().getInstitution() : InstitutionFacade.instance().find(branch.getInstitutionId()));
            }
            if (branch.getInstitution() == null) {
                messages.add(new FieldMessage("institutionId", "Institution is required", Message.SEVERITY_ERROR));
            }
        }
        return messages;
    }
}
