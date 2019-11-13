/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.admin.bean;

import com.elixir.commons.crypto.Encoder;
import com.elixir.commons.util.RandomGenerator;
import com.elixir.commons.validator.EmailValidator;
import static com.nibss.nipreport.admin.bean.AbstractBean.instance;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.model.AdminResource;
import static com.nibss.nipreport.model.AdminResource.CREATE_USER;
import static com.nibss.nipreport.model.AdminResource.EDIT_USER;
import static com.nibss.nipreport.model.AdminResource.VIEW_USER;
import com.nibss.nipreport.admin.repository.AbstractFacade;
import com.nibss.nipreport.admin.repository.BranchFacade;
import com.nibss.nipreport.admin.repository.InstitutionFacade;
import com.nibss.nipreport.admin.repository.UserIdentityFacade;
import com.nibss.nipreport.admin.repository.UserRoleFacade;
import com.nibss.nipreport.bean.MailBean;
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
@Path(AdminResource.USER)
@Named
@RequestScoped
public class UserBean extends AbstractBean<UserIdentity> {

    @Inject
    private UserIdentityFacade userIdentityFacade;

    public UserBean() {
        super(AdminResource.USER);
    }

    public static UserBean instance() {
        return instance(UserBean.class);
    }

    @Override
    public AbstractFacade<UserIdentity> getRepository() {
        return userIdentityFacade;
    }

    @RolesAllowed({LIST_USER, CREATE_USER, EDIT_USER})
    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName, @QueryParam("userId") Long userId) {
        String roleName = null;
        switch ((resourceName == null || resourceName.isEmpty()) ? LIST : resourceName) {
            case VIEW: {
                resourceName = VIEW_USER;
                roleName = LIST_USER;
                break;
            }
            case EDIT: {
                resourceName = EDIT_USER;
                break;
            }
            case CREATE: {
                resourceName = CREATE_USER;
                UserIdentity userIdentity = new UserIdentity();
                setEntity(userIdentity);
                break;
            }
            default: {
                resourceName = LIST_USER;
                break;
            }
        }
        return renderView(userId, resourceName, roleName);
    }

    @RolesAllowed({CREATE_USER})
    @POST
    @Path("create.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse create(@Valid final UserIdentity userIdentity) {
        DataResponse dataResponse = new DataResponse();
        try {
            userIdentity.setEnabled(true);
            List<Message> msgs = validate(userIdentity);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                userIdentity.setCreatedDate(new Date());
                userIdentity.setCreatedBy(this.getLoggedInUser().getUserId());
                getRepository().create(userIdentity, createActivityLog(userIdentity));
                dataResponse.addMessage(new Message("UserIdentity created successfully.", Message.SEVERITY_INFO));
                try {
                    final String pwd = generateSystemPassword(userIdentity, null);
                    getRepository().save(userIdentity);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MailBean.instance().sendLoginDetails(userIdentity, pwd);
                        }
                    }).start();
                } catch (Exception e) {
                    log(e);
                    dataResponse.addMessage(new Message("Auto Generated Password did not complete succesfully. Please do a manual password reset.", Message.SEVERITY_ERROR));
                }
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("UserIdentity creation failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({EDIT_USER})
    @PUT
    @Path("edit.action")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse edit(@Valid UserIdentity userIdentity) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = validate(userIdentity);
            dataResponse.setMessages(msgs);
            dataResponse.setValid(!dataResponse.hasMessages());
            if (dataResponse.isValid()) {
                userIdentity = sanitize(userIdentity.getUserId(), userIdentity);
                getRepository().edit(userIdentity, createActivityLog(userIdentity));
                final UserIdentity user = userIdentity;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MailBean.instance().sendUserEditAlert(user);
                    }
                }).start();
                dataResponse.addMessage(new Message("UserIdentity modified successfully.", Message.SEVERITY_INFO));
            }
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("UserIdentity modification failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @RolesAllowed({DELETE_USER})
    @DELETE
    @Path("delete.action")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse delete(@QueryParam("userId") Long userId) {
        DataResponse dataResponse = new DataResponse();
        try {
            UserIdentity userIdentity = sanitize(userId, null);
            userIdentity.setFlag(Flag.DELETED);
            getRepository().delete(userIdentity, createActivityLog(userIdentity));
            dataResponse.addMessage(new Message("UserIdentity deleted successfully.", Message.SEVERITY_INFO));
            dataResponse.setValid(true);
        } catch (Exception e) {
            log(e);
            dataResponse.addMessage(new Message("UserIdentity deletion failed!", Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    protected UserIdentity sanitize(Long userId, UserIdentity _userIdentity) {
        if (userId != null) {
            UserIdentity userIdentity = getRepository().find(userId);
            if (userIdentity != null) {
                if (_userIdentity != null) {
                    userIdentity.setFirstName(_userIdentity.getFirstName());
                    userIdentity.setLastName(_userIdentity.getLastName());
                    userIdentity.setEmail(_userIdentity.getEmail());
                    userIdentity.setBranch(_userIdentity.getBranch());
                    userIdentity.setUserRole(_userIdentity.getUserRole());
                    userIdentity.setFlag(_userIdentity.getFlag());
                }
                return userIdentity;
            }
        }
        throw new IllegalArgumentException("UserIdentity identity could not be found.");
    }

    @Override
    protected List<Message> validate(UserIdentity userIdentity) {
        if (userIdentity == null) {
            return null;
        }
        List<Message> messages = super.validate(userIdentity);
        if (userIdentity.getUserId() == null) {
            if (userIdentity.getEmail() == null || userIdentity.getEmail().isEmpty()) {
                messages.add(new FieldMessage("email", "Email is required", Message.SEVERITY_ERROR));
            } else if (!getEmailValidator().validate(userIdentity.getEmail())) {
                messages.add(new FieldMessage("email", "Email format is not well formed", Message.SEVERITY_ERROR));
            } else if (userIdentityFacade.containsEmail(userIdentity.getEmail(), userIdentity.getUserId())) {
                messages.add(new FieldMessage("email", "Duplicate Email. (Email already exist)", Message.SEVERITY_ERROR));
            }
        }
        if (messages.isEmpty()) {
            if (userIdentity.getBranchId() != null) {
                userIdentity.setBranch(getEntity() != null && getEntity().getBranch() != null && userIdentity.getBranchId().equals(getEntity().getBranch().getBranchId()) ? getEntity().getBranch() : BranchFacade.instance().find(userIdentity.getBranchId()));
            } else {
                userIdentity.setBranch(null);
            }
            if (userIdentity.getBranch() == null) {
                messages.add(new FieldMessage("branchId", "Branch is required", Message.SEVERITY_ERROR));
            }
        }

        if (messages.isEmpty()) {
            if (userIdentity.getUserRoleId() != null) {
                userIdentity.setUserRole(getEntity() != null && getEntity().getUserRole() != null && userIdentity.getUserRoleId().equals(getEntity().getUserRole().getUserRoleId()) ? getEntity().getUserRole() : UserRoleFacade.instance().find(userIdentity.getUserRoleId()));
            } else {
                userIdentity.setUserRole(null);
            }
            if (userIdentity.getUserRole() == null) {
                messages.add(new FieldMessage("userRoleId", "User Role is required", Message.SEVERITY_ERROR));
            }
        }

        return messages;
    }

    //**********************
    @GET
    @Path("user-roles")//users
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse getUserRoles(@QueryParam("institutionId") Long institutionId) {
        DataResponse dataResponse = new DataResponse();
        Long institutionTypeId = InstitutionFacade.instance().findInstitutionTypeId(institutionId);
        if (institutionTypeId == null) {
            UserIdentity loggedInUser = this.getLoggedInUser();
            if (loggedInUser != null) {
                institutionTypeId = loggedInUser.getBranch().getInstitution().getInstitutionType().getInstitutionTypeId();
            }
        }
        try {
            List<Object[]> data = UserRoleFacade.instance().findByInstitutionTypeId(institutionTypeId, !isNibss());
            dataResponse.setData(toNameValuePair(data));
            dataResponse.setValid(true);
        } catch (Exception e) {
            dataResponse.addMessage(new Message("Fetching Records did not perform as intended. Please refresh", e.getMessage(), Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    @GET
    @Path("reset-password")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse resetPassword(@QueryParam("userId") Long userId) {
        return resetPassword(UserIdentityFacade.instance().find(userId), null);
    }

    @GET
    @Path("reset-default-password")
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse resetDefaultPassword(@QueryParam("userId") Long userId) {
        UserIdentity user = UserIdentityFacade.instance().find(userId);
        String pwd = user != null ? user.getEmail() : null;
        return resetPassword(UserIdentityFacade.instance().find(userId), pwd);
    }

    private DataResponse resetPassword(UserIdentity user, String pwd) {
        DataResponse dataResponse = new DataResponse();
        try {
            if (user == null) {
                throw new IllegalArgumentException();
            }
            pwd = generateSystemPassword(user, pwd);
            user.setLastPasswordChangeDate(new Date());
            user.setFlag(Flag.INACTIVE);
            MailBean.instance().sendPasswordReset(user, pwd);
            getRepository().save(user);
            dataResponse.setValid(true);
            dataResponse.addMessage(new Message("Password reset was successful.", Message.SEVERITY_INFO));
        } catch (Exception e) {
            log(e);
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Reset Password failed!", e.getMessage(), Message.SEVERITY_ERROR));
        }
        return dataResponse;
    }

    private String generateSystemPassword(UserIdentity user, String newPassword) throws Exception {
        newPassword = newPassword == null ? new RandomGenerator().getRandomPassword(10, 12, false) : newPassword;
        user.setPasswordHash(new Encoder().encode(newPassword, user.getUserId().toString()));
        return newPassword;
    }
    private EmailValidator emailValidator;

    private EmailValidator getEmailValidator() {
        if (emailValidator == null) {
            emailValidator = new EmailValidator();
        }
        return emailValidator;
    }

}
