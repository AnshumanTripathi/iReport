package com.ireport.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by AnshumanTripathi on 11/24/16.
 */
public class UserInfo implements Parcelable{
    //Official and UserInfo have similar properties. This differentiates user from official
    private boolean isOfficial;

    //UserInfo Details
    private String screenName;
    private String email;
    private String firstName;
    private String lastName;
    private String homeAddress;

    //UserInfo Settings
    private Settings settings = new Settings();

    public UserInfo() {
    }

    public UserInfo(String screenName,
                    String email,
                    String firstName,
                    String lastName,
                    String homeAddress) {
        this.screenName = screenName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
    }

    public UserInfo(String email){
        this.email = email;
        this.screenName = email;
    }

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


    public void resetInfo() {
        this.screenName = "";
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.homeAddress = "";
        this.settings.reset();
    }

    public String toString() {
        String ans = "";
        ans += getEmail() + " -> <";

        if(getScreenName()!=null)
            ans += getScreenName() + ", ";
        if(getFirstName()!=null)
            ans += getFirstName() + ", ";
        if(getLastName()!= null)
            ans += getLastName() + ", ";
        ans += Boolean.toString(isOfficial()) + ", ";
        if(getHomeAddress()!=null)
            ans += getHomeAddress() + ">, <";

        if(getSettings()!=null)
            ans += getSettings().toString() + ">";


        return ans;
    }


    /* sandhyar: everything below here is for implementing Parcelable */
    // Source: http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getScreenName()); out.writeString(getEmail());
        out.writeString(getFirstName());
        out.writeString(getLastName()); out.writeString(getHomeAddress());
        out.writeString(Boolean.toString(getSettings().isAllowEmailConfirmation()));
        out.writeString(Boolean.toString(getSettings().isAllowEmailNotification()));
        out.writeString(Boolean.toString(getSettings().isAnonymous()));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserInfo(Parcel in) {
        this.screenName = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.homeAddress = in.readString();
        this.setSettings(new Settings(Boolean.valueOf(in.readString()),
                Boolean.valueOf(in.readString()), Boolean.valueOf(in.readString())));
    }


}
