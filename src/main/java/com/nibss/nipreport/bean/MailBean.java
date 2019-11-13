/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.bean;

import com.elixir.commons.cdi.BeanManager;
import com.elixir.commons.mail.MailSender;
import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.context.AppConfig;
import com.nibss.nipreport.context.AppConstant;
import com.nibss.nipreport.context.AppUtil;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import org.apache.deltaspike.core.api.common.DeltaSpike;

/**
 *
 * @author oogunjimi
 */
@RequestScoped
public class MailBean {

    private Session mailSession;
    private MailSender mailSender;
    private Properties mailConfig;
    //@Context
    @Inject
    @DeltaSpike
    private HttpServletRequest request;
    @Inject
    private AppConfig appConfig;

    public MailBean() {
    }

    @PostConstruct
    private void init() {
        getMailConfig();
    }

    public static MailBean instance() {
        return BeanManager.INSTANCE.getReference(MailBean.class);
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public Session getMailSession() {
        return mailSession;
    }

    public void setMailSession(Session mailSession) {
        this.mailSession = mailSession;
    }

    private MailSender getMailSender() {
        if (mailSender == null) {
            mailSender = new MailSender(mailSession);
        }
        return mailSender;
    }

    public void sendAsHtml(String message, String subject, String... receivers) {
        getMailSender().setHtmlMessage(message);
        sendMail(subject, receivers);
    }

    public void sendAsText(String message, String subject, String... receivers) {
        getMailSender().setTextMessage(message);
        sendMail(subject, receivers);
    }

    private void _sendAsHtml(final String message, final String subject, final String... receivers) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getMailSender().setHtmlMessage(message);
                sendMail(subject, receivers);
            }
        });
        thread.start();
    }

    private synchronized void sendMail(String subject, String... receivers) {
        try {
            Properties _mailConfig = new Properties();
            _mailConfig.putAll(getMailConfig());
            getMailSender().setSubject(subject);
            getMailSender().setToRecipients(receivers);
            getMailSender().sendMail(_mailConfig);
        } catch (Exception ex) {
            AppUtil.log(ex);
        }
    }

    private Properties getMailConfig() {
        if (mailConfig == null) {
            if (request != null) {
                Object sessionMailConfig = request.getSession(true).getAttribute(AppConstant.MAIL_CONFIG_PARAM);
                if (sessionMailConfig instanceof Properties) {
                    mailConfig = (Properties) sessionMailConfig;
                }
            }
            if (mailConfig == null) {
                try {
                    Properties defaultMailConfig = getAppConfig().getMailConfig();
                    if (defaultMailConfig == null) {
                        throw new IllegalArgumentException();
                    }
                    mailConfig = new Properties();
                    mailConfig.putAll(defaultMailConfig);
                    if (request != null) {
                        request.getSession(true).setAttribute(AppConstant.MAIL_CONFIG_PARAM, mailConfig);
                    }
                } catch (Exception ex) {
                    AppUtil.log(ex);
                }
            }
        }
        return mailConfig;
    }

    private String htmlTemplateMessage(Properties msgMap, String msgKey) {
        if (msgMap == null) {
            msgMap = getAppConfig().getMailMessages();
            if (msgMap == null) {
                return null;
            }
        }
        String msgBody = (msgKey != null) ? msgMap.getProperty(msgKey) : "";
        String header = msgMap.getProperty("alert.header");
        String footer = msgMap.getProperty("alert.footer");
        try {
            String institutionName = "NIBSS";//UserIdentity.getLoggedInUserIdentity().getMaster().getSubsidiary().getSubsidiaryName();
            String applicationName = getAppConfig().getApplicationName();
            footer = new MessageFormat(footer).format(new String[]{institutionName, applicationName});
        } catch (Exception e) {
        }
        return header + msgBody + footer;
    }

    //*************************************************************************
    public void sendLoginDetails(UserIdentity ui, String password) {
        AppConfig config = getAppConfig();
        Properties msgMap = config.getMailMessages();
        String subject = msgMap.getProperty("login_details.subject");
        String message = htmlTemplateMessage(msgMap, "login_details.message");

        MessageFormat msgFormat = new MessageFormat(message);
        subject = new MessageFormat(subject).format(new String[]{config.getApplicationName()});
        String[] params = {ui.getFirstName() + " " + ui.getLastName(), config.getApplicationUrl(), config.getApplicationName(), ui.getBranch().getBranchName(), ui.getEmail(), password};
        message = msgFormat.format((Object[]) params);
        _sendAsHtml(message, subject, ui.getEmail());
    }

    public void sendUserEditAlert(UserIdentity user) {
        AppConfig config = getAppConfig();
        Properties msgMap = config.getMailMessages();
        String subject = msgMap.getProperty("user_edit.subject");
        String message = htmlTemplateMessage(msgMap, "user_edit.message");
        UserIdentity loggedInUser = ProfileBean.instance().getLoggedInUser();

        subject = new MessageFormat(subject).format(new String[]{config.getApplicationName()});
        String[] params = {user.getFirstName() + " " + user.getLastName(), config.getApplicationUrl(), config.getApplicationName(), user.getBranch().getBranchName(), user.getUserRole().getUserRoleName(), user.getFirstName(), user.getLastName(), user.getEmail(), loggedInUser.getEmail(), formatDate(new Date())};
        message = new MessageFormat(message).format((Object[]) params);
        _sendAsHtml(message, subject, user.getEmail());
    }

    public void sendPasswordReset(UserIdentity user, String password) {
        AppConfig config = getAppConfig();
        Properties msgMap = config.getMailMessages();
        String subject = msgMap.getProperty("password_reset.subject");
        String message = htmlTemplateMessage(msgMap, "password_reset.message");
        UserIdentity loggedInUser = ProfileBean.instance().getLoggedInUser();
        subject = new MessageFormat(subject).format(new String[]{config.getApplicationName()});
        String[] params = {user.getFirstName() + " " + user.getLastName(), config.getApplicationUrl(), config.getApplicationName(), user.getBranch().getBranchName(), user.getUserRole().getUserRoleName(), user.getEmail(), password, loggedInUser.getEmail(), formatDate(new Date())};
        message = new MessageFormat(message).format((Object[]) params);
        _sendAsHtml(message, subject, user.getEmail());
    }

    public void sendFeedback(List<String> emails) {
        AppConfig config = getAppConfig();
        Properties msgMap = config.getMailMessages();
        String subject = msgMap.getProperty("send_feedback.subject");
        String message = htmlTemplateMessage(msgMap, "send_feedback.message");
        UserIdentity loggedInUser = ProfileBean.instance().getLoggedInUser();
        subject = new MessageFormat(subject).format(new String[]{config.getApplicationName()});
        String[] params = {config.getApplicationUrl(), config.getApplicationName(), loggedInUser.getBranch().getInstitution().getInstitutionName(), loggedInUser.getBranch().getBranchName(), loggedInUser.getEmail(), formatDate(new Date())};
        message = new MessageFormat(message).format((Object[]) params);
        _sendAsHtml(message, subject, emails.toArray(new String[emails.size()]));
    }

    private DateFormat dateFormat = null;

    protected String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        }
        return dateFormat.format(date);
    }

    private <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    //@PreDestroy
    private void destroy() {
        if (mailSender != null) {
            mailSender.destroy();
        }
        mailSession = null;
        mailSender = null;
        mailConfig = null;
    }
}
