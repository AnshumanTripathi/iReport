package com.ireport.activites;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;

import com.ireport.R;
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
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_notification);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*
        addPreferencesFromResource(R.layout.settings_toolbar);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        android.support.v7.widget.Toolbar bar = (android.support.v7.widget.Toolbar) findViewById(R.id.settings_toolbar);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

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
//            HttpUtils.volley_updateSettings(settings, "sandhyafeb1990@gmail.com");
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


}