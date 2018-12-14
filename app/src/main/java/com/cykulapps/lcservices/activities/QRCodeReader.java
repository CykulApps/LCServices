package com.cykulapps.lcservices.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QRCodeReader extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    ProgressDialog progressDialog;
    String departmentID, qrCode;
    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        departmentID = bundle.getString("stuff");
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.QR_Scan),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            JSONObject rootObject = new JSONObject(response);
                            String status = rootObject.getString("result_status");
                            String msg = rootObject.getString("report_status");
                            Log.d("JE", "" + response);
                            Intent intent = new Intent(QRCodeReader.this, QRCodeResultsActivity.class);
                            intent.putExtra("msg", msg);
                            intent.putExtra("status", status);
                            startActivity(intent);
                            QRCodeReader.this.finish();


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
                            Toast.makeText(QRCodeReader.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(QRCodeReader.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(QRCodeReader.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

               // params.put(Constants.EVENTID, Prefs.getString(Prefs.EVENT_CODE, null));
                params.put("departmentID", departmentID);
                params.put("qrCode", qrCode);
                Log.e("sharath","---->"+params);
                return params;

            }
        };
        requestQueue.add(stringRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0 , -1 , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }


}
