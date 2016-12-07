package com.ireport.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;

import com.ireport.R;
import com.ireport.controller.utils.Constants;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateSettingsHandler;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, ICallbackActivity {

    public SwitchPreference emailPref, notificationsPref, anonPref;
    private static String TAG = "Settings";
    private static String anonStr = "notifications_report_anonymously";
    private static String emailStr = "notifications_email_confirmation";
    private static String notificationStr = "notifications_status_change";

    UpdateSettingsHandler updateSettingsHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_notification);

        Intent i = getIntent();
        UserInfo userInfo = i.getParcelableExtra("user_info");
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
            Settings settings = new Settings(
                sharedPreferences.getBoolean(emailStr, false),
                sharedPreferences.getBoolean(notificationStr, false),
                sharedPreferences.getBoolean(anonStr, false)
            );
            Log.d(TAG, settings.toString());
            updateSettingsHandler = new UpdateSettingsHandler
                    (this, "settings_activity", Constants.SANDHYA_EMAIL, settings);
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