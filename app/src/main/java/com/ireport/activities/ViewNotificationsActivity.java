package com.ireport.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class ViewNotificationsActivity extends AppCompatPreferenceActivity {

        /*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_notifications);
        }
        */
        static final String[] MOBILE_OS =
                new String[]{"Android", "iOS", "WindowsMobile", "Blackberry"};

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setListAdapter(new NotificationsArrayAdapter(this, MOBILE_OS));

            getSupportActionBar().setTitle("Your Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {

            //get selected items
            String selectedValue = (String) getListAdapter().getItem(position);
            Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

        }
}
