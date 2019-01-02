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
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.adapter.ParkAdapter;
import com.cykulapps.lcservices.helper.GridSpacingItemDecoration;
import com.cykulapps.lcservices.helper.ParksScannerActivity;
import com.cykulapps.lcservices.model.EventModel;
import com.cykulapps.lcservices.model.SubActivityModel;
import com.cykulapps.lcservices.overrideFonts;
import com.cykulapps.lcservices.utils.Utils;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubActivities extends AppCompatActivity implements ParkAdapter.ItemListener{
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SubActivityModel subActivityModel;
    ArrayList<SubActivityModel> subActivityModelArrayList;
    Intent intent;
    String eventID,departID;
    RequestQueue requestQueue;
    ArrayList<EventModel> eventModelArrayList;
    Context context;
    TextView tv_appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_activities);
        context = this;
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewsubactivity);
        subActivityModelArrayList = new ArrayList<>();
        eventModelArrayList = new ArrayList<>();
        intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        departID = intent.getStringExtra("departID");
        createRecyclerView();
        fetchDepartments();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String appName = sharedPreferences.getString("category", null);
        tv_appName = (TextView)findViewById(R.id.appName);
        tv_appName.setText(appName);


    }

    private void updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchDepartments() {
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
                            try {
                                progressDialog.dismiss();
                                Log.e("Response", response);
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
                                ParkAdapter parkAdapter = new ParkAdapter(eventModelArrayList, SubActivities.this, SubActivities.this);

                                int spanCount = 2;
                                int spacing = getResources().getDimensionPixelOffset(R.dimen._12sdp);
                                boolean includeEdge = true;
                                GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
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
                                Toast.makeText(SubActivities.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(SubActivities.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError) {
                                Toast.makeText(SubActivities.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("eventID", eventID);
                    params.put("departID", departID);
                    Log.e("EventID", eventID);
                    Log.e("Department ID", departID);
                    return params;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(this);
        }
    }

    private void createRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    @Override
    public void onItemClick(String eventID,String departID, String category, String deptName) {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref.edit();
        editor2.putString("eventID",eventID);
        editor2.putString("departID",departID);
        editor2.putString("category",deptName);
        editor2.apply();
        if (category.equals("Scan")){
            startActivity(new Intent(this, ParksScannerActivity.class).putExtra("eventID",eventID).putExtra("departID", departID));
        }
    }

}
