package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateSettingsHandler;
import com.ireport.model.AppContext;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, ICallbackActivity {
    private AppCompatDelegate mDelegate;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    public SwitchPreference emailPref, notificationsPref, anonPref;
    private static String TAG = "Settings";
    private static String anonStr = "notifications_report_anonymously";
    private static String emailStr = "notifications_email_confirmation";
    private static String notificationStr = "notifications_status_change";

    UpdateSettingsHandler updateSettingsHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_delegate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        addPreferencesFromResource(R.xml.pref_notification);

        Intent i = getIntent();
        UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();
        Log.d(TAG, "Got userinfo: " + userInfo.toString());

        emailPref = (SwitchPreference) findPreference(emailStr);
        emailPref.setChecked(userInfo.getSettings().isAllowEmailConfirmation());
        notificationsPref = (SwitchPreference) findPreference(notificationStr);
        notificationsPref.setChecked(userInfo.getSettings().isAllowEmailNotification());
        anonPref = (SwitchPreference) findPreference(anonStr);
        anonPref.setChecked(userInfo.getSettings().isAnonymous());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "On shared preference changed");
        if (key.equals(emailStr) || key.equals(notificationStr) || key.equals(anonStr)) {
            Preference pref = findPreference(key);
            Log.d(TAG, key + " " + Boolean.toString(sharedPreferences.getBoolean(key, false)));

            boolean is_email_on = sharedPreferences.getBoolean(emailStr, false);
            boolean is_notif_on = sharedPreferences.getBoolean(notificationStr, false);
            boolean is_anonymous = sharedPreferences.getBoolean(anonStr, false);

            if (is_anonymous) {
                // we need to uncheck all other preferences when anonymous is checked
                sharedPreferences.edit().putBoolean(emailStr, false);
                sharedPreferences.edit().putBoolean(notificationStr, false);
                emailPref.setChecked(false);
                notificationsPref.setChecked(false);
                is_email_on = false;
                is_notif_on = false;
            } else if (is_email_on || is_notif_on) {
                is_anonymous = false;
                anonPref.setChecked(false);
                sharedPreferences.edit().putBoolean(anonStr, false);
            }
            Settings settings = new Settings(
                    is_email_on,
                    is_notif_on,
                    is_anonymous
            );

            Log.d(TAG, settings.toString());
            UserInfo user_info = AppContext.getInstance().getCurrentLoggedInUser();
            user_info.setSettings(settings);
            updateSettingsHandler = new UpdateSettingsHandler
                    (this, "settings_activity", user_info.getEmail(), settings);
            updateSettingsHandler.updateSettingForUser(getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            Log.d(TAG, "crappp");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        // sample code.
        Log.d(TAG, "In onPostProcessCompletion");
        if (responseObj instanceof UserInfo) {
            Log.d(TAG, "got userinfo!!!!");
            UserInfo userInfo = (UserInfo) responseObj;
            Log.d(TAG, userInfo.toString());
            Log.d(TAG, userInfo.getSettings().toString());
        }
    }
}