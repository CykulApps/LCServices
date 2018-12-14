package com.cykulapps.lcservices.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRCodeReader1 extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    ProgressDialog progressDialog;
    String departmentID, qrCode,mDepID,other;
    AlertDialog alert11;
    String riderID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getIntent().getExtras();
//        departmentID = bundle.getString("eventID");
//        mDepID = bundle.getString("departmentID");
        departmentID = Prefs.getString("eventID","");
        mDepID = Prefs.getString("departmentID","");

        Log.e("Keys==>",departmentID + "==>"+ mDepID);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                getSupportFragmentManager().popBackStack();
//            } else {
//                finish();
//            }
//        }
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

                        qrCode = bib.getText().toString().trim();
                        Log.e("riderID","no"+qrCode);

                        if (qrCode.isEmpty()) {
                            Toast.makeText(QRCodeReader1.this, "Please enter bib no", Toast.LENGTH_SHORT).show();
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
    public void handleResult(Result rawResult) {
        qrCode = rawResult.getContents();
        checkValidQRCode();
    }

    private void checkValidQRCode() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.QR_Scan1),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();

                            JSONObject rootObject = new JSONObject(response);
                            String status = rootObject.getString("result_status");
                            String msg = rootObject.getString("report_status");
                            Log.d("justengage", "" + response);
                            Intent intent = new Intent(QRCodeReader1.this, QRCodeResultsActivity.class);
                            intent.putExtra("msg", msg);
                            intent.putExtra("status", status);
                            startActivity(intent);
                            QRCodeReader1.this.finish();


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
                            Toast.makeText(QRCodeReader1.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(QRCodeReader1.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(QRCodeReader1.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                if(!((departmentID.equals("FR2018"))|| (departmentID.equals("JR2018"))))
                {
                    Log.e("if","if");
                    other=departmentID;
                    departmentID="JR2018";
                    Log.e("other","other"+other);
                    Log.e("departmentID","departmentID"+departmentID);

                }else
                {
                    Log.e("else","else");
                    other=departmentID;
                    Log.e("other","other"+other);
                    Log.e("departmentID","departmentID"+departmentID);

                }

                Map<String, String> params = new HashMap<>();
                params.put("eventID", departmentID);
                params.put("departmentID",mDepID);
                params.put("customerID", qrCode);
                params.put("other",other);
                Log.e("justengage","---->"+params);
                return params;

            }
        };
        requestQueue.add(stringRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0 , -1 , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }
}
