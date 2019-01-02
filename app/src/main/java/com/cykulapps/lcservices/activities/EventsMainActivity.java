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
import com.cykulapps.lcservices.adapter.EventsAdapter;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.helper.GridSpacingItemDecoration;
import com.cykulapps.lcservices.listeners.ParkItemListener;
import com.cykulapps.lcservices.login.LoginActivity;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsMainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private RecyclerView rcv_my_items;
    Intent intent;
    String eventID="", userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        rcv_my_items = findViewById(R.id.rc_view);
        intent = getIntent();
        eventID = Prefs.getString("eventID","");
        userType = Prefs.getString("userType","");

        userType=Prefs.getString("userType","");
        if(userType.equalsIgnoreCase("admin")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Log.e("eventID",""+eventID);
        Log.e("userType",""+userType);

        sentRequest_for_image();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_logout_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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


    private void sentRequest_for_image() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOAD_DEPT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject rootObject = new JSONObject(response);
                                Log.e("eventsMain", "" + response);
                                String result_status = rootObject.getString(Constants.RESULT_STATUS);
                                String report_status = rootObject.getString(Constants.REPORT_STATUS);
                                if (result_status.equals(Constants.TRUE))
                                {
                                    Gson gson = new GsonBuilder().create();
                                    List<com.cykulapps.lcservices.model.Response> imagesOfficeList = gson.fromJson(rootObject.getString("response"), new TypeToken<List<com.cykulapps.lcservices.model.Response>>() {
                                    }.getType());
                                    setDataToAdapter(imagesOfficeList);
                                } else {
                                    Toast.makeText(EventsMainActivity.this, report_status, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EventsMainActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(EventsMainActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("country", Prefs.getString(Prefs.COUNTRY, null));
                    params.put("eventID", eventID);
                    params.put("userType", userType);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
            DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);
            stringRequest.setShouldCache(false);
        } else
            Utils.showDialog(this);
    }

    private void setDataToAdapter(List<com.cykulapps.lcservices.model.Response> imagesOfficeList) {
        int spanCount = 2;
        int spacing = getResources().getDimensionPixelOffset(R.dimen._12sdp);
        boolean includeEdge = true;
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
        rcv_my_items.removeItemDecoration(itemDecoration);
        rcv_my_items.addItemDecoration(itemDecoration);

        rcv_my_items.setLayoutManager(new GridLayoutManager(this, 2));
        rcv_my_items.setAdapter(new EventsAdapter(EventsMainActivity.this, imagesOfficeList, new ParkItemListener() {
            @Override
            public void itemClickListener(int position, com.cykulapps.lcservices.model.Response response) {
                actionsBasedOnPosition(position, response);

            }
        }));
    }

    private void actionsBasedOnPosition(int position, com.cykulapps.lcservices.model.Response response)
    {
        String mDeptID = response.getDepartmentID();
        String category = response.getCategory();
        String departmentName = response.getDepartmentName();
        String alertMessage = response.getAlertMessage();
        Log.e("EventsMain", "" + response.toString());

        switch (category) {
//            case "goody":
//                startQRcode(mDeptID, alertMessage, departmentName);
//                break;
            case "events":
                Intent intent1 = new Intent(EventsMainActivity.this, EventsSubActivity.class);
                intent1.putExtra("departmentID", mDeptID);
                intent1.putExtra("departmentName",departmentName);
                Prefs.putString("eventSubDept",departmentName);
                Prefs.putString("eventSubDeptID",mDeptID);
                Log.e("mDeptID", "" + mDeptID);
                //this is to retrieve sub departments from DB
                startActivity(intent1);
                break;
        }
    }

    private void startQRcode(String depatmentId, String message, String title) {
        alertDialog(this, message, depatmentId, title, true);
    }

    public void alertDialog(final Context context, String message, final String depatmentId, final String title, final boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if (b) {
                            Intent in = new Intent(EventsMainActivity.this, QRCodeReader.class);
                            Bundle b = new Bundle();
                            b.putString("stuff", depatmentId);
                            in.putExtras(b);
                            startActivity(in);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }

    public void alertDialog2(final Context context, String message, final String depatmentId, final String title, final boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
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
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
        }else {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("Logout?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            if (Prefs.getBoolean(Prefs.LOGGED_IN_VENDEOR, false)) {
                                Prefs.logoutUser(getApplicationContext());
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}