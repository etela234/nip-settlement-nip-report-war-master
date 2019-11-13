/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.bean;

import com.elixir.commons.cdi.BeanManager;
import com.elixir.commons.crypto.Encoder;
import com.elixir.commons.util.RandomGenerator;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.admin.repository.UserIdentityFacade;
import com.nibss.nipreport.annotation.LoggedIn;
import com.nibss.nipreport.context.AppConfig;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import com.nibss.nipreport.data.DataResponse;
import com.nibss.nipreport.data.FieldMessage;
import com.nibss.nipreport.data.Message;
import com.nibss.nipreport.model.Flag;
import com.nibss.nipreport.model.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
@Path("profile")
@Named
@SessionScoped
public class ProfileBean implements Serializable {

    @Inject
    @DeltaSpike
    private HttpServletRequest request;
    @Inject
    private UserIdentityFacade userIdentityFacade;
    private DateFormat dateFormat;
    private DecimalFormat amountFormat;

    public UserIdentityFacade getRepository() {
        return userIdentityFacade;
    }

    public static ProfileBean instance() {
        return BeanManager.INSTANCE.getReference(ProfileBean.class);
    }

    @GET
    @Path("{resourceName}")
    @Produces({MediaType.TEXT_HTML})
    public View view(@PathParam("resourceName") String resourceName) {
        switch ((resourceName == null || resourceName.isEmpty()) ? Resource.LIST : resourceName) {
            case Resource.CHANGE_PASSWORD: {
                resourceName = Resource.CHANGE_PASSWORD;
                break;
            }
            default: {
                throw new IllegalArgumentException(resourceName);
            }
        }

        String contentPath = "/WEB-INF/views/" + resourceName + ".jspx";
        if (AppUtil.isAjaxRequest(request)) {
            return new View(contentPath);
        }
        NavigatorBean navBean = NavigatorBean.instance();
        navBean.setContentPath(contentPath);
        return new View(AppConstant.MAIN_PAGE);
    }

    @GET
    @POST
    @Path("error/{statusCode}")
    public View errorView(@PathParam("statusCode") Integer statusCode) {
        if (AppUtil.isAjaxRequest(request)) {
            return new View(AppConstant.ERROR_PAGE);
        }

        NavigatorBean navBean = NavigatorBean.instance();
        //navBean.setModuleName(null);
        navBean.setContentPath(AppConstant.ERROR_PAGE);
        return new View(AppConstant.MAIN_PAGE);
    }
//if (AppUtil.isAjaxRequest(request)) {
//            DataResponse dataResponse = new DataResponse(false);
//            return Response.ok(dataResponse).build();
//        }
//        NavigatorBean.instance().setContentPath(null);
//        try {
//            return Response.temporaryRedirect(new java.net.URI(AppConstant.MAIN_PAGE)).build();
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(ProfileBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return Response.serverError().build();

    @POST
    @Path("reset-password.action")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse resetPassword(@FormParam("email") String email) {
        DataResponse dataResponse = new DataResponse();
        if (email == null) {
            dataResponse.addMessage(new Message("Email cannot be empty!", Message.SEVERITY_ERROR));
            return dataResponse;
        }
        try {
            List<UserIdentity> users = getRepository().findByEmail(email);
            if (users.isEmpty()) {
                dataResponse.addMessage(new Message("User not found!", Message.SEVERITY_ERROR));
            } else if (users.size() > 1) {
                dataResponse.addMessage(new Message("Multiple users found for this email. Please contact the Administrator!", Message.SEVERITY_ERROR));
            }
            if (!dataResponse.hasMessages()) {
                UserIdentity user = users.get(0);
                String pwd = generateSystemPassword(user, null);
                user.setLastPasswordChangeDate(new Date());
                user.setFlag(Flag.INACTIVE);
                MailBean.instance().sendPasswordReset(user, pwd);
                getRepository().save(user);
                dataResponse.setValid(true);
                dataResponse.addMessage(new Message("Password reset was successful. Please check your mail for the new Password", Message.SEVERITY_INFO));
            }
        } catch (Exception ex) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Password Reset failed!", ex.getMessage(), Message.SEVERITY_ERROR));
            AppUtil.log(ex);
        }
        return dataResponse;
    }

    @POST
    @Path("change-password.action")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public DataResponse changePassword(@FormParam("currentPassword") String currentPassword,
            @FormParam("newPassword") String newPassword,
            @FormParam("confirmNewPassword") String confirmNewPassword) {
        DataResponse dataResponse = new DataResponse();
        try {
            List<Message> msgs = new ArrayList<>();
            if (currentPassword == null || currentPassword.isEmpty()) {
                msgs.add(new FieldMessage("currentPassword", "Current password Field is required", Message.SEVERITY_ERROR));
            }
            if (newPassword == null || newPassword.isEmpty()) {
                msgs.add(new FieldMessage("newPassword", "New password Field is required", Message.SEVERITY_ERROR));
            }
            if (confirmNewPassword == null || confirmNewPassword.isEmpty()) {
                msgs.add(new FieldMessage("confirmNewPassword", "Confirm New password Field is required", Message.SEVERITY_ERROR));
            }
            if (msgs.isEmpty() && !newPassword.equals(confirmNewPassword)) {
                msgs.add(new FieldMessage("confirmNewPassword", "Confirm New Password does not match New Password", Message.SEVERITY_ERROR));
            }
            if (msgs.isEmpty()) {
                passwordPolicy(newPassword, msgs);
            }
            dataResponse.setMessages(msgs);
            dataResponse.setValid(msgs.isEmpty());
            if (msgs.isEmpty()) {
                UserIdentity ui = getLoggedInUser();
                if (ui.getPasswordHash() != null) {
                    dataResponse = changePassword(dataResponse, ui, currentPassword, newPassword);
                } else {
                    dataResponse.setValid(false);
                    msgs.add(new FieldMessage("currentPassword", "Current Password does not match", Message.SEVERITY_ERROR));
                }
            }
        } catch (Exception ex) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Change Password failed!", ex.getMessage(), Message.SEVERITY_ERROR));
            AppUtil.log(ex);
        }
        return dataResponse;
    }

    public DataResponse changePassword(DataResponse dataResponse, UserIdentity ui, String currentPassword, String newPassword) {
        dataResponse = dataResponse == null ? new DataResponse() : dataResponse;
        try {
            Encoder encoder = new Encoder();
            String encodedCurrentPassword = encoder.encode(currentPassword, ui.getUserId().toString());
            String encodedNewPassword = encoder.encode(newPassword, ui.getUserId().toString());
            if (!encodedCurrentPassword.equals(ui.getPasswordHash())) {
                dataResponse.addMessage(new FieldMessage("currentPassword", "Current Password does not match", Message.SEVERITY_ERROR));
            }
            if (encodedCurrentPassword.equals(encodedNewPassword)) {
                dataResponse.addMessage(new FieldMessage("newPassword", "New Password cannot be the same as current Password.", Message.SEVERITY_ERROR));
            }
            if (dataResponse.getMessages() == null || dataResponse.getMessages().isEmpty()) {
                UserIdentity user = getRepository().find(ui.getUserId());
                user.setPasswordHash(encodedNewPassword);
                user.setLastPasswordChangeDate(new Date());
                user.setEnabled(true);
                user = getRepository().save(user);
                setLoggedInUser(user);
                dataResponse.addMessage(new Message("Password changed successfully.", Message.SEVERITY_INFO));
            } else {
                dataResponse.setValid(false);
            }
        } catch (Exception ex) {
            dataResponse.setValid(false);
            dataResponse.addMessage(new Message("Change Password failed!", ex.getMessage(), Message.SEVERITY_ERROR));
            AppUtil.log(ex);
        }
        return dataResponse;
    }

    private void passwordPolicy(String pwd, List<Message> msgs) {
        try {
            AppConfig config = AppConfig.instance();
            if (config == null) {
                return;
            }
            Integer minPasswordLength = config.getMinPasswordLength();
            if (minPasswordLength != null && pwd.length() < minPasswordLength) {
                msgs.add(new FieldMessage("newPassword", "Password length must be a minimum of " + minPasswordLength + " character(s)", Message.SEVERITY_ERROR));
            }
            boolean[] ans = containsAlphaNumericSymbolicUpperCaseChar(pwd);
            boolean[] fmt = new boolean[]{
                config.isAlphabeticPasswordFormat(),
                config.isNumericPasswordFormat(),
                config.isSymbolicPasswordFormat(),
                config.isUppercasePasswordFormat()};
            if ((fmt[0] && !ans[0]) || (fmt[1] && !ans[1]) || (fmt[2] && !ans[2]) || (fmt[3] && !ans[3])) {
                msgs.add(new FieldMessage("newPassword", "Password Format must include a combination of " + (fmt[0] ? "[Alphabet] " : "") + (fmt[1] ? "[Number] " : "") + (fmt[2] ? "[Special Character] " : "") + (fmt[3] ? "[Upper Case] " : ""), Message.SEVERITY_ERROR));
            }
        } catch (Exception x) {
            msgs.add(new Message("An unexpected error occured while validating the new password.", x.getMessage(), Message.SEVERITY_ERROR));
        }
    }

    private boolean[] containsAlphaNumericSymbolicUpperCaseChar(String value) {
        char[] chars = value.toCharArray();
        boolean letter = false, digit = false, specialChar = false, uppercaseChar = false;
        for (char c : chars) {
            if ((!letter || !uppercaseChar) && Character.isLetter(c)) {
                letter = true;
                if (Character.isUpperCase(c)) {
                    uppercaseChar = true;
                }
            } else if (!digit && Character.isDigit(c)) {
                digit = true;
            } else if (!specialChar && !Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                specialChar = true;
            }
            if (letter && digit && specialChar && uppercaseChar) {
                break;
            }
        }
        return new boolean[]{letter, digit, specialChar, uppercaseChar};
    }

    private String generateSystemPassword(UserIdentity user, String newPassword) throws Exception {
        newPassword = newPassword == null ? new RandomGenerator().getRandomPassword(10, 12, false) : newPassword;
        user.setPasswordHash(new Encoder().encode(newPassword, user.getUserId().toString()));
        return newPassword;
    }

    //***********util methods
    public boolean isNibss() {
        try {
            return getLoggedInUser().getBranch().getInstitution().getInstitutionType().getInstitutionTypeId().equals(Flag.NIBSS_INSTITUTION_TYPE);
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    @javax.enterprise.inject.Produces
    @Named("loggedInUser")
    @LoggedIn
    public UserIdentity getLoggedInUser() {
        HttpSession session = request != null ? request.getSession(false) : null;
        if (session != null) {
            Object user = session.getAttribute(AppConstant.USER_PARAM);
            if (user instanceof UserIdentity) {
                return (UserIdentity) user;
            }
        }
        return null;
    }

    protected void setLoggedInUser(UserIdentity user) {
        HttpSession session = request != null ? request.getSession(false) : null;
        if (session != null) {
            session.setAttribute(AppConstant.USER_PARAM, user);
        }
    }

    public boolean hasRole(String roleName) {
        if (roleName == null) {
            return false;
        }
        UserIdentity loggedInUser = getLoggedInUser();
        List<String> _roleNames = loggedInUser != null ? loggedInUser.getRoleNames() : null;
        return (_roleNames != null) ? _roleNames.contains(roleName) : false;
    }

    public boolean hasAnyRole(String roleNamesVar) {
        if (roleNamesVar == null) {
            return false;
        }
        String[] roleNames = roleNamesVar.split(",");
        UserIdentity loggedInUser = getLoggedInUser();
        List<String> _roleNames = loggedInUser != null ? loggedInUser.getRoleNames() : null;
        if (_roleNames != null) {
            for (String _roleName : _roleNames) {
                if (_roleName != null) {
                    for (String roleName : roleNames) {
                        if (roleName != null && _roleName.equals(roleName)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean likeAnyRole(String roleNamesVar) {
        if (roleNamesVar == null) {
            return false;
        }
        String[] roleNames = roleNamesVar.split(",");
        UserIdentity loggedInUser = getLoggedInUser();
        List<String> _roleNames = loggedInUser != null ? loggedInUser.getRoleNames() : null;
        if (_roleNames != null) {
            for (String _roleName : _roleNames) {
                if (_roleName != null) {
                    for (String roleName : roleNames) {
                        if (roleName != null && _roleName.contains(roleName)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isUserPasswordExpired() {
        try {
            Date date = getLoggedInUser().getLastPasswordChangeDate();
            if (date == null) {
                return false;
            }
            AppConfig config = AppConfig.instance();
            Integer x = config != null ? config.getPasswordLifespan() : null;
            if (x == null || x <= 0) {
                return false;
            }
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, x);
            return (c.before(new Date()));
        } catch (Exception ex) {
            AppUtil.log(ex);
        }
        return false;
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dateFormat.format(date);
    }

    public String formatAmount(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        if (amountFormat == null) {
            amountFormat = new DecimalFormat("#,##0.00");
        }
        return amountFormat.format(amount);
    }

}
