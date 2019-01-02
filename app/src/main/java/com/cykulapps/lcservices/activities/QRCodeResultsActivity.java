package com.cykulapps.lcservices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.helper.EventsScannerActivity;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.views.CustomButton;


public class QRCodeResultsActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    CustomButton rescan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_status_screen);

        String msg= getIntent().getStringExtra("msg");
        String status = getIntent().getStringExtra("status");
        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageview);
        rescan = findViewById(R.id.scanagain);

        if (status.equals(Constants.TRUE)) {
            textView.setText(msg);
            imageView.setImageResource(R.drawable.tick);
        }else{
            textView.setText(msg);
            imageView.setImageResource(R.drawable.exclamation);
        }

        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(QRCodeResultsActivity.this, EventsScannerActivity.class);
                startActivity(refresh);
                QRCodeResultsActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
