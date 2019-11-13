/*
 * copyright 2014 Nibss
 * http://www.nibss-plc.com/
 * 
 */
package com.nibss.nipreport.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
public abstract class AbstractFlag implements Flag {

    public boolean isEnabled() {
        return ENABLED.equals(getFlag());
    }

    public void setEnabled(boolean flag) {
        setFlag(flag ? ENABLED : DISABLED);
    }

    @XmlTransient
    public boolean isInactive() {
        return INACTIVE.equals(getFlag());
    }

    public String getFlagName() {
        return getFlagName(getFlag());
    }

    public static String getFlagName(String flag) {
        switch (flag) {
            case ENABLED:
                return "Enabled";
            case DISABLED:
                return "Disabled";
            case INACTIVE:
                return "Inactive";
            case LOCKED:
                return "Locked";
            case NEW:
                return "New";
            case OPEN:
                return "Open";
            case CLOSED:
                return "Closed";
        }
        return flag;
    }
    
    public static String getFlagName(int flag) {
        switch (flag) {
            case BUSY:
                return "Busy";
            case IDLE:
                return "Idle";
        }
        return String.valueOf(flag);
    }
}
