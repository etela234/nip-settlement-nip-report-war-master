/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.entity.Role;
import com.nibss.nipreport.admin.entity.UserRole;
import com.nibss.nipreport.model.AdminResource;
import static com.nibss.nipreport.model.AdminResource.CREATE_USER_ROLE;
import static com.nibss.nipreport.model.AdminResource.EDIT_USER_ROLE;
import static com.nibss.nipreport.model.AdminResource.VIEW_USER_ROLE;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.UserRoleFacade;
import com.nibss.nipreport.admin.repository.InstitutionTypeFacade;
import com.nibss.nipreport.admin.repository.RoleFacade;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.data.TreeData;
import com.nibss.nipreport.model.Flag;
import static com.nibss.nipreport.model.Resource.CREATE;
import static com.nibss.nipreport.model.Resource.EDIT;
import static com.nibss.nipreport.model.Resource.LIST;
import static com.nibss.nipreport.model.Resource.VIEW;
import java.util.ArrayList;
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
@Path(AdminResource.USER_ROLE)
@Named
@RequestScoped
public class UserRoleBean extends AbstractBean<UserRole> {

    @Inject
    private UserRoleFacade userRoleFacade;

    public UserRoleBean() {
        super(AdminResource.USER_ROLE);
    }

    public static UserRoleBean instance() {
        return instance(UserRoleBean.class);
    }

    @Override
    public AbstractFacade<UserRole> getRepository() {
        return userRoleFacade;
    }

    @RolesAllowed({LIST_USER_ROLE, CREATE_USER_ROLE, EDIT_USER_ROLE})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("userRoleId") Long userRoleId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_USER_ROLE;
                roleName = LIST_USER_ROLE;
                break;
            }
            case EDIT: {
                resourceName = EDIT_USER_ROLE;
                break;
            }
            case CREATE: {
                resourceName = CREATE_USER_ROLE;
                UserRole userRole = new UserRole();
                //userRole.setInstitution(InstitutionTypeFacade.instance().find(Flag.USER_ROLE_INSTITUTION_TYPE));
                setEntity(userRole);
                break;
            }
            default: {
                resourceName = LIST_USER_ROLE;
                break;
            }
        }
        return renderView(userRoleId, resourceName, roleName);
    }

    @RolesAllowed({CREATE_USER_ROLE})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid UserRole userRole) {
        DataResponse dataResponse = new DataResponse();
        try {
            userRole.setEnabled(true);
            List<Message> msgs = validate(userRole);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                getRepository().create(userRole, createActivityLog(userRole));
                dataResponse.addMessage(new Message("UserRole created successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("UserRole creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({EDIT_USER_ROLE})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid UserRole userRole) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(userRole);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                userRole = sanitize(userRole.getUserRoleId(), userRole);
                getRepository().edit(userRole, createActivityLog(userRole));
                dataResponse.addMessage(new Message("UserRole modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("UserRole modification failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({DELETE_USER_ROLE})
    @DELETE
    @Path("delete.action")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse delete(@QueryParam("institutionId") Long institutionId) {
        DataResponse dataResponse = new DataResponse();
        try {
            UserRole userRole = sanitize(institutionId, null);
            userRole.setFlag(Flag.DELETED);
            getRepository().delete(userRole, createActivityLog(userRole));
            dataResponse.addMessage(new Message("UserRole deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("UserRole deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    //
    @GET
    @Path("institution-types")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getInstitutionTypes() {
        return getEntities(InstitutionTypeFacade.instance(), false, "institutionTypeId", "institutionTypeName");
    }

    @GET
    @Path("role-config")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getRoleConfig(@QueryParam("institutionTypeId") Long institutionTypeId) {
        DataResponse dataResponse = new DataResponse();
        if (institutionTypeId == null) {
            dataResponse.addMessage(new FieldMessage("institutionTypeId", "Institution Type should be selected", Message.SEVERITY_ERROR));
            return dataResponse;
        }
        List<Role> roleList = RoleFacade.instance().findByInstitutionType(institutionTypeId);
        List<TreeData> treeDataList = new ArrayList<>(roleList.size());
        for (Role role : roleList) {
            if (role != null) {
                TreeData treeData = new TreeData();
                treeData.setId(String.valueOf(role.getRoleId()));
                treeData.setLabel(role.getDescription() != null ? role.getDescription() : role.getRoleName());
                treeData.setParentId(role.getParentRoleId() != null ? role.getParentRoleId().toString() : "#");
                if (treeData.getId() != null) {
                    treeDataList.add(treeData);
                }
            }
        }
        dataResponse.setValid(true);
        dataResponse.setData(treeDataList);
        return dataResponse;
    }

//    @GET
//    @Path("role-configx")
//    @Produces(MediaType.APPLICATION_JSON)
//    public DataResponse getRoleConfig_(@QueryParam("institutionTypeId") Integer institutionTypeId, @QueryParam("userRoleId") Long userRoleId, @QueryParam("roleConfig") String roleConfig) {
//        String[] selectedRoleConfig = null;
//        UserRole userRole = userRoleId != null ? UserRoleFacade.instance().find(userRoleId) : null;
//        roleConfig = userRole != null ? userRole.getRoleConfig() : roleConfig;
//        if (roleConfig != null) {
//            selectedRoleConfig = roleConfig.split(",");
//        }
//
//        DataResponse dataResponse = new DataResponse();
//        if (institutionTypeId == null) {
//            dataResponse.addMessage(new FieldMessage("institutionTypeId", "Institution Type should be selected", Message.SEVERITY_ERROR));
//            return dataResponse;
//        }
//        List<Object[]> roleList = RoleFacade.instance().findByInstitutionType(institutionTypeId);
//        List<TreeData> treeDataList = new ArrayList<>(roleList.size());
//        for (Object[] role : roleList) {
//            if (role != null) {
//                TreeData treeData = new TreeData();
//                for (int x = 0; x < role.length; x++) {
//                    String value = role[x] != null ? role[x].toString() : null;
//                    switch (x) {
//                        case 0:
//                            treeData.setId(value);
//                            break;
//                        case 1:
//                            treeData.setLabel(value);
//                            break;
//                        case 2:
//                            treeData.setParentId(value != null ? value : "#");
//                            break;
//                    }
//                }
//                if (treeData.getId() != null) {
//                    treeDataList.add(treeData);
//                    treeData.setIcon("unselected");
//                    if (selectedRoleConfig != null) {
//                        for (String c : selectedRoleConfig) {
//                            if (c != null && !c.isEmpty() && c.equals(treeData.getId())) {
//                                treeData.setIcon("selected");
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        dataResponse.setData(treeDataList);
//        return dataResponse;
//    }
    //
    protected UserRole sanitize(Long institutionId, UserRole _userRole) {
        if (institutionId != null) {
            UserRole userRole = getRepository().find(institutionId);
            if (userRole != null) {
                if (_userRole != null) {
                    userRole.setUserRoleName(_userRole.getUserRoleName());
                    userRole.setRoleConfig(_userRole.getRoleConfig());
                    //String admin = _userRole.getAdmin();
                    userRole.setAdmin(_userRole.isAdmin());
                    userRole.setFlag(_userRole.getFlag());
                }
                return userRole;
            }
        }
        throw new IllegalArgumentException("UserRole identity could not be found.");
    }

    @Override
    protected List<Message> validate(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        List<Message> messages = super.validate(userRole);
        if (userRole.getUserRoleName() == null || userRole.getUserRoleName().isEmpty()) {
            messages.add(new FieldMessage("userRoleName", "User Role Name is required", Message.SEVERITY_ERROR));
        }
        if (messages.isEmpty()) {
            if (userRole.getInstitutionTypeId() != null) {
                userRole.setInstitutionType(getEntity() != null && getEntity().getInstitutionType() != null && userRole.getInstitutionTypeId().equals(getEntity().getInstitutionType().getInstitutionTypeId())
                        ? getEntity().getInstitutionType() : InstitutionTypeFacade.instance().find(userRole.getInstitutionTypeId()));
            } else {
                userRole.setInstitutionType(null);
            }
            if (userRole.getInstitutionType() == null) {
                messages.add(new FieldMessage("institutionTypeId", "Institution Type is required", Message.SEVERITY_ERROR));
            }
        }

        if (messages.isEmpty() && userRoleFacade.containsUserRoleName(userRole.getUserRoleName(), userRole.getInstitutionTypeId(), userRole.getUserRoleId())) {
            messages.add(new FieldMessage("userRoleName", "Duplicate User Role Name. (User Role Code already exist)", Message.SEVERITY_ERROR));
        }
        return messages;
    }

}
