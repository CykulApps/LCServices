package com.cykulapps.lcservices.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.PrefController;
import com.cykulapps.lcservices.adapter.EventAdapter;
import com.cykulapps.lcservices.login.CpLoginActivity;
import com.cykulapps.lcservices.model.EventModel;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.utils.TLSSocketFactory;
import com.cykulapps.lcservices.utils.Utils;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActivity extends AppCompatActivity implements EventAdapter.ItemListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    Context context;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<EventModel> eventModelArrayList;
    EventModel eventModel;
    RequestQueue requestQueue;
    private int CAMERA_PERMISSION_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        recyclerView = findViewById(R.id.recyclerView);
        this.context = this;
        eventModelArrayList = new ArrayList<>();
        //Picasso.with(DepartmentActivity.this).setLoggingEnabled(true);

        // createRecyclerView();

        if(isCameraAllowed())
        {
            fetch_rides();
        }else
        {
            requestCameraPermission();
        }


    }


    //We are calling this method to check the permission status
    private boolean isCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestCameraPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == CAMERA_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                fetch_rides();
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String logoutTime = sdf.format(new Date());
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                String id = sharedPreferences.getString("rowID", null);
                Log.e("rowID", id+"");
                sendData(logoutTime, id);

        }
        return true;
    }

    private void fetch_rides() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            updateAndroidSecurityProvider();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HOMESCREEN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            try {
                                progressDialog.dismiss();

                                JSONObject rootObject = new JSONObject(response);
                                JSONArray jsonArray = rootObject.getJSONArray("imageTiles");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.getJSONObject(i);
                                    String eventID = json.getString("eventID");
                                    String eventURL = json.getString("imageUrl");
                                    String departID = json.getString("departmentID");
                                    String category = json.getString("category");
                                    String deptName = json.getString("departmentName");
                                    EventModel eventModel = new EventModel();
                                    eventModel.setDepartID(departID);
                                    eventModel.setEventID(eventID);
                                    eventModel.setEventUrl(eventURL);
                                    eventModel.setCategory(category);
                                    eventModel.setDeptName(deptName);
                                    eventModelArrayList.add(eventModel);
                                }
                                EventAdapter eventAdapter = new EventAdapter(eventModelArrayList, DepartmentActivity.this, DepartmentActivity.this);
                                recyclerView.setAdapter(eventAdapter);
                                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                                eventAdapter.notifyDataSetChanged();


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
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError) {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("status", "1");
                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(this);
        }
    }

    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onItemClick(String eventID,String departID, String category, String deptName) {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref.edit();
        editor2.putString("peventID", eventID);
        editor2.putString("pdepartID",departID);
        editor2.putString("eventID",eventID);
        editor2.putString("departID",departID);
        editor2.putString("category",deptName);
        editor2.apply();
        startActivity(new Intent(this, SubDepartmentActivity.class).putExtra("eventID",eventID).putExtra("departID", departID));
    }
    private void sendData(final String logoutTime, final String id) {
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
                requestQueue = Volley.newRequestQueue(DepartmentActivity.this, stack);
            } else {
                requestQueue = Volley.newRequestQueue(DepartmentActivity.this);
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
                                String reportStatus = jsonObject.getString("reportStatus");

                                Log.e("result ", "" + result);

                                if (result.equals("true")) {

                                    startActivity(new Intent(DepartmentActivity.this, CpLoginActivity.class));
                                    PrefController.savePrefs("login","fail",DepartmentActivity.this);
                                    finish();

                                } else {
                                    Toast.makeText(DepartmentActivity.this, reportStatus, Toast.LENGTH_SHORT).show();

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
                    params.put("logoutTime",logoutTime);
                    params.put("rowID", id);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else {
            Utils.showDialog(this);
        }
    }

}
