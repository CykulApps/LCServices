package com.cykulapps.lcservices.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.login.CpLoginActivity;

public class CommonAdmin extends AppCompatActivity {

    CardView cardPark,cardEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_admin);

        cardPark = findViewById(R.id.card_park);
        cardEvents = findViewById(R.id.card_events);

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
                    startActivity(new Intent(CommonAdmin.this, CpDepartmentActivity.class));
                }
                else
                {
                    startActivity(new Intent(CommonAdmin.this, CpLoginActivity.class));

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                //.setTitle("Really Exit?")
                .setMessage("Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Prefs.logoutUser(getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), CpLoginActivity.class));
                        finish();
                    }
                }).create().show();
    }
}
