/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.elixir.commons.cdi.BeanManager;
import com.elixir.commons.web.context.UserRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContext;

/**
 *
 * @author oogunjimi
 */
@ApplicationScoped
public class AppConfig implements Serializable {

    private UserRegistry userRegistry;
    private String applicationName = "NIP Report";
    private String applicationUrl;
    private String applicationSupportEmail;
    private String homeDir;
    private boolean debug;
    private Properties mailConfig;
    private Properties mailMessages;
    //
    private Integer sessionTimeout;
    private Integer inactivityPeriod;
    private Integer passwordLifespan;
    private Integer minPasswordLength;
    private boolean alphabeticPasswordFormat = true;
    private boolean numericPasswordFormat = true;
    private boolean symbolicPasswordFormat = true;
    private boolean uppercasePasswordFormat = true;
    private String settlementReportDir;
    private String billingReportDir;
    //
    private String wsHostname;
    private Integer wsPort;

    public static AppConfig instance() {
        return BeanManager.INSTANCE.getReference(AppConfig.class);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public String getApplicationSupportEmail() {
        return applicationSupportEmail;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public boolean isDebug() {
        return debug;
    }

    public Properties getMailConfig() {
        return mailConfig;
    }

    public Properties getMailMessages() {
        return mailMessages;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public Integer getInactivityPeriod() {
        return inactivityPeriod;
    }

    public Integer getPasswordLifespan() {
        return passwordLifespan;
    }

    public Integer getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(Integer minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public boolean isAlphabeticPasswordFormat() {
        return alphabeticPasswordFormat;
    }

    public void setAlphabeticPasswordFormat(boolean alphabeticPasswordFormat) {
        this.alphabeticPasswordFormat = alphabeticPasswordFormat;
    }

    public boolean isNumericPasswordFormat() {
        return numericPasswordFormat;
    }

    public void setNumericPasswordFormat(boolean numericPasswordFormat) {
        this.numericPasswordFormat = numericPasswordFormat;
    }

    public boolean isSymbolicPasswordFormat() {
        return symbolicPasswordFormat;
    }

    public void setSymbolicPasswordFormat(boolean symbolicPasswordFormat) {
        this.symbolicPasswordFormat = symbolicPasswordFormat;
    }

    public boolean isUppercasePasswordFormat() {
        return uppercasePasswordFormat;
    }

    public void setUppercasePasswordFormat(boolean uppercasePasswordFormat) {
        this.uppercasePasswordFormat = uppercasePasswordFormat;
    }

    public String getSettlementReportDir() {
        return settlementReportDir;
    }

    public String getBillingReportDir() {
        return billingReportDir;
    }

    public UserRegistry getUserRegistry() {
        if (userRegistry == null) {
            userRegistry = new UserRegistry();
        }
        return userRegistry;
    }

    public Integer getWsPort() {
        return wsPort;
    }

    public String getWsHostname() {
        return wsHostname;
    }

   

    //*********************
    public void init(ServletContext sc) throws IOException {
        String home = sc.getRealPath("resources");
        int tmpIndex = home.lastIndexOf(File.separator + "tmp" + File.separator);
        tmpIndex = tmpIndex == -1 ? home.lastIndexOf(File.separator + "deployments" + File.separator) : tmpIndex;
        if (tmpIndex > 0) {
            home = home.substring(0, tmpIndex) + File.separator + AppConstant.HOME_DIR_NAME;
        }
        homeDir = home;
        readMailConfig();
        readMailMessages();
        readConfig(sc);
    }

    private Properties loadConfig(String propFile) throws IOException {
        File file = new File(getHomeDir() + File.separator + propFile);
        if (file.exists()) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        }
        return null;
    }

    private Properties loadConfig(InputStream is) {
        try {
            Properties prop = new Properties();
            prop.load(is);
            return prop;
        } catch (IOException ex) {
            AppUtil.log(ex);
        }
        return null;
    }

    public void readMailConfig() {
        if (mailConfig == null) {
            try {
                mailConfig = loadConfig("config" + File.separator + "mail.conf");
            } catch (IOException ex) {
                AppUtil.log(ex);
            }
            if (mailConfig == null) {
                mailConfig = loadConfig(this.getClass().getClassLoader().getResourceAsStream("mail.properties"));
                AppUtil.log("mail configuration defaulted");
            }
        }

    }

    public void readMailMessages() {
        if (mailMessages == null) {
            try {
                mailMessages = loadConfig("config" + File.separator + "mail-messages.conf");
            } catch (IOException ex) {
                AppUtil.log(ex);
            }
            if (mailMessages == null) {
                mailMessages = loadConfig(this.getClass().getClassLoader().getResourceAsStream("mail-messages.properties"));
                AppUtil.log("mail messages configuration defaulted");
            }
        }
    }

    private void readConfig(ServletContext sc) throws IOException {
        Properties prop = loadConfig("config" + File.separator + "nipreport.conf");
        if (prop == null) {
            AppUtil.log("No Application config Found:(reading config skipped)");
            return;
        }
        AppUtil.log("reading Application config");
        String value;

        value = prop.getProperty("debug");
        if (value != null && !value.isEmpty()) {
            this.debug = Boolean.parseBoolean(value);
        }

        value = prop.getProperty("application-url");
        if (value != null && !value.isEmpty()) {
            this.applicationUrl = value;
        }

        value = prop.getProperty("application-support-email");
        if (value != null && !value.isEmpty()) {
            this.applicationSupportEmail = value;
        }
        value = prop.getProperty("application-name");
        if (value != null && !value.isEmpty()) {
            this.applicationName = value;
        }
        value = prop.getProperty("settlement-report-dir");
        if (value != null && !value.isEmpty()) {
            this.settlementReportDir = value;
        }
        value = prop.getProperty("billing-report-dir");
        if (value != null && !value.isEmpty()) {
            this.billingReportDir = value;
        }

        value = prop.getProperty("session-timeout");
        if (value != null && !value.isEmpty()) {
            try {
                this.sessionTimeout = Integer.parseInt(value);
            } catch (Exception se) {
                AppUtil.log("session-timeout must be numeric");
            }
        }
        value = prop.getProperty("inactivity-period");
        if (value != null && !value.isEmpty()) {
            try {
                this.inactivityPeriod = Integer.parseInt(value);
            } catch (Exception se) {
                AppUtil.log("inactivity-period must be numeric");
            }
        }
        value = prop.getProperty("password-lifespan");
        if (value != null && !value.isEmpty()) {
            try {
                this.passwordLifespan = Integer.parseInt(value);
            } catch (Exception se) {
                AppUtil.log("password-lifespan must be numeric");
            }
        }
        value = prop.getProperty("min-password-length");
        if (value != null && !value.isEmpty()) {
            try {
                this.minPasswordLength = Integer.parseInt(value);
            } catch (Exception se) {
                AppUtil.log("min-password-length must be numeric");
            }
        }

        value = prop.getProperty("alphabetic-password-format");
        if (value != null && !value.isEmpty()) {
            try {
                this.alphabeticPasswordFormat = Boolean.parseBoolean(value);
            } catch (Exception se) {
                AppUtil.log("alphabetic-password-format must be boolean - true or false");
            }
        }
        value = prop.getProperty("numeric-password-format");
        if (value != null && !value.isEmpty()) {
            try {
                this.numericPasswordFormat = Boolean.parseBoolean(value);
            } catch (Exception se) {
                AppUtil.log("numeric-password-format must be boolean - true or false");
            }
        }
        value = prop.getProperty("symbolic-password-format");
        if (value != null && !value.isEmpty()) {
            try {
                this.symbolicPasswordFormat = Boolean.parseBoolean(value);
            } catch (Exception se) {
                AppUtil.log("symbolic-password-format must be boolean - true or false");
            }
        }
        value = prop.getProperty("uppercase-password-format");
        if (value != null && !value.isEmpty()) {
            try {
                this.uppercasePasswordFormat = Boolean.parseBoolean(value);
            } catch (Exception se) {
                AppUtil.log("uppercase-password-format must be boolean - true or false");
            }
        }

        value = prop.getProperty("ws-hostname");
        if (value != null && !value.isEmpty()) {
            this.wsHostname = value;
        }

        value = prop.getProperty("ws-port");
        if (value != null && !value.isEmpty()) {
            try {
                this.wsPort = Integer.parseInt(value);
            } catch (Exception se) {
                AppUtil.log("ws-port must be numeric");
            }
        }
    }

    public void destroy() {
    }

}
