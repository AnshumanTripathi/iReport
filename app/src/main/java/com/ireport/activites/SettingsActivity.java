package com.ireport.activites;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toolbar;

import com.ireport.R;
import com.ireport.controller.utils.HttpUtils;
import com.ireport.model.Settings;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public SwitchPreference emailPref, notificationsPref, anonPref;
    private static String TAG = "Settings";
    private static String anonStr = "notifications_report_anonymously";
    private static String emailStr = "notifications_email_confirmation";
    private static String notificationStr = "notifications_status_change";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupActionBar();
        addPreferencesFromResource(R.xml.pref_notification);

        emailPref = (SwitchPreference) findPreference(emailStr);
        notificationsPref = (SwitchPreference) findPreference(notificationStr);
        anonPref = (SwitchPreference) findPreference(anonStr);
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
            HttpUtils.volley_updateSettings(settings, "sandhyafeb1990@gmail.com");
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

    private void setupActionBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
     */

}