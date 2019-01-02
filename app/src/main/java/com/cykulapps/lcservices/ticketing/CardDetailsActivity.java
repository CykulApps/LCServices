package com.cykulapps.lcservices.ticketing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.Config;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.activities.SubDepartmentActivity;
import com.cykulapps.lcservices.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDetailsActivity extends AppCompatActivity {
    EditText et1, et2,et3,et4,et5,et6,et7,et8,et9,et10;
    ImageView image1, image2,image3,image4,image5,image6,image7,image8,image9,image10;
    TextView tv_name, tv_amount;
    Button btn_submit;
    Context context;
    String result;
    List<String> list = new ArrayList<>();
    RequestQueue requestQueue;
    StringBuilder builder;
    String eventID, deptID,adminID;
    int rCode=1,rCode2=2,rCode3=3,rCode4=4,rCode5=5,rCode6=6,rCode7=7,rCode8=8,rCode9=9,rCode10=10;
    //String rCode="1",rCode2="1",rCode3="3",rCode4="4",rCode5="5",rCode6="6",rCode7="7",rCode8="8",rCode9="9",rCode10="10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        context = this;
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        et4 = (EditText)findViewById(R.id.et4);
        et5 = (EditText)findViewById(R.id.et5);
        et6 = (EditText)findViewById(R.id.et6);
        et7 = (EditText)findViewById(R.id.et7);
        et8 = (EditText)findViewById(R.id.et8);
        et9 = (EditText)findViewById(R.id.et9);
        et10 = (EditText)findViewById(R.id.et10);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        image4 = (ImageView)findViewById(R.id.image4);
        image5 = (ImageView)findViewById(R.id.image5);
        image6 = (ImageView)findViewById(R.id.image6);
        image7 = (ImageView)findViewById(R.id.image7);
        image8 = (ImageView)findViewById(R.id.image8);
        image9 = (ImageView)findViewById(R.id.image9);
        image10 = (ImageView)findViewById(R.id.image10);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_amount = (TextView)findViewById(R.id.tv_amount);
        btn_submit = (Button)findViewById(R.id.btn_submit);


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode2);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode3);

            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);

            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);

            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);

            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);

            }
        });
        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);
            }
        });
        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);
            }
        });
        image10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                startActivityForResult(intent, rCode);
            }
        });
        builder = new StringBuilder();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences.getString("eventID", "");
        deptID = sharedPreferences.getString("departID", "");
        adminID = sharedPreferences.getString("userID", "");
        Log.e("adminID====",adminID);

        Intent intent = getIntent();
        final String fname = intent.getStringExtra("fname");
        final String lname = intent.getStringExtra("lname");
        final String mno = intent.getStringExtra("mno");
        final String ownCycle = intent.getStringExtra("ownCycle");
        final String rentCycle = intent.getStringExtra("rentCycle");
        final String speCycle = intent.getStringExtra("speCycle");
        final String kidsCycle = intent.getStringExtra("kidsCycle");
        final String activity = intent.getStringExtra("activity");
        final String rock = intent.getStringExtra("rock");
        final String totalCount = intent.getStringExtra("totalCount");
        final String totalAmount = intent.getStringExtra("totalAmount");
        final String curTime = intent.getStringExtra("curTime");
        final String radioType = intent.getStringExtra("radioType");
        final String camera = intent.getStringExtra("camera");
        final String video = intent.getStringExtra("video");


        final int count = Integer.parseInt(totalCount);
        tv_name.setText(fname);
        tv_amount.setText(totalAmount);
     //   countValue=count;

        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                builder.delete(0,builder.length());
                list = getCardData(count);
               // sendDataToServer(String.valueOf(cusID), list.toString());
                sendDataToServer(fname,lname,mno,ownCycle,rentCycle, speCycle, kidsCycle,activity,rock,totalCount,totalAmount,curTime,radioType, list.toString(),camera,video);

            }
        });

       switch (count){
           case 1:

               et1.setVisibility(View.VISIBLE);
               image1.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });
               break;
           case 2:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);

               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });
               break;
           case 3:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);

               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);

               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });

               break;
           case 4:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);

               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);

               image1.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               break;
           case 5:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);

               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);

                   }
               });
               break;
           case 6:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);
               et6.setVisibility(View.VISIBLE);
               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image6.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);

                   }
               });
               image6.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode6);

                   }
               });

               break;
           case 7:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);
               et6.setVisibility(View.VISIBLE);
               et7.setVisibility(View.VISIBLE);

               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image6.setVisibility(View.VISIBLE);
               image7.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                   startActivityForResult(intent, rCode2);
               }
           });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);
                   }
               });

               image6.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode6);

                   }
               });
               image7.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode7);

                   }
               });

               break;

           case 8:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);
               et6.setVisibility(View.VISIBLE);
               et7.setVisibility(View.VISIBLE);
               et8.setVisibility(View.VISIBLE);

               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image6.setVisibility(View.VISIBLE);
               image7.setVisibility(View.VISIBLE);
               image8.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);
                   }
               });

               image6.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode6);
                   }
               });

               image7.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode7);

                   }
               });
               image8.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode8);

                   }
               });


               break;

           case 9:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);
               et6.setVisibility(View.VISIBLE);
               et7.setVisibility(View.VISIBLE);
               et8.setVisibility(View.VISIBLE);
               et9.setVisibility(View.VISIBLE);
               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image6.setVisibility(View.VISIBLE);
               image7.setVisibility(View.VISIBLE);
               image8.setVisibility(View.VISIBLE);
               image9.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);
                   }
               });

               image6.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode6);
                   }
               });

               image7.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode7);

                   }
               });
               image8.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode8);

                   }
               });
               image9.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode9);

                   }
               });


               break;
           case 10:
               et1.setVisibility(View.VISIBLE);
               et2.setVisibility(View.VISIBLE);
               et3.setVisibility(View.VISIBLE);
               et4.setVisibility(View.VISIBLE);
               et5.setVisibility(View.VISIBLE);
               et6.setVisibility(View.VISIBLE);
               et7.setVisibility(View.VISIBLE);
               et8.setVisibility(View.VISIBLE);
               et9.setVisibility(View.VISIBLE);
               et10.setVisibility(View.VISIBLE);
               image1.setVisibility(View.VISIBLE);
               image2.setVisibility(View.VISIBLE);
               image3.setVisibility(View.VISIBLE);
               image4.setVisibility(View.VISIBLE);
               image5.setVisibility(View.VISIBLE);
               image6.setVisibility(View.VISIBLE);
               image7.setVisibility(View.VISIBLE);
               image8.setVisibility(View.VISIBLE);
               image9.setVisibility(View.VISIBLE);
               image10.setVisibility(View.VISIBLE);
               image1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode);
                   }
               });

               image2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode2);
                   }
               });

               image3.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode3);

                   }
               });
               image4.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode4);

                   }
               });
               image5.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode5);
                   }
               });

               image6.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode6);
                   }
               });

               image7.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode7);

                   }
               });
               image8.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode8);

                   }
               });
               image9.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode9);

                   }
               });
               image10.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent=new Intent(CardDetailsActivity.this, CardScanActivity.class);
                       startActivityForResult(intent, rCode10);

                   }
               });



               break;
               default:
                   Toast.makeText(this, "No count found",Toast.LENGTH_LONG).show();
                   break;
       }
    }

    private void sendDataToServer(final String fname, final String lname, final String mno, final String ownCycle, final String rentCycle,
                                  final String speCycle, final String kidsCycle, final String activity,
                                  final String rock, final String totalCount, final String totalAmount,
                                  final String curTime, final String radioType, final String list, final String camera, final String video) {
        if (Utils.isNetConnected(this)) {
            Log.e("called","called");
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                HttpStack stack = null;
                try {
                    stack = new HurlStack(null, new TLSSocketFactory());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                    Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                    stack = new HurlStack();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                    stack = new HurlStack();
                }
                requestQueue = Volley.newRequestQueue(CardDetailsActivity.this, stack);
            } else {
                requestQueue = Volley.newRequestQueue(context);
            }


            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
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
                            if (error instanceof NoConnectionError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof TimeoutError)
                            {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "Please check your Network", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("adminID",adminID);
                    map.put("mobileNumber",mno);
                    map.put("firstName",fname);
                    map.put("lastName",lname);
                    map.put("ownCycle",ownCycle);
                    map.put("rentCycle",rentCycle);
                    map.put("specialCycle",speCycle);
                    map.put("kidsCycle",kidsCycle);
                    map.put("walkRun",activity);
                    map.put("rockClimbing",rock);
                    map.put("paymentmode",radioType);
                    map.put("amount",totalAmount);
                    map.put("currentDate",curTime);
                    map.put("registerType","OneTime");
                    map.put("camera", camera);
                    map.put("video", video);
                    map.put("count",totalCount);
                    map.put("cardList", builder.toString());
                    map.put("eventID", eventID);
                    map.put("deptID", deptID);
                    Log.e("Camera",camera);
                    Log.e("Video", video);
                    Log.e("MApValues==",map.toString());

                    return map;
                }
            };

            requestQueue.add(stringRequest);

        } else {
            Utils.showDialog(context);
        }
    }


    private List<String> getCardData(int count) {
        List<String> list = new ArrayList<>();
        String data1, data2, data3, data4, data5, data6, data7, data8, data9, data10;
        switch (count){
            case 1:
                data1 = et1.getText().toString();
               // list.add(data1);
                builder.append(data1);
                break;
            case 2:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                builder.append(data1).append(",").append(data2);
               /* list.add(data1);
                list.add(data2);*/
                break;
            case 3:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                /*list.add(data1);
                list.add(data2);
                list.add(data3);*/
                builder.append(data1).append(",").append(data2).append(",").append(data3);
                break;
            case 4:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4);
               /* list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);*/
                break;
            case 5:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5);
               /* list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);*/
                break;
            case 6:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                data6 = et6.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5).append(",").append(data6);
                /*list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);*/
                break;
            case 7:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                data6 = et6.getText().toString();
                data7 = et7.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5).append(",").append(data6).append(",").append(data7);
               /* list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);
                list.add(data7);*/
                break;

            case 8:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                data6 = et6.getText().toString();
                data7 = et7.getText().toString();
                data8 = et8.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5).append(",").append(data6).append(",").append(data7).append(",").append(data8);
               /* list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);
                list.add(data7);
                list.add(data8);*/
                break;

            case 9:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                data6 = et6.getText().toString();
                data7 = et7.getText().toString();
                data8 = et8.getText().toString();
                data9 = et9.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5).append(",").append(data6).append(",").append(data7).append(",").append(data8).append(",").append(data9);
                /*list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);
                list.add(data7);
                list.add(data8);
                list.add(data9);*/
                break;
            case 10:
                data1 = et1.getText().toString();
                data2 = et2.getText().toString();
                data3 = et3.getText().toString();
                data4 = et4.getText().toString();
                data5 = et5.getText().toString();
                data6 = et6.getText().toString();
                data7 = et7.getText().toString();
                data8 = et8.getText().toString();
                data9 = et9.getText().toString();
                data10 = et10.getText().toString();
                builder.append(data1).append(",").append(data2).append(",").append(data3).append(",").append(data4).append(",").append(data5).append(",").append(data6).append(",").append(data7).append(",").append(data8).append(",").append(data9).append(",").append(data10);
              /*  list.add(data1);
                list.add(data2);
                list.add(data3);
                list.add(data4);
                list.add(data5);
                list.add(data6);
                list.add(data7);
                list.add(data8);
                list.add(data9);
                list.add(data10);*/
                break;
            default:
                Toast.makeText(this, "No count found",Toast.LENGTH_LONG).show();
                break;
        }
        return list;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == rCode && resultCode == Activity.RESULT_OK) {
            String result=data.getStringExtra("result");
            Log.e("result==",result);
               et1.setText(result);
          }
         else if (requestCode == rCode2 && resultCode == Activity.RESULT_OK) {
                String result=data.getStringExtra("result");
                Log.e("result==",result);


               // et1.setText(result);
                et2.setText(result);


            }


               else if (requestCode == rCode3 && resultCode == Activity.RESULT_OK) {
                String result=data.getStringExtra("result");
                Log.e("result==",result);
             //   et1.setText(result);
             //   et2.setText(result);
                et3.setText(result);


                }
                  else if (requestCode == rCode4 && resultCode == Activity.RESULT_OK) {
                    Log.e("called3","called");
                    String result=data.getStringExtra("result");
                    Log.e("result==",result);
                  //  et1.setText(result);
                   // et2.setText(result);
                   // et3.setText(result);
                    et4.setText(result);


                }
                        else if(requestCode == rCode5 && resultCode == Activity.RESULT_OK) {
                        String result=data.getStringExtra("result");
                        Log.e("result==",result);
                       /* et1.setText(result);
                        et2.setText(result);
                        et3.setText(result);
                        et4.setText(result);*/
                        et5.setText(result);
                    }
                           else if(requestCode == rCode6 && resultCode == Activity.RESULT_OK) {
                            Log.e("called3","called");
                            String result=data.getStringExtra("result");
                            Log.e("result==",result);
                          /*  et1.setText(result);
                            et2.setText(result);
                          et3.setText(result);
                          et4.setText(result);
                         et5.setText(result);*/
                         et6.setText(result);


                        }
                   else if(requestCode == rCode7 && resultCode == Activity.RESULT_OK) {
                                Log.e("called3","called");
                                String result=data.getStringExtra("result");
                                Log.e("result==",result);
         /*   et1.setText(result);
            et2.setText(result);
            et3.setText(result);
            et4.setText(result);
            et5.setText(result);
            et6.setText(result);*/
            et7.setText(result);


                            }
                               else if(requestCode == rCode8 && resultCode == Activity.RESULT_OK)  {

                                    Log.e("called3","called");
                                    String result=data.getStringExtra("result");
                                    Log.e("result==",result);
           /* et1.setText(result);
            et2.setText(result);
            et3.setText(result);
            et4.setText(result);
            et5.setText(result);
            et6.setText(result);
            et7.setText(result);*/
            et8.setText(result);


                                }
                                  else if(requestCode == rCode9 && resultCode == Activity.RESULT_OK)  {

                                        Log.e("called3","called");
                                        String result=data.getStringExtra("result");
                                        Log.e("result==",result);
          /*  et1.setText(result);
            et2.setText(result);
            et3.setText(result);
            et4.setText(result);
            et5.setText(result);
            et6.setText(result);
            et7.setText(result);
            et8.setText(result);*/
            et9.setText(result);


                                    }
                                     else if(requestCode == rCode10 && resultCode == Activity.RESULT_OK)  {

                                            Log.e("called3","called");
                                            String result=data.getStringExtra("result");
                                            Log.e("result==",result);
                                           /* et1.setText(result);
                                            et2.setText(result);
                                            et3.setText(result);
                                              et4.setText(result);
                                              et5.setText(result);
                                            et6.setText(result);
                                            et7.setText(result);
                                           et8.setText(result);
                                         et9.setText(result);*/
                                         et10.setText(result);



                                        }

        }
    }//

