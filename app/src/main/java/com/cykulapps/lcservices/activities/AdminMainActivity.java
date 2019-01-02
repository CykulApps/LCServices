package com.cykulapps.lcservices.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.login.LoginActivity;

public class AdminMainActivity extends AppCompatActivity {

    CardView cardPark,cardEvents;
    ImageView parks, events;
    TextView textAdmin;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        textAdmin = findViewById(R.id.admin);
        cardPark = findViewById(R.id.card_park);
        cardEvents = findViewById(R.id.card_events);
        parks = findViewById(R.id.parks);
        events = findViewById(R.id.events);

        cardPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), DepartmentActivity.class);
                startActivity(intent);
            }
        });

        cardEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prefs.getBoolean(Prefs.LOGGED_IN,false))
                {
                    startActivity(new Intent(AdminMainActivity.this, EventsMainActivity.class));
                }else
                {
                    startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Prefs.logoutUser(getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }).create().show();
    }
}
