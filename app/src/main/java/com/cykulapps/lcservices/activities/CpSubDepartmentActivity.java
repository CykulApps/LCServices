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
import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.adapter.HomeItemsAdapter;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.individualtask.AddMoney;
import com.cykulapps.lcservices.individualtask.CheckBalance;
import com.cykulapps.lcservices.individualtask.Dashboard;
import com.cykulapps.lcservices.listeners.HomeItemListener;
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

public class CpSubDepartmentActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private RecyclerView rcv_my_items;
    private String department, mDeptID,departmentName;
    String userType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_sub_department_activity);
        Intent intent = getIntent();
        department = intent.getStringExtra("departmentID");
        departmentName = intent.getStringExtra("departmentName");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        setTitle(departmentName);

        Log.e("vivek","sub"+department);
        Log.e("departmentName","hey"+departmentName);
        //Log.e("subEvent",""+departmentID);
        rcv_my_items = (RecyclerView) findViewById(R.id.rc_view);
        userType=Prefs.getString("userType","");
        Log.e("userType","userType"+userType);

        sentRequest_for_image();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(userType.equals("admin"))
        {
            getMenuInflater().inflate(R.menu.cp_subactivity_menu, menu);//Menu Resource, Menu
            return true;

        }else
        {
            getMenuInflater().inflate(R.menu.cp_vendor_menu, menu);//Menu Resource, Menu
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.dashboard:
                startActivity(new Intent(this, Dashboard.class).putExtra("departmentID",department).putExtra("departmentName",departmentName));
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

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;

    }


    private void actionsBasedOnPosition(int position, com.cykulapps.lcservices.model.Response response)
    {
        mDeptID = response.getDepartmentID();
        String category = response.getCategory();
        String departmentName = response.getDepartmentName();
        String alertMessage = response.getAlertMessage();
        Log.e("subdept",""+response.toString());

        switch(category)
        {
            case "qrcode":
                startQRcode(mDeptID, alertMessage, departmentName);
                break;


        }
    }

    private void sentRequest_for_image() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.SUBDEPARTMENTS),
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
                                    List<com.cykulapps.lcservices.model.Response> imagesOfficeList = gson.fromJson(rootObject.getString("response"), new TypeToken<List<com.cykulapps.lcservices.model.Response>>() {
                                    }.getType());
                                    setDataToAdapter(imagesOfficeList);
                                } else {
                                    alertDialog1(CpSubDepartmentActivity.this,report_status,"Just Engage");
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
                                Toast.makeText(CpSubDepartmentActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(CpSubDepartmentActivity.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("departmentID",department);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else
            Utils.showDialog(this);
    }

    private void setDataToAdapter(List<com.cykulapps.lcservices.model.Response> imagesOfficeList) {
        rcv_my_items.setLayoutManager(new GridLayoutManager(this, 2));
        rcv_my_items.setAdapter(new HomeItemsAdapter(CpSubDepartmentActivity.this, imagesOfficeList, new HomeItemListener() {
            @Override
            public void itemClickListener(int position, com.cykulapps.lcservices.model.Response response) {
                actionsBasedOnPosition(position, response);
            }
        }));
    }

    private void startQRcode(String depatmentId, String message, String title)
    {

        alertDialog(this, message, depatmentId, title, true);
    }

    public void alertDialog(final Context context, String message, final String departmentID, final String title, final boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (b) {
                            Intent in = new Intent(CpSubDepartmentActivity.this, QRCodeReader1.class);
                            Prefs.putString("eventID",department);
                            Prefs.putString("departmentID",mDeptID);
                            Log.e("keystoreader",""+departmentID+"next"+mDeptID);

//                            Bundle b = new Bundle();
//                            b.putString("eventID", departmentID);
//                            b.putString("departmentID",mDeptID);
//                            in.putExtras(b);
                            startActivity(in);
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                })*/;

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }

    public void alertDialog1(final Context context, String message, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        CpSubDepartmentActivity.this.finish();

                    }
                })
                /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                })*/;

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }
    public void alertDialog2(final Context context, String message, final String depatmentId, final String title, final boolean b)
    {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}