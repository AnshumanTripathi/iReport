package com.ireport.model;

/**
 * Created by AnshumanTripathi on 11/24/16.
 */
public class Settings {
    private boolean isAllowEmailConfirmation;
    private boolean isAllowEmailNotification;
    private boolean isAnonymous;

    public Settings(boolean isAllowEmailConfirmation,
                    boolean isAllowEmailNotification,
                    boolean isAnonymous) {

        this.isAnonymous = isAnonymous;
        this.isAllowEmailConfirmation = isAllowEmailConfirmation;
        this.isAllowEmailNotification = isAllowEmailNotification;
    }

    public Settings() {

    }

    public boolean isAllowEmailConfirmation() {
        return isAllowEmailConfirmation;
    }

    public void setAllowEmailConfirmation(boolean allowEmailConfirmation) {
        isAllowEmailConfirmation = allowEmailConfirmation;
    }

    public boolean isAllowEmailNotification() {
        return isAllowEmailNotification;
    }

    public void setAllowEmailNotification(boolean allowEmailNotification) {
        isAllowEmailNotification = allowEmailNotification;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
