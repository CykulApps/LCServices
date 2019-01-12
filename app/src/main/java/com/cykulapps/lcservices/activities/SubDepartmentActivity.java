package com.cykulapps.lcservices.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.PrefController;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.adapter.ParkAdapter;
import com.cykulapps.lcservices.helper.GridSpacingItemDecoration;
import com.cykulapps.lcservices.helper.ParksScannerActivity;
import com.cykulapps.lcservices.login.LoginActivity;
import com.cykulapps.lcservices.model.EventModel;
import com.cykulapps.lcservices.ticketing.HomeActivity;
import com.cykulapps.lcservices.utils.Utils;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubDepartmentActivity extends AppCompatActivity implements ParkAdapter.ItemListener{
    Context context;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<EventModel> eventModelArrayList;
    EventModel eventModel;
    RequestQueue requestQueue;
    private int CAMERA_PERMISSION_CODE=1;
    TextView tv_appName;
    String eventIDPrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_department);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        this.context = this;
        eventModelArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        eventIDPrent = sharedPreferences.getString("peventID", "");
        String departID = sharedPreferences.getString("pdepartID", "");
        String appName = sharedPreferences.getString("category", "");
        tv_appName = findViewById(R.id.appName);
        tv_appName.setText(appName);
        fetch_rides(departID);
    }
    private void fetch_rides(final String departID) {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            updateAndroidSecurityProvider();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ASSET,
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
                                ParkAdapter parkAdapter = new ParkAdapter(eventModelArrayList, SubDepartmentActivity.this, SubDepartmentActivity.this);

                                int spanCount = 2;
                                int spacing = getResources().getDimensionPixelOffset(R.dimen._12sdp);
                                GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(spanCount, spacing, true);
                                recyclerView.removeItemDecoration(itemDecoration);
                                recyclerView.addItemDecoration(itemDecoration);

                                recyclerView.setAdapter(parkAdapter);
                                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                                parkAdapter.notifyDataSetChanged();


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
                    map.put("eventID", eventIDPrent);
                    map.put("departID", departID);
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
        editor2.putString("eventID",eventID);
        editor2.putString("departID",departID);
        editor2.putString("category",deptName);
        editor2.apply();

        int calledDRCV = PrefController.loadPreferences("calledDRCV", 0, SubDepartmentActivity.this);
        int calledPPCP = PrefController.loadPreferences("calledPPCP", 0, SubDepartmentActivity.this);

        switch (category) {

            case "Ticketing":

                if (calledDRCV == 0 || calledPPCP == 0) {
                    Log.e("jsoncalled", "if");
                    getPrices(eventID, departID);

                } else {
                    Log.e("jsoncalled", "else");
                    Intent intent1 = new Intent(SubDepartmentActivity.this, HomeActivity.class);
                    startActivity(intent1);
                }
                break;

            case "Scan":
                startActivity(new Intent(this, ParksScannerActivity.class).putExtra("eventID", eventID).putExtra("departID", departID));
                break;

            case "Asset":
                startActivity(new Intent(this, SubActivities.class).putExtra("eventID", eventID).putExtra("departID", departID));
                break;

        }
    }


    public void getPrices(final String eventID,final String departID){
        if (Utils.isNetConnected(context)) {
            Log.e("called","called");
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PRICING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                Log.e("Price Response", response);
                                JSONObject jsonObject = new JSONObject(response);

                                if(jsonObject.getJSONObject("oneTime").getString("eventID").equalsIgnoreCase("DRCV")) {
                                    SharedPreferences pref = context.getSharedPreferences("MyPrefAnnualDRCV", MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = pref.edit();

                                    SharedPreferences pref2 = context.getSharedPreferences("MyPrefMonthDRCV", MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = pref2.edit();

                                    SharedPreferences pref3 = context.getSharedPreferences("MyPrefOneDRCV", MODE_PRIVATE);
                                    SharedPreferences.Editor editor3 = pref3.edit();

                                    JSONObject oneTimeObject = jsonObject.getJSONObject("oneTime");
                                    JSONObject monthlyObject = jsonObject.getJSONObject("monthly");
                                    JSONObject annualObject = jsonObject.getJSONObject("annual");

                                    String ownCycle = oneTimeObject.getString("ownCycle");
                                    String adultParkCycle = oneTimeObject.getString("RentalCycle"); //new
                                    String SpeCycle = oneTimeObject.getString("SpeCycle");
                                    String kidsCycle = oneTimeObject.getString("kidsCycle");
                                    String kidsWalk = oneTimeObject.getString("kidsWak");
                                    String activity = oneTimeObject.getString("activity");
                                    String rock = oneTimeObject.getString("rock");
                                    String camera = oneTimeObject.getString("camera");
                                    String video = oneTimeObject.getString("video");
                                    editor3.putString("ownCycle", ownCycle);
                                    editor3.putString("RentalCycle", adultParkCycle);
                                    editor3.putString("SpeCycle", SpeCycle);
                                    editor3.putString("kidsCycle", kidsCycle);
                                    editor3.putString("kidsWalk", kidsWalk);
                                    editor3.putString("activity", activity);
                                    editor3.putString("rock", rock);
                                    editor3.putString("camera", camera);
                                    editor3.putString("video", video);
                                    editor3.apply();

                                    String ownCycle1 = monthlyObject.getString("ownCycle");
                                    String RentalCycle1 = monthlyObject.getString("RentalCycle");
                                    String SpeCycle1 = monthlyObject.getString("SpeCycle");
                                    String kidsCycle1 = monthlyObject.getString("kidsCycle");
                                    String kidsWalk1 = monthlyObject.getString("kidsWak");
                                    String activity1 = monthlyObject.getString("activity");
                                    String rock1 = monthlyObject.getString("rock");
                                    String camera1 = monthlyObject.getString("camera");
                                    String video1 = monthlyObject.getString("video");

                                    editor2.putString("ownCycle", ownCycle1);
                                    editor2.putString("RentalCycle", RentalCycle1);
                                    editor2.putString("SpeCycle", SpeCycle1);
                                    editor2.putString("kidsCycle", kidsCycle1);
                                    editor2.putString("kidsWalk", kidsWalk1);
                                    editor2.putString("activity", activity1);
                                    editor2.putString("rock", rock1);
                                    editor2.putString("camera", camera1);
                                    editor2.putString("video", video1);
                                    editor2.apply();

                                    String ownCycle2 = annualObject.getString("ownCycle");
                                    String RentalCycle2 = annualObject.getString("RentalCycle");
                                    String SpeCycle2 = annualObject.getString("SpeCycle");
                                    String kidsCycle2 = annualObject.getString("kidsCycle");
                                    String kidsWalk2 = annualObject.getString("kidsWak");
                                    String activity2 = annualObject.getString("activity");
                                    String rock2 = annualObject.getString("rock");
                                    String camera2 = annualObject.getString("camera");
                                    String video2 = annualObject.getString("video");
                                    String citizenWalk = annualObject.getString("citizenWalk");

                                    Log.e("activity===", activity2);
                                    editor1.putString("ownCycle", ownCycle2);
                                    editor1.putString("RentalCycle", RentalCycle2);
                                    editor1.putString("SpeCycle", SpeCycle2);
                                    editor1.putString("kidsCycle", kidsCycle2);
                                    editor1.putString("kidsWalk", kidsWalk2);
                                    editor1.putString("activity", activity2);
                                    editor1.putString("rock", rock2);
                                    editor1.putString("camera", camera2);
                                    editor1.putString("video", video2);
                                    editor1.putString("seniorcitizen", citizenWalk);
                                    editor1.apply();

                                    PrefController.savePrefs("calledDRCV", 1, SubDepartmentActivity.this);
                                    Intent intent = new Intent(SubDepartmentActivity.this, HomeActivity.class);
                                    intent.putExtra("eventID", eventID);
                                    intent.putExtra("departID", departID);
                                    startActivity(intent);

                                }
                                else if(jsonObject.getJSONObject("oneTime").getString("eventID").equalsIgnoreCase("PPCP")){

                                    SharedPreferences pref = context.getSharedPreferences("MyPrefAnnualPPCP", MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = pref.edit();

                                    SharedPreferences pref2 = context.getSharedPreferences("MyPrefMonthPPCP", MODE_PRIVATE);
                                    SharedPreferences.Editor editor2 = pref2.edit();

                                    SharedPreferences pref3 = context.getSharedPreferences("MyPrefOnePPCP", MODE_PRIVATE);
                                    SharedPreferences.Editor editor3 = pref3.edit();

                                    JSONObject oneTimeObject = jsonObject.getJSONObject("oneTime");
                                    JSONObject monthlyObject = jsonObject.getJSONObject("monthly");
                                    JSONObject annualObject = jsonObject.getJSONObject("annual");

                                    String ownCycle = oneTimeObject.getString("ownCycle");
                                    String adultParkCycle = oneTimeObject.getString("RentalCycle");
                                    String SpeCycle = oneTimeObject.getString("SpeCycle");
                                    String kidsCycle = oneTimeObject.getString("kidsCycle");
                                    String kidsWalk = oneTimeObject.getString("kidsWak");
                                    String activity = oneTimeObject.getString("activity");
                                    String rock = oneTimeObject.getString("rock");
                                    String camera = oneTimeObject.getString("camera");
                                    String video = oneTimeObject.getString("video");
                                    editor3.putString("ownCycle", ownCycle);
                                    editor3.putString("RentalCycle", adultParkCycle);
                                    editor3.putString("SpeCycle", SpeCycle);
                                    editor3.putString("kidsCycle", kidsCycle);
                                    editor3.putString("kidsWalk", kidsWalk);
                                    editor3.putString("activity", activity);
                                    editor3.putString("rock", rock);
                                    editor3.putString("camera", camera);
                                    editor3.putString("video", video);
                                    editor3.apply();

                                    String ownCycle1 = monthlyObject.getString("ownCycle");
                                    String RentalCycle1 = monthlyObject.getString("RentalCycle");
                                    String SpeCycle1 = monthlyObject.getString("SpeCycle");
                                    String kidsCycle1 = monthlyObject.getString("kidsCycle");
                                    String kidsWalk1 = monthlyObject.getString("kidsWak");
                                    String activity1 = monthlyObject.getString("activity");
                                    String rock1 = monthlyObject.getString("rock");
                                    String camera1 = monthlyObject.getString("camera");
                                    String video1 = monthlyObject.getString("video");

                                    editor2.putString("ownCycle", ownCycle1);
                                    editor2.putString("RentalCycle", RentalCycle1);
                                    editor2.putString("SpeCycle", SpeCycle1);
                                    editor2.putString("kidsCycle", kidsCycle1);
                                    editor2.putString("kidsWalk", kidsWalk1);
                                    editor2.putString("activity", activity1);
                                    editor2.putString("rock", rock1);
                                    editor2.putString("camera", camera1);
                                    editor2.putString("video", video1);
                                    editor2.apply();

                                    String ownCycle2 = annualObject.getString("ownCycle");
                                    String RentalCycle2 = annualObject.getString("RentalCycle");
                                    String SpeCycle2 = annualObject.getString("SpeCycle");
                                    String kidsCycle2 = annualObject.getString("kidsCycle");
                                    String kidsWalk2 = annualObject.getString("kidsWak");
                                    String activity2 = annualObject.getString("activity");
                                    String rock2 = annualObject.getString("rock");
                                    String camera2 = annualObject.getString("camera");
                                    String video2 = annualObject.getString("video");
                                    String citizenWalk = annualObject.getString("citizenWalk");

                                    Log.e("activity===", activity2);
                                    editor1.putString("ownCycle", ownCycle2);
                                    editor1.putString("RentalCycle", RentalCycle2);
                                    editor1.putString("SpeCycle", SpeCycle2);
                                    editor1.putString("kidsCycle", kidsCycle2);
                                    editor1.putString("kidsWalk", kidsWalk2);
                                    editor1.putString("activity", activity2);
                                    editor1.putString("rock", rock2);
                                    editor1.putString("camera", camera2);
                                    editor1.putString("video", video2);
                                    editor1.putString("seniorcitizen", citizenWalk);
                                    editor1.apply();

                                    PrefController.savePrefs("calledPPCP", 1, SubDepartmentActivity.this);
                                    Intent intent = new Intent(SubDepartmentActivity.this, HomeActivity.class);
                                    intent.putExtra("eventID", eventID);
                                    intent.putExtra("departID", departID);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(context, "something went wrong from server side...", Toast.LENGTH_SHORT).show();
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
                            if (error instanceof NoConnectionError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError)
                            {
                                //TODO
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("eventID", eventIDPrent);
                    Log.e("Maps======",map.toString());
                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(context);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
