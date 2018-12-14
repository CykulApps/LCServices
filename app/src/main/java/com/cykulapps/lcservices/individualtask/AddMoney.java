package com.cykulapps.lcservices.individualtask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.cykulapps.lcservices.activities.CpDepartmentActivity;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMoney extends AppCompatActivity {
    EditText etBibNO, etamount;
    String bibno, amount, mode;
    ProgressDialog progressDialog;
    RadioButton cash, paytm, card;
    RadioGroup radioGroup;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_add_money);
        etBibNO = findViewById(R.id.bibno);
        etamount = findViewById(R.id.amount);
        cash = findViewById(R.id.radioButton1);
        paytm = findViewById(R.id.radioButton2);
        card = findViewById(R.id.radioButton3);
        radioGroup = findViewById(R.id.radioGroup);
        button = findViewById(R.id.btnSubmit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bibno = etBibNO.getText().toString().trim();
                amount = etamount.getText().toString().trim();

                if (!((bibno.isEmpty() || amount.isEmpty() || (radioGroup.getCheckedRadioButtonId() == -1)))) {
                    if (cash.isChecked())
                    {
                        mode = "CASH";

                    } else if(paytm.isChecked()) {
                        mode = "PAYTM";
                    }else
                    {
                        mode = "CARD";
                    }
                    addmoney();

                } else {
                    Toast.makeText(AddMoney.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addmoney() {
        if (Utils.isNetConnected(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.addmoney),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject rootObject = new JSONObject(response);
                                Log.e("dept", "" + response);
                                String result_status = rootObject.getString(Constants.RESULT_STATUS);
                                String report_status = rootObject.getString(Constants.REPORT_STATUS);
                                if (result_status.equals(Constants.TRUE)) {
                                    alertDialog(AddMoney.this, report_status);


                                } else {
                                    Toast.makeText(AddMoney.this, report_status, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddMoney.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(AddMoney.this, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("bibno", bibno);
                    params.put("amount", amount);
                    params.put("mode", mode);
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        } else
            Utils.showDialog(this);
    }

    public void alertDialog(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(AddMoney.this, CpDepartmentActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                });
               /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                }*/

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("CP Services");
        alert.show();
    }
}
