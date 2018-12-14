package com.cykulapps.lcservices.ticketing;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnualFragment extends Fragment {
    EditText et_fname, et_lname, et_mob;

    RadioButton r_payment, r_cash;
    RequestQueue requestQueue;
    Context context;
    EditText et_own, et_rent, et_kids, et_activity, et_card, et_seniorcitizen;
    TextView tv_total;
    Button btn_submit;
    RadioGroup radioMode;
    ImageView annual_card_image;
    String eventID, deptID, adminID;
    TextView tv_renewal, tv_seniorcitzen_price, tv_citizen_total;
    TextView tv_own_price, tv_own_total, tv_rent_price, tv_rent_total, tv_spe_price, tv_spe_total, tv_kids_price, tv_kids_total, tv_activity_price, tv_activity_total, tv_rock_price, tv_rock_total;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_annual, container, false);
        context = getActivity();
        requestQueue = Volley.newRequestQueue(context);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(getActivity(), root);
        et_fname = (EditText) root.findViewById(R.id.et_fname);
        et_lname = (EditText) root.findViewById(R.id.et_lname);
        et_mob = (EditText) root.findViewById(R.id.et_mob);
        et_seniorcitizen = (EditText) root.findViewById(R.id.et_seniorcitizen);

        tv_renewal = (TextView) root.findViewById(R.id.tv_renewal);
        annual_card_image = (ImageView) root.findViewById(R.id.annual_card_image);
        annual_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CardScanActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        tv_renewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RenewalActivity.class);
                intent.putExtra("type", "annual");
                startActivity(intent);
            }
        });


        et_own = (EditText) root.findViewById(R.id.et_own);
        et_rent = (EditText) root.findViewById(R.id.et_rent);
        et_kids = (EditText) root.findViewById(R.id.et_kids);
        et_activity = (EditText) root.findViewById(R.id.et_activity);
        et_card = (EditText) root.findViewById(R.id.et_card);
        tv_total = (TextView) root.findViewById(R.id.et_total);
        r_payment = (RadioButton) root.findViewById(R.id.radio_payment);
        r_cash = (RadioButton) root.findViewById(R.id.radio_cash);
        btn_submit = (Button) root.findViewById(R.id.btn_submit);
        radioMode = (RadioGroup) root.findViewById(R.id.radio_mode);

        tv_own_price = (TextView) root.findViewById(R.id.tv_own_price);
        tv_own_total = (TextView) root.findViewById(R.id.tv_own_total);

        tv_rent_price = (TextView) root.findViewById(R.id.tv_rent_price);
        tv_rent_total = (TextView) root.findViewById(R.id.tv_rent_total);

        tv_kids_price = (TextView) root.findViewById(R.id.tv_kids_price);
        tv_kids_total = (TextView) root.findViewById(R.id.tv_kids_total);

        tv_activity_price = (TextView) root.findViewById(R.id.tv_activity_price);
        tv_activity_total = (TextView) root.findViewById(R.id.tv_activity_total);

        tv_seniorcitzen_price = (TextView) root.findViewById(R.id.tv_seniorcitzen_price);
        tv_citizen_total = (TextView) root.findViewById(R.id.tv_citizen_total);

        tv_total.setText("0.0");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences.getString("eventID", null);
        deptID = sharedPreferences.getString("departID", null);
        adminID = sharedPreferences.getString("userID", null);
        String peventID = sharedPreferences.getString("peventID", null);


        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("MyPrefAnnual", MODE_PRIVATE);
        final String ownCycle = sharedPreferences1.getString("ownCycle", null);
        final String RentalCycle = sharedPreferences1.getString("RentalCycle", null);
        final String SpeCycle = sharedPreferences1.getString("SpeCycle", null);
        final String kidsCycle = sharedPreferences1.getString("kidsCycle", null);
        final String sactivity = sharedPreferences1.getString("activity", null);
        final String seniorcitizen = sharedPreferences1.getString("seniorcitizen", null);
        final String srock = sharedPreferences1.getString("rock", null);

        final String ownPrice = "Rs. " + ownCycle + " x";
        tv_own_price.setText(ownPrice);

        final String rentPrice = "Rs. " + RentalCycle + " x";
        tv_rent_price.setText(rentPrice);

        final String kidsPrice = "Rs. " + kidsCycle + " x";
        tv_kids_price.setText(kidsPrice);

        final String activityPrice = "Rs. " + sactivity + " x";
        tv_activity_price.setText(activityPrice);
        final String seniorcitizenPrice = "Rs. " + seniorcitizen + " x";
        tv_seniorcitzen_price.setText(seniorcitizenPrice);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String activity = et_activity.getText().toString();
                String seniorcitizen = et_seniorcitizen.getText().toString();
                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String mno = et_mob.getText().toString();
                String cardNum = et_card.getText().toString();
              /*  if (fname.length() == 0) {
                    et_fname.requestFocus();
                    et_fname.setError("Enter first name");
                }*/ if (mno.length() == 0) {
                    et_mob.requestFocus();
                    et_mob.setError("Enter mobile number");
                } else if (mno.length() < 10) {
                    et_mob.requestFocus();
                    et_mob.setError("Mobile Number should be 10 digits");
                } else if (cardNum.length() == 0) {
                    et_card.requestFocus();
                    et_card.setError("Please enter card number");
                } else if (cardNum.length() < 5) {
                    et_card.requestFocus();
                    et_card.setError("Card Number should be minimum 6 digits");
                } else {
                    int down, drent, dkids, dactivity, dseniorcitizen;

                    if (own.length() == 0) {
                        down = 0;
                    } else {
                        down = Integer.parseInt(own);
                    }

                    if (rent.length() == 0) {
                        drent = 0;
                    } else {
                        drent = Integer.parseInt(rent);

                    }
                    if (kids.length() == 0) {
                        dkids = 0;
                    } else {
                        dkids = Integer.parseInt(kids);

                    }
                    if (activity.length() == 0) {
                        dactivity = 0;
                    } else {
                        dactivity = Integer.parseInt(activity);
                    }
                    if (seniorcitizen.length() == 0) {
                        dseniorcitizen = 0;
                    } else {
                        dseniorcitizen = Integer.parseInt(seniorcitizen);
                    }

                    int totalCount = down + drent + dkids + dactivity + dseniorcitizen;
                    Log.e("Total Count==", totalCount + "");
                    if (radioMode.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getActivity(), "Please select payment mode", Toast.LENGTH_SHORT).show();
                    } else {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String curTime = sdf.format(new Date());
                        String totalAmount = tv_total.getText().toString();
                        int selectedId = radioMode.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) root.findViewById(selectedId);

                        String radioType = radioButton.getText().toString();
                        sendDataToServer(fname, lname, mno, down, drent, dkids, dactivity, dseniorcitizen, totalCount, totalAmount, curTime, radioType, cardNum);
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
                String citizenActivity = et_seniorcitizen.getText().toString();

                double dtotal = calculatePrice(own, rent, kids, activity, citizenActivity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (own.length() != 0) {
                    double own_total = Integer.parseInt(ownCycle) * Integer.parseInt(own);
                    String total = " = " + String.valueOf(own_total);
                    tv_own_total.setText(total);
                } else {
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
                String citizenActivity = et_seniorcitizen.getText().toString();

                double dtotal = calculatePrice(own, rent, kids, activity, citizenActivity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);


                if (rent.length() != 0) {
                    double own_total = Integer.parseInt(RentalCycle) * Integer.parseInt(rent);
                    String total = " = " + String.valueOf(own_total);
                    tv_rent_total.setText(total);
                } else {
                    String total = " = 0.00";
                    tv_rent_total.setText(total);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                String citizenActivity = et_seniorcitizen.getText().toString();

                double dtotal = calculatePrice(own, rent, kids, activity, citizenActivity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (activity.length() != 0) {
                    double own_total = Integer.parseInt(sactivity) * Integer.parseInt(activity);
                    String total = " = " + String.valueOf(own_total);
                    tv_activity_total.setText(total);
                } else {
                    String total = " = 0.00";
                    tv_activity_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

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
                String citizenActivity = et_seniorcitizen.getText().toString();

                double dtotal = calculatePrice(own, rent, kids, activity, citizenActivity);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (activity.length() != 0) {
                    double own_total = Integer.parseInt(sactivity) * Integer.parseInt(activity);
                    String total = " = " + String.valueOf(own_total);
                    tv_activity_total.setText(total);
                } else {
                    String total = " = 0.00";
                    tv_activity_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_seniorcitizen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String rent = et_rent.getText().toString();
                String kids = et_kids.getText().toString();
                String citizenActivity = et_seniorcitizen.getText().toString();
                String activity = et_activity.getText().toString();
                double citizentotal = calculatePrice(own, rent, kids, activity, citizenActivity);
                String seniortotal = String.valueOf(citizentotal);
                tv_total.setText(seniortotal);

                if (citizenActivity.length() != 0) {
                    double citizen_total = Integer.parseInt(seniorcitizen) * Integer.parseInt(citizenActivity);
                    String total = " = " + String.valueOf(citizen_total);
                    tv_citizen_total.setText(total);
                } else {
                    String total = " = 0.00";
                    tv_citizen_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private void sendDataToServer(final String fname, final String lname, final String mno, final int down, final int drent, final int dkids, final int dactivity, final int dseniorcitizen, final int totalCount, final String totalAmount, final String curTime, final String radioType, final String cardNum) {
        if (Utils.isNetConnected(getActivity())) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                Log.e("Response", response);
                                JSONObject jsonObject = new JSONObject(response);
                                String resultStatus = jsonObject.getString("resultStatus");
                                String reportStatus = jsonObject.getString("reportStatus");
                                if (resultStatus.equalsIgnoreCase("True")) {
                                    Toast.makeText(context, reportStatus, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SubDepartmentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
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
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("adminID", adminID);
                    map.put("mobileNumber", mno);
                    map.put("firstName", fname);
                    map.put("lastName", lname);
                    map.put("ownCycle", String.valueOf(down));
                    map.put("rentCycle", String.valueOf(drent));
                    map.put("specialCycle", String.valueOf(0));
                    map.put("kidsCycle", String.valueOf(dkids));
                    map.put("walkRun", String.valueOf(dactivity));
                    map.put("rockClimbing", String.valueOf(0));
                    map.put("paymentmode", radioType);
                    map.put("amount", totalAmount);
                    map.put("currentDate", curTime);
                    map.put("registerType", "Annual");
                    map.put("count", String.valueOf(totalCount));
                    map.put("cardNum", cardNum);
                    map.put("eventID", eventID);
                    map.put("deptID", deptID);
                    map.put("citizenWalk", dseniorcitizen + "");
                    Log.e("MapsValues",map.toString());

                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(context);
        }

    }


    private double calculatePrice(String own, String rent, String kids, String activity, String citizenActivity) {
        Double down, drent, dkids, dactivity, dcitizen;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefAnnual", MODE_PRIVATE);
        String ownCycle = sharedPreferences.getString("ownCycle", null);
        String RentalCycle = sharedPreferences.getString("RentalCycle", null);
        String SpeCycle = sharedPreferences.getString("SpeCycle", null);
        String kidsCycle = sharedPreferences.getString("kidsCycle", null);
        String sactivity = sharedPreferences.getString("activity", null);
        String srock = sharedPreferences.getString("rock", null);
        final String seniorcitizen = sharedPreferences.getString("seniorcitizen", null);

        int aown = Integer.parseInt(ownCycle);
        int arent = Integer.parseInt(RentalCycle);
        int akids = Integer.parseInt(kidsCycle);
        int act = Integer.parseInt(sactivity);
        int cit = Integer.parseInt(seniorcitizen);

        if (own.length() == 0) {
            down = 0.0;
        } else {
            down = Double.parseDouble(own);
            down = down * aown;
            Log.e("Down", down + "");
        }

        if (rent.length() == 0) {
            drent = 0.0;
        } else {
            drent = Double.parseDouble(rent);
            drent = drent * arent;
        }

        if (kids.length() == 0) {
            dkids = 0.0;
        } else {
            dkids = Double.parseDouble(kids);
            dkids = dkids * akids;
        }
        if (activity.length() == 0) {
            dactivity = 0.0;
        } else {
            dactivity = Double.parseDouble(activity);
            dactivity = dactivity * act;
        }
        if (citizenActivity.length() == 0) {
            dcitizen = 0.0;
        } else {
            dcitizen = Double.parseDouble(citizenActivity);
            dcitizen = dcitizen * cit;
        }
        return down + drent + dkids + dactivity + dcitizen;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                et_card.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }
}
