package com.cykulapps.lcservices.ticketing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.helper.MessageActivity;
import com.cykulapps.lcservices.helper.ScannerActivity;
import com.cykulapps.lcservices.overrideFonts;
import com.cykulapps.lcservices.utils.Utils;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class CardScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{
    ZBarScannerView mScannerView;
    String riderID;
    ProgressDialog progressDialog;
    String deptID, eventID;
    Intent intent;
    AlertDialog alert11;
    private Camera camera;
    private Camera.Parameters parameter;
    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);
        setContentView(R.layout.activity_card_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Programmatically initialize the scanner view
        mScannerView = new ZBarScannerView(this);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        contentFrame.addView(mScannerView);
        intent = getIntent();
        deptID = intent.getStringExtra("deptName");
        eventID = intent.getStringExtra("eventID");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences.getString("eventID", null);
        deptID = sharedPreferences.getString("departID", null);
        String appName = sharedPreferences.getString("category", null);
        TextView tv_appName = (TextView)findViewById(R.id.appName);
        tv_appName.setText(appName);


    }



   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                View view = LayoutInflater.from(this).inflate(R.layout.enterdata, null);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("LC Services");
                builder1.setView(view);
                builder1.setCancelable(true);
                alert11 = builder1.create();
                alert11.show();
                final EditText bib = view.findViewById(R.id.et_bib);
                bib.setHint("Enter Card Number");
                Button btn = view.findViewById(R.id.submit);
                btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        riderID = bib.getText().toString().trim();
                       // sendretunintentent(riderID);
                        Log.e("riderID","no"+riderID);

                        if (riderID.isEmpty()) {
                            Toast.makeText(CardScanActivity.this, "Please enter Card Number", Toast.LENGTH_SHORT).show();
                        } else {

                            alert11.dismiss();
                        }

                    }
                });
                break;
            default:
                return super.onOptionsItemSelected(item);
*//*
            case R.id.flash:

                openFlash();
                break;*//*


        }
        return true;
    }*/

    private void sendretunintentent(final String riderID) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",riderID);
        Log.e("riderID==",riderID);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void handleResult(Result result) {
        riderID = result.getContents();
        Log.e(" riderID", "" + riderID);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",riderID);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    public void openFlash()
    {
        deviceHasFlash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!deviceHasFlash){
            Toast.makeText(CardScanActivity.this, "Sorry, you device does not have any camera", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            this.camera = Camera.open(0);
            parameter = this.camera.getParameters();

            if(!isFlashLightOn){
                turnOnTheFlash1();
            }else{
                turnOffTheFlash1();
            }
        }
    }

    private void turnOnTheFlash1()
    {

    }
    public  void turnOffTheFlash1()
    {

    }


    private void turnOffTheFlash() {
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        this.camera.setParameters(parameter);
        this.camera.stopPreview();
        isFlashLightOn = false;
        //flashLight.setImageResource(R.drawable.buttonoff);
    }

    private void turnOnTheFlash() {
        if(this.camera != null){
            parameter = this.camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameter);
            this.camera.startPreview();
            isFlashLightOn = true;
            //flashLight.setImageResource(R.drawable.buttonon);
        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameter = camera.getParameters();
            } catch (RuntimeException e) {
                System.out.println("Error: Failed to Open: " + e.getMessage());
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // getCamera();
    }

}
