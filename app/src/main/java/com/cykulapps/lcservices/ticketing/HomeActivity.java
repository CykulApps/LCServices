package com.cykulapps.lcservices.ticketing;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.cykulapps.lcservices.overrideFonts;
import com.cykulapps.lcservices.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager simpleViewPager;
    ProgressDialog progressDialog;
    String eventID;
    Context context;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        context = this;
        requestQueue = Volley.newRequestQueue(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(this, getWindow().getDecorView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        simpleViewPager = (ViewPager) findViewById(R.id.simpleView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String eventID = sharedPreferences.getString("peventID", "");
      //  getPrices(eventID);

        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("One Time");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Monthly User");// set the Text for the second Tab
        tabLayout.addTab(secondTab);

        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Annual User");// set the Text for the second Tab
        tabLayout.addTab(thirdTab);




        TicketingPagerAdapter adapter = new TicketingPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);

        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {


                simpleViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

         public void getPrices(final String eventID){
            if (Utils.isNetConnected(context)) {
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
                                Log.e("Response", response);
                                JSONObject jsonObject = new JSONObject(response);

                                SharedPreferences pref = context.getSharedPreferences("MyPrefAnnual", MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = pref.edit();

                                SharedPreferences pref2 = context.getSharedPreferences("MyPrefMonth", MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = pref2.edit();

                                SharedPreferences pref3 = context.getSharedPreferences("MyPrefOne", MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = pref3.edit();

                                JSONObject oneTimeObject = jsonObject.getJSONObject("oneTime");
                                JSONObject monthlyObject = jsonObject.getJSONObject("monthly");
                                JSONObject annualObject = jsonObject.getJSONObject("annual");

                                String ownCycle = oneTimeObject.getString("ownCycle");
                                String RentalCycle = oneTimeObject.getString("RentalCycle");
                                String SpeCycle = oneTimeObject.getString("SpeCycle");
                                String kidsCycle = oneTimeObject.getString("kidsCycle");
                                String activity = oneTimeObject.getString("activity");
                                String rock = oneTimeObject.getString("rock");
                                String camera = oneTimeObject.getString("camera");
                                String video = oneTimeObject.getString("video");
                                editor3.putString("ownCycle",ownCycle);
                                editor3.putString("RentalCycle",RentalCycle);
                                editor3.putString("SpeCycle",SpeCycle);
                                editor3.putString("kidsCycle",kidsCycle);
                                editor3.putString("activity",activity);
                                editor3.putString("rock",rock);
                                editor3.putString("camera",camera);
                                editor3.putString("video",video);
                                editor3.apply();

                                String ownCycle1 = monthlyObject.getString("ownCycle");
                                String RentalCycle1 = monthlyObject.getString("RentalCycle");
                                String SpeCycle1 = monthlyObject.getString("SpeCycle");
                                String kidsCycle1 = monthlyObject.getString("kidsCycle");
                                String activity1 = monthlyObject.getString("activity");
                                String rock1 = monthlyObject.getString("rock");
                                String camera1 = monthlyObject.getString("camera");
                                String video1 = monthlyObject.getString("video");

                                editor2.putString("ownCycle",ownCycle1);
                                editor2.putString("RentalCycle",RentalCycle1);
                                editor2.putString("SpeCycle",SpeCycle1);
                                editor2.putString("kidsCycle",kidsCycle1);
                                editor2.putString("activity",activity1);
                                editor2.putString("rock",rock1);
                                editor2.putString("camera",camera1);
                                editor2.putString("video",video1);
                                editor2.apply();

                                String ownCycle2 = annualObject.getString("ownCycle");
                                String RentalCycle2 = annualObject.getString("RentalCycle");
                                String SpeCycle2 = annualObject.getString("SpeCycle");
                                String kidsCycle2 = annualObject.getString("kidsCycle");
                                String activity2 = annualObject.getString("activity");
                                String rock2 = annualObject.getString("rock");
                                String camera2 = annualObject.getString("camera");
                                String video2 = annualObject.getString("video");
                                String citizenWalk = annualObject.getString("citizenWalk");

                                Log.e("activity===",activity2);
                                editor1.putString("ownCycle",ownCycle2);
                                editor1.putString("RentalCycle",RentalCycle2);
                                editor1.putString("SpeCycle",SpeCycle2);
                                editor1.putString("kidsCycle",kidsCycle2);
                                editor1.putString("activity",activity2);
                                editor1.putString("rock",rock2);
                                editor1.putString("camera",camera2);
                                editor1.putString("video",video2);
                                editor1.putString("seniorcitizen",citizenWalk);
                                editor1.apply();

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
                    map.put("eventID", eventID);
                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(context);
        }
    }

    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(H.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
*/



}


