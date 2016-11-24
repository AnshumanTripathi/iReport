package com.ireport.model;

import com.ireport.repository.MongoRepository;

/**
 * Created by AnshumanTripathi on 11/24/16.
 */

public class User implements MongoRepository{
    //Official and User have similar properties. This differentiates user from official
    private boolean isOfficial;

    //User Details
    private String screenName;
    private String email;
    private String firstName;
    private String lastName;
    private String homeAddress;

    //User Settings
    private Settings settings;

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void insert() {

    }

    @Override
    public void getAll() {

    }
}
