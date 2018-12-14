package com.cykulapps.lcservices.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.activities.CommonAdmin;
import com.cykulapps.lcservices.activities.CpDepartmentActivity;
import com.cykulapps.lcservices.activities.DepartmentActivity;
import com.cykulapps.lcservices.common.CommonUtils;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.utils.PrefController;
import com.cykulapps.lcservices.utils.TLSSocketFactory;
import com.cykulapps.lcservices.utils.Utils;
import com.cykulapps.lcservices.views.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CpLoginActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    private EditText username, password;
    private ProgressDialog progressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String userName, passWord, country;
    private TextInputLayout user_name_layout, password_name_layout;
    String eventID;
    String userType;
    String loginTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_login_activity);
        //getSupportActionBar().setElevation(0);
        checkAndRequestPermissions();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginbutton = findViewById(R.id.submitBtn);
        user_name_layout = findViewById(R.id.first_name_layout);
        password_name_layout = findViewById(R.id.last_name_layout);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loginTime = sdf.format(new Date());

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = username.getText().toString().trim();
                passWord = password.getText().toString().trim();

                if (validation()) {
                    if(userName.equalsIgnoreCase("admin") && passWord != null) {
                        checkEventCode();
                    }else{
                        checkParkCode();
                    }
                }
            }
        });
        CustomTextView greetingtext =  findViewById(R.id.greetingtext);
        String greeting = "<font color=#FF5D6791>" + "LC " + "</font>" + "<font color=#3B3C43>" + "Services " + "</font>" ;
        //html set based on the version wise
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // for 24 api and more
            greetingtext.setText(Html.fromHtml(greeting, 0));
        } else {
            greetingtext.setText(Html.fromHtml(greeting));
        }
    }


    private void checkEventCode()
    {
        if (Utils.isNetConnected(this))
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.LOGIN),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("response", response);

                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString(Constants.RESULT_STATUS);
                                String reportStatus = jsonObject.getString(Constants.REPORT_STATUS);
                                userType = jsonObject.getString("userType");
                                country = jsonObject.getString("country");
                                eventID = jsonObject.getString("eventID");

                                Log.e("userType","0"+userType);
                                Log.e("result","1"+result);

                                if (userType.equals("vendor") && result.equals(Constants.TRUE))
                                {

                                    Prefs.putString(Prefs.COUNTRY, country);
                                    Prefs.putBoolean(Prefs.LOGGED_IN, true);
                                    Prefs.putString("eventID",eventID);
                                    Prefs.putString("userType",userType);
                                    startActivity(new Intent(CpLoginActivity.this, CpDepartmentActivity.class));
                                    finish();

                                } else if(userType.equals("admin") && result.equals(Constants.TRUE))
                                {
                                    eventID = "NA";
                                    Prefs.putString(Prefs.COUNTRY, country);
                                    Prefs.putBoolean(Prefs.LOGGED_IN, true);
                                    Prefs.putString("eventID",eventID);
                                    Prefs.putString("userType",userType);
                                    startActivity(new Intent(CpLoginActivity.this, CommonAdmin.class));
                                    finish();
                                    Log.e("result", "" + result);
                                } else {
                                    CommonUtils.showToastMessage(CpLoginActivity.this, reportStatus);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(CpLoginActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(CpLoginActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userName);
                    params.put("password", passWord);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else
            Utils.showDialog(this);
    }

    //check for park
    private void checkParkCode() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                HttpStack stack = null;
                try {
                    stack = new HurlStack(null, new TLSSocketFactory());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                    Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                    stack = new HurlStack();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                    stack = new HurlStack();
                }
                requestQueue = Volley.newRequestQueue(CpLoginActivity.this, stack);
            } else {
                requestQueue = Volley.newRequestQueue(CpLoginActivity.this);
            }


            //RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                Log.e("Map", "" + response);
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("result");

                                Log.e("result ", "" + result);

                                if (result.equals("true")) {

                                    String userID = jsonObject.getString("userID");
                                    int rowID = jsonObject.getInt("rowID");
                                    SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = pref.edit();
                                    editor2.putString("userID",userID);
                                    Log.e("userID==",userID);
                                    editor2.putString("rowID",String.valueOf(rowID));
                                    editor2.apply();
                                    PrefController.savePrefs("calledJson",1,CpLoginActivity.this);
                                    startActivity(new Intent(CpLoginActivity.this, DepartmentActivity.class));
                                    PrefController.savePrefs("login","success",CpLoginActivity.this);
                                    finish();

                                } else {
                                    Toast.makeText(CpLoginActivity.this, "Invalid credential", Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  Toast.makeText(MapStationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.e("error", "" + error);
                            progressDialog.dismiss();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userName", userName);
                    params.put("password", passWord);
                    params.put("loginTime",loginTime);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else {
            Utils.showDialog(this);
        }
    }
    boolean validation() {
        if (CommonUtils.isNotNull(username.getText().toString())) {
            user_name_layout.setError(getString(R.string.please_enter_first_name));
            return false;
        } else {
            user_name_layout.setErrorEnabled(false);
        }
        if (CommonUtils.isNotNull(password.getText().toString())) {
            password_name_layout.setError(getString(R.string.please_enter_last_name));
            return false;
        } else {
            password_name_layout.setErrorEnabled(false);
        }

        return true;
    }


    private boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int phoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (phoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera ,Call  Storage Service Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            //Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                //.setTitle("Really Exit?")
                .setMessage("Exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //CpLoginActivity.super.onBackPressed();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                }).create().show();
    }

}