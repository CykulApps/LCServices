package com.cykulapps.lcservices.individualtask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cykulapps.lcservices.Config;
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.adapter.DasboardAdapter;
import com.cykulapps.lcservices.helper.GridSpacingItemDecoration;
import com.cykulapps.lcservices.model.DashboardModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;
    DashboardModel dashboardModel;
    ArrayList<DashboardModel> dashboardModelArrayList;
    DasboardAdapter dasboardAdapter;
    RecyclerView recyclerView;
    Intent intent;
    String department, departmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_dashboard_activity);
        intent = getIntent();
        department = intent.getStringExtra("departmentID");
        departmentName = intent.getStringExtra("departmentName");
        //setTitle(departmentName);


        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        dashboardModelArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.dashboardrecycler);


        /*Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);*/

        Log.e("vivek", "dash" + department);
        recyclerView.setLayoutManager(new GridLayoutManager(Dashboard.this, 2));
        int spanCount = 2;
        int spacing = getResources().getDimensionPixelOffset(R.dimen._12sdp);
        boolean includeEdge = true;
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        getDashboard();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void refreshItems() {
        // Load items
        // ...
        dashboardModelArrayList.clear();
        getDashboard();

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void getDashboard()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Dashboard,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);

                        try {
                            progressDialog.dismiss();
                            JSONObject root = new JSONObject(response);
                           int count= Integer.parseInt(root.getString("count"));
                            Log.e("root.length()","length"+root.length());
                            if (count > 0)
                            {
                                for (int i = 0; i < count; i++) {
                                    JSONObject rootObject = root.getJSONObject((i + 1) + "");
                                    String departmentID = rootObject.getString("departmentID");
                                    String departmentName = rootObject.getString("departmentName");
                                    String total = rootObject.getString("total");
                                    dashboardModel = new DashboardModel(departmentID, departmentName, total);
                                    dashboardModelArrayList.add(dashboardModel);

                                }


                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                dasboardAdapter = new DasboardAdapter(dashboardModelArrayList, Dashboard.this);
                                recyclerView.setAdapter(dasboardAdapter);

                            } else {
                                Toast.makeText(Dashboard.this, "NO Activity found", Toast.LENGTH_SHORT).show();
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
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(Dashboard.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Dashboard.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(Dashboard.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("eventID", department);


                // params.put(Constants.EVENTID, Prefs.getString(Prefs.EVENT_CODE, null));

                Log.e("sharath", "---->" + params);
                return params;

            }
        };
        requestQueue.add(stringRequest);
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
    }
}