package com.cykulapps.lcservices.helper;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.overrideFonts;

public class ScannerResultActivity extends AppCompatActivity {
    Intent intent;
    String messsage, status;
    TextView textView;
    String eventID, deptName;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        intent = getIntent();
        messsage = intent.getStringExtra("message");
        status = intent.getStringExtra("result");
        eventID = intent.getStringExtra("eventID");
        deptName = intent.getStringExtra("deptName");
        textView = findViewById(R.id.tv);
        imageView = findViewById(R.id.image);

        if (status.equals("true")) {

            textView.setText(messsage);
            textView.setTextColor(getResources().getColor(R.color.btn));
            imageView.setImageResource(R.drawable.right);
        } else {
            textView.setText(messsage);
            textView.setTextColor(Color.RED);
            imageView.setImageResource(R.drawable.alert);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void scanNext(View view) {
        startActivity(new Intent(this, ParksScannerActivity.class).putExtra("eventID", eventID).putExtra("deptName", deptName));
        finish();

    }
}
