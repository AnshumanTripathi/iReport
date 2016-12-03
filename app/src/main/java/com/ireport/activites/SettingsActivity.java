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

public class SettingsActivity extends PreferenceActivity {

    public SwitchPreference emailPref, notificationsPref, anonPref;
    private static String TAG = "Settings";

    private static  Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Log.d(TAG, "preference: " + preference.toString() + " changed to : " + value.toString());
            return true;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_notification);

        emailPref = (SwitchPreference) findPreference("notifications_email_confirmation");
        notificationsPref = (SwitchPreference) findPreference("notifications_status_change");
        anonPref = (SwitchPreference) findPreference("notifications_report_anonymously");

        emailPref.setOnPreferenceChangeListener(onPreferenceChangeListener);
        notificationsPref.setOnPreferenceChangeListener(onPreferenceChangeListener);
        anonPref.setOnPreferenceChangeListener(onPreferenceChangeListener);

    }




    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}