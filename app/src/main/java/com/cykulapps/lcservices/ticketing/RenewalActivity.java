package com.cykulapps.lcservices.ticketing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.cykulapps.lcservices.activities.SubDepartmentActivity;
import com.cykulapps.lcservices.overrideFonts;
import com.cykulapps.lcservices.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RenewalActivity extends AppCompatActivity {

    RadioButton r_payment,r_cash;
    RequestQueue requestQueue;
    Context context;
    EditText et_own, et_rent, et_kids, et_activity, et_card;
    TextView tv_total;
    Button btn_submit;
    RadioGroup radioMode;
    String eventID,deptID,adminID;
    String ownCycle,RentalCycle,kidsCycle,sactivity;
    TextView tv_own_price, tv_own_total,tv_rent_price, tv_rent_total,tv_spe_price, tv_spe_total,tv_kids_price, tv_kids_total,tv_activity_price, tv_activity_total,tv_rock_price, tv_rock_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewal);

        context = this;
        requestQueue = Volley.newRequestQueue(context);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");

        tv_own_price = (TextView)findViewById(R.id.tv_own_price);
        tv_own_total = (TextView)findViewById(R.id.tv_own_total);

        tv_rent_price = (TextView)findViewById(R.id.tv_rent_price);
        tv_rent_total = (TextView)findViewById(R.id.tv_rent_total);

        tv_kids_price = (TextView)findViewById(R.id.tv_kids_price);
        tv_kids_total = (TextView)findViewById(R.id.tv_kids_total);

        tv_activity_price = (TextView)findViewById(R.id.tv_activity_price);
        tv_activity_total = (TextView)findViewById(R.id.tv_activity_total);

        et_own = (EditText)findViewById(R.id.et_own);
        et_rent = (EditText)findViewById(R.id.et_rent);
        et_kids = (EditText)findViewById(R.id.et_kids);
        et_activity = (EditText)findViewById(R.id.et_activity);
        et_card = (EditText)findViewById(R.id.et_card);
        tv_total = (TextView) findViewById(R.id.et_total);
        r_payment=(RadioButton)findViewById(R.id.radio_payment);
        r_cash=(RadioButton)findViewById(R.id.radio_cash);
        btn_submit = (Button)findViewById(R.id.btn_renewal);
        radioMode = (RadioGroup)findViewById(R.id.radio_mode);
        tv_total.setText("0.0");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences.getString("eventID", null);
        deptID = sharedPreferences.getString("departID", null);
        adminID = sharedPreferences.getString("userID", null);

        if (type.equalsIgnoreCase("annual")){
            SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefAnnual", MODE_PRIVATE);
            ownCycle = sharedPreferences1.getString("ownCycle", null);
            RentalCycle = sharedPreferences1.getString("RentalCycle", null);
            kidsCycle = sharedPreferences1.getString("kidsCycle", null);
            sactivity = sharedPreferences1.getString("activity", null);

        }else {
            SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefMonth", MODE_PRIVATE);
            ownCycle = sharedPreferences1.getString("ownCycle", null);
            RentalCycle = sharedPreferences1.getString("RentalCycle", null);
            kidsCycle = sharedPreferences1.getString("kidsCycle", null);
            sactivity = sharedPreferences1.getString("activity", null);

        }
        final String ownPrice = "Rs. "+ownCycle+" x";
        tv_own_price.setText(ownPrice);

        final String rentPrice = "Rs. "+RentalCycle+" x";
        tv_rent_price.setText(rentPrice);

        final String kidsPrice = "Rs. "+kidsCycle+" x";
        tv_kids_price.setText(kidsPrice);

        final String activityPrice = "Rs. "+sactivity+" x";
        tv_activity_price.setText(activityPrice);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();

                String cardNum = et_card.getText().toString();
                if (cardNum.length()==0){
                    et_card.requestFocus();
                    et_card.setError("Please enter card number");
                }else if (cardNum.length()<5){
                    et_card.requestFocus();
                    et_card.setError("Card Number should be minimum 6 digits");
                }else {
                    int down,drent, dkids,dactivity;

                    if (own.length()==0){
                        down = 0;
                    }else {
                        down = Integer.parseInt(own);
                    }

                    if (rent.length()==0){
                        drent = 0;
                    }else {
                        drent = Integer.parseInt(rent);

                    }
                    if (kids.length()==0){
                        dkids = 0;
                    }else {
                        dkids = Integer.parseInt(kids);

                    }
                    if (activity.length()==0){
                        dactivity = 0;
                    }else {
                        dactivity = Integer.parseInt(activity);

                    }

                    int totalCount = down+drent+dkids+dactivity;
                    Log.e("Total Count", totalCount+"");
                    if (radioMode.getCheckedRadioButtonId() == -1)
                    {
                        Toast.makeText(context, "Please select payment mode", Toast.LENGTH_SHORT).show();
                    }else {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String curTime = sdf.format(new Date());
                        String totalAmount = tv_total.getText().toString();
                        int selectedId = radioMode .getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton)findViewById(selectedId);

                        String radioType = radioButton.getText().toString();
                        sendDataToServer(down,drent, dkids,dactivity,totalCount,totalAmount,curTime,radioType, cardNum, type);
                    }
                }
            }
        });

        et_own.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();

                double dtotal = calculatePrice(own,rent, kids, activity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (own.length()!=0){
                    double own_total = Integer.parseInt(ownCycle)*Integer.parseInt(own);
                    String total = " = "+String.valueOf(own_total);
                    tv_own_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_own_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_rent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();

                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();

                double dtotal = calculatePrice(own,rent, kids, activity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);


                if (rent.length()!=0){
                    double own_total = Integer.parseInt(RentalCycle)*Integer.parseInt(rent);
                    String total = " = "+String.valueOf(own_total);
                    tv_rent_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_rent_total.setText(total);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        et_activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();

                double dtotal = calculatePrice(own,rent, kids, activity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (activity.length()!=0){
                    double own_total = Integer.parseInt(sactivity)*Integer.parseInt(activity);
                    String total = " = "+String.valueOf(own_total);
                    tv_activity_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_activity_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_kids.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();
                double dtotal = calculatePrice(own,rent, kids, activity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (kids.length()!=0){
                    double own_total = Integer.parseInt(kidsCycle)*Integer.parseInt(kids);
                    String total = " = "+String.valueOf(own_total);
                    tv_kids_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_kids_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendDataToServer(final int down, final int drent, final int dkids, final int dactivity, final int totalCount, final String totalAmount, final String curTime, final String radioType, final String cardNum, final String type) {
        if (Utils.isNetConnected(context)) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RENEWAL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                Log.e("Response", response);
                                JSONObject jsonObject = new JSONObject(response);
                                String resultStatus = jsonObject.getString("resultStatus");
                                String reportStatus = jsonObject.getString("reportStatus");
                                if (resultStatus.equalsIgnoreCase("True")){
                                    Toast.makeText(context, reportStatus, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SubDepartmentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(context, reportStatus, Toast.LENGTH_SHORT).show();
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
                    map.put("adminID",adminID);
                    map.put("ownCycle",String.valueOf(down));
                    map.put("rentCycle",String.valueOf(drent));
                    map.put("specialCycle",String.valueOf(0));
                    map.put("kidsCycle",String.valueOf(dkids));
                    map.put("walkRun",String.valueOf(dactivity));
                    map.put("rockClimbing",String.valueOf(0));
                    map.put("paymentmode",radioType);
                    map.put("amount",totalAmount);
                    map.put("currentDate",curTime);
                    map.put("registerType",type);
                    map.put("count",String.valueOf(totalCount));
                    map.put("cardNum", cardNum);
                    map.put("eventID", eventID);
                    map.put("deptID", deptID);
                    map.put("type", type);

                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(context);
        }

    }


    private double calculatePrice(String own,String rent, String kids, String activity) {
        Double down,drent, dkids,dactivity;

        int aown = Integer.parseInt(ownCycle);
        int arent = Integer.parseInt(RentalCycle);
        int akids = Integer.parseInt(kidsCycle);
        int act = Integer.parseInt(sactivity);

        if (own.length()==0){
            down = 0.0;
        }else {
            down = Double.parseDouble(own);
            down = down*aown;
            Log.e("Down", down+"");
        }

        if (rent.length()==0){
            drent = 0.0;
        }else {
            drent = Double.parseDouble(rent);
            drent = drent*arent;
        }

        if (kids.length()==0){
            dkids = 0.0;
        }else {
            dkids = Double.parseDouble(kids);
            dkids = dkids*akids;
        }
        if (activity.length()==0){
            dactivity = 0.0;
        }else {
            dactivity = Double.parseDouble(activity);
            dactivity = dactivity*act;
        }

        return down+drent+dkids+dactivity;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
