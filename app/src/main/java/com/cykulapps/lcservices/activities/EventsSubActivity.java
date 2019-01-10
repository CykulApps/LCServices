package com.cykulapps.lcservices.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.cykulapps.lcservices.adapter.EventsAdapter;
import com.cykulapps.lcservices.adapter.EventsSubAdapter;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.helper.EventsScannerActivity;
import com.cykulapps.lcservices.helper.GridSpacingItemDecoration;
import com.cykulapps.lcservices.individualtask.AddMoney;
import com.cykulapps.lcservices.individualtask.CheckBalance;
import com.cykulapps.lcservices.individualtask.Dashboard;
import com.cykulapps.lcservices.listeners.ParkItemListener;
import com.cykulapps.lcservices.login.LoginActivity;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsSubActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private String departmentID, mDeptID,departmentName,eventID,alertMessage,category;
    String userType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_sub_activity);
        Intent intent = getIntent();
        departmentID = Prefs.getString("eventSubDeptID","");
        departmentName = Prefs.getString("eventSubDept","");
        Log.e("departmetnId",departmentID+"");
        userType=Prefs.getString("userType","");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(userType.equalsIgnoreCase("admin") || userType.equalsIgnoreCase("tester")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        getSupportActionBar().setElevation(0);
        setTitle(departmentName);

        Log.e("vivek","sub"+departmentID);
        Log.e("departmentName","hey"+departmentName);

        recyclerView = findViewById(R.id.rc_view);

        Log.e("userType","userType"+userType);

        sentRequest_for_image();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(userType.equalsIgnoreCase("admin"))
        {
            getMenuInflater().inflate(R.menu.events_admin_menu, menu);//Menu Resource, Menu
            return true;

        }else if(userType.equalsIgnoreCase("tester"))
        {
            getMenuInflater().inflate(R.menu.events_tester_menu, menu);//Menu Resource, Menu
            return true;
        }else{
            getMenuInflater().inflate(R.menu.events_vendor_menu, menu);//Menu Resource, Menu
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.dashboard:
                startActivity(new Intent(this, Dashboard.class).putExtra("departmentID",departmentID).putExtra("departmentName",departmentName));
               // finish();
                break;
            case R.id.wallet:
                startActivity(new Intent(this, AddMoney.class));
               // finish();
                break;

            case R.id.checkBal:
                startActivity(new Intent(this, CheckBalance.class));
                // finish();
                break;

            case R.id.logout:
                Prefs.logoutUser(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;

    }


    private void actionsBasedOnPosition(int position, com.cykulapps.lcservices.model.Response response)
    {
        mDeptID = response.getDepartmentID();
        category = response.getCategory();
        departmentName = response.getDepartmentName();
        alertMessage = response.getAlertMessage();
        eventID = response.getEventID();

        Log.e("subdept",""+response.toString());

        switch(category)
        {
            case "qrcode":
                startQRcode(mDeptID, alertMessage, departmentName,eventID);
                break;
        }
    }

    private void sentRequest_for_image() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SUB_DEPT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.v("Response==>",response);
                                progressDialog.dismiss();
                                JSONObject rootObject = new JSONObject(response);
                                Log.e("subdept",""+response);
                                String result_status = rootObject.getString(Constants.RESULT_STATUS);
                                String report_status = rootObject.getString(Constants.REPORT_STATUS);
                                if (result_status.equals(Constants.TRUE)) {
                                    Gson gson = new GsonBuilder().create();
                                    List<com.cykulapps.lcservices.model.Response> responseList = gson.fromJson(rootObject.getString("response"), new TypeToken<List<com.cykulapps.lcservices.model.Response>>() {
                                    }.getType());
                                    setDataToAdapter(responseList);
                                } else {
                                    alertDialog1(EventsSubActivity.this,report_status,"Just Engage");
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
                                Toast.makeText(EventsSubActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(EventsSubActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("departmentID",departmentID);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else
            Utils.showDialog(this);
    }

    private void setDataToAdapter(List<com.cykulapps.lcservices.model.Response> imagesOfficeList) {

        int spanCount = 2;
        int spacing = getResources().getDimensionPixelOffset(R.dimen._12sdp);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(spanCount, spacing, true);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if(departmentID.equalsIgnoreCase("SR2019") || departmentID.equalsIgnoreCase("JR2019")) {
            recyclerView.setAdapter(new EventsAdapter(EventsSubActivity.this, imagesOfficeList, new ParkItemListener() {
                @Override
                public void itemClickListener(int position, com.cykulapps.lcservices.model.Response response) {
                    actionsBasedOnPosition(position, response);
                }
            }));
        }else {
            recyclerView.setAdapter(new EventsSubAdapter(EventsSubActivity.this, imagesOfficeList, new ParkItemListener() {
                @Override
                public void itemClickListener(int position, com.cykulapps.lcservices.model.Response response) {
                    actionsBasedOnPosition(position, response);
                }
            }));
        }
    }

    private void startQRcode(String depatmentId, String message, String deptName,String eventID)
    {
        alertDialog(this, message, depatmentId, deptName, eventID,true);
    }

    public void alertDialog(final Context context, String message, final String departmentID, final String deptName,final String eventID, final boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (b) {
                            Intent in = new Intent(EventsSubActivity.this, EventsScannerActivity.class);
                            Prefs.putString("deptName",departmentName);
                            Prefs.putString("eventID",eventID);
                            Prefs.putString("departmentID",mDeptID);

                            Log.e("keystoreader",""+departmentID+"next"+mDeptID);
                            startActivity(in);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(deptName);
        alert.show();
    }

    public void alertDialog1(final Context context, String message, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        EventsSubActivity.this.finish();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }

    @Override
    public void onBackPressed() {

        if(Prefs.getBoolean(Prefs.LOGGED_IN,false)){
            startActivity(new Intent(getApplicationContext(), EventsMainActivity.class));

        }else if(Prefs.getBoolean(Prefs.LOGGED_IN_VENDEOR,false))
        {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}