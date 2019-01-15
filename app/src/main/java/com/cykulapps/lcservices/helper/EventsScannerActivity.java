package com.cykulapps.lcservices.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.activities.QRCodeResultsActivity;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.overrideFonts;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class EventsScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    ProgressDialog progressDialog;
    String eventID, qrCode,deptID,deptName,datetime_timstamp,time,department;
    AlertDialog alert11;
    Calendar cal;
    Date currentLocalTime;
    DateFormat datetime, timeonly;
    Toolbar toolbar;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new added code added by Ashish
        deptName = Prefs.getString("deptName","");
        deptID = Prefs.getString("departmentID","");
        eventID = Prefs.getString("eventID","");
        department = Prefs.getString("eventSubDept","");

        setContentView(R.layout.activity_scanner_common);
        toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.appName);
        setSupportActionBar(toolbar);
        textView.setText(deptName);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        // Programmatically initialize the scanner view
        mScannerView = new ZBarScannerView(this);
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        contentFrame.addView(mScannerView);

        //get current time
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        currentLocalTime = cal.getTime();
        datetime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timeonly = new SimpleDateFormat("HH:mm:ss");

        time = timeonly.format(currentLocalTime); //current time
        datetime_timstamp = datetime.format(cal.getTime()); //current time and date

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enter_data, menu);//Menu Resource, Menu
        return true;
    }

    @Override
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
                Button btn = view.findViewById(R.id.submit);
                btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mScannerView.stopCamera();
                        qrCode = bib.getText().toString().trim();
                        Log.e("riderID","no"+qrCode);

                        if (qrCode.isEmpty()) {
                            Toast.makeText(EventsScannerActivity.this, "Please enter bib no", Toast.LENGTH_SHORT).show();
                        } else {
                            checkValidQRCode();
                            alert11.dismiss();
                        }
                    }
                });
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        qrCode = rawResult.getContents();
        checkValidQRCode();
    }

    private void checkValidQRCode() {
        mScannerView.stopCamera();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.QR_SCAN1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();

                            JSONObject rootObject = new JSONObject(response);
                            String status = rootObject.getString("result_status");
                            String msg = rootObject.getString("report_status");
                            Log.d("justengage", "" + response);
                            time = timeonly.format(currentLocalTime); //current time
                            datetime_timstamp = datetime.format(cal.getTime()); //current time and date
                            Intent intent = new Intent(EventsScannerActivity.this, QRCodeResultsActivity.class);
                            intent.putExtra("msg", msg);
                            intent.putExtra("status", status);
                            startActivity(intent);
                            EventsScannerActivity.this.finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(EventsScannerActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(EventsScannerActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(EventsScannerActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("eventID", eventID);
                params.put("deptID",deptID);
                params.put("deptName",deptName);
                params.put("bibNumber", qrCode);
                params.put("time",time);
                params.put("timestamp",datetime_timstamp);
                params.put("category",department);
                Log.e("justengage","---->"+params);
                return params;

            }
        };
        requestQueue.add(stringRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0 , -1 , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}