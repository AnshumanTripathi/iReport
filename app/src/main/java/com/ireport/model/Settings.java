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
        this.isAnonymous = false;
        this.isAllowEmailConfirmation = true;
        this.isAllowEmailNotification = true;
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

    public void reset() {
        this.isAllowEmailNotification = true;
        this.isAllowEmailConfirmation = true;
        this.isAnonymous = false;
    }

    public String toString() {
        return "{email: " + Boolean.toString(this.isAllowEmailConfirmation()) +
            ", notifications: " + Boolean.toString(this.isAllowEmailNotification()) +
                ", anonymous: " + Boolean.toString(this.isAnonymous()) + "}";
    }
}
