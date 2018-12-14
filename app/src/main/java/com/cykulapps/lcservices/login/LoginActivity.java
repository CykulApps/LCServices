package com.cykulapps.lcservices.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.activities.DepartmentActivity;
import com.cykulapps.lcservices.overrideFonts;
import com.cykulapps.lcservices.utils.PrefController;
import com.cykulapps.lcservices.utils.TLSSocketFactory;
import com.cykulapps.lcservices.utils.Utils;

import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText etUser, etPass;
    String user, pass;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String loginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());

        etUser = findViewById(R.id.input_user);
        etPass = findViewById(R.id.input_password);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loginTime = sdf.format(new Date());

    }

    public void checkCredentials(View view) {
        user = etUser.getText().toString().trim();
        pass = etPass.getText().toString().trim();

        if (user.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            sendData();
        }
    }

    private void sendData() {
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
                requestQueue = Volley.newRequestQueue(LoginActivity.this, stack);
            } else {
                requestQueue = Volley.newRequestQueue(LoginActivity.this);
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
                                   PrefController.savePrefs("calledJson",1,LoginActivity.this);
                                    startActivity(new Intent(LoginActivity.this, DepartmentActivity.class));
                                    PrefController.savePrefs("login","success",LoginActivity.this);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid credential", Toast.LENGTH_SHORT).show();

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
                    params.put("userName", user);
                    params.put("password", pass);
                    params.put("loginTime",loginTime);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else {
            Utils.showDialog(this);
        }
    }

}