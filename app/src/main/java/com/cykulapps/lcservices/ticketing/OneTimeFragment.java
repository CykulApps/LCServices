package com.cykulapps.lcservices.ticketing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cykulapps.lcservices.R;

import com.cykulapps.lcservices.overrideFonts;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class OneTimeFragment extends Fragment {
    EditText et_fname, et_lname, et_mob;
    RadioButton r_payment, r_cash;
    RequestQueue requestQueue;
    Context context;
    EditText et_own, et_rent, et_spe, et_kids_walk, et_activity, et_rock, et_camera, et_video, et_adult_park_cycle, et_kids_park_cycle;
    TextView tv_total;
    Button btn_submit;
    RadioGroup radioMode;
    String eventID;
    TextView tv_own_price, tv_own_total,tv_rent_price, tv_rent_total,tv_spe_price,
            tv_spe_total,tv_kids_price, tv_kids_walk_total,tv_activity_price,
            tv_activity_total,tv_rock_price, tv_rock_total, tv_camera_price,
            tv_camera_total, tv_video_price, tv_video_total, tv_adult_park_cycle_price, tv_adult_cycle_total, tv_kids_park_cycle_price,tv_kids_cycle_total;

    String ownCycle, RentalCycle, adultRentalCycle, SpeCycle, kidsCycle, kidsWalk, sactivity, srock, scamera, svideo;
    LinearLayout layoutKW, layoutAW, layoutCamera, layoutVideo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_one_time, container, false);
        overrideFonts fonts = new overrideFonts();
        fonts.overrideFonts(getActivity(), root);
        context = getActivity();
        requestQueue = Volley.newRequestQueue(context);

        layoutKW = root.findViewById(R.id.layoutKW);
        layoutAW = root.findViewById(R.id.layoutAW);
        layoutCamera = root.findViewById(R.id.layoutCamera);
        layoutVideo = root.findViewById(R.id.layoutVideo);

        et_fname = root.findViewById(R.id.et_fname);
        et_lname = root.findViewById(R.id.et_lname);
        et_mob = root.findViewById(R.id.et_mob);
        et_own = root.findViewById(R.id.et_own);
        et_rent = root.findViewById(R.id.et_rent);
        et_spe = root.findViewById(R.id.et_spe);
        et_kids_walk = root.findViewById(R.id.et_kids_walk);
        et_activity = root.findViewById(R.id.et_activity);
        et_rock = root.findViewById(R.id.et_rock);
        et_camera = root.findViewById(R.id.et_camera);
        et_video = root.findViewById(R.id.et_video);
        tv_total = root.findViewById(R.id.et_total);
        r_payment = root.findViewById(R.id.radio_payment);
        r_cash = root.findViewById(R.id.radio_cash);
        btn_submit = root.findViewById(R.id.btn_submit);
        radioMode = root.findViewById(R.id.radio_mode);

        tv_own_price = root.findViewById(R.id.tv_own_price);
        tv_own_total = root.findViewById(R.id.tv_own_total);

        tv_rent_price = root.findViewById(R.id.tv_rent_price);
        tv_rent_total = root.findViewById(R.id.tv_rent_total);

        tv_spe_price = root.findViewById(R.id.tv_spe_price);
        tv_spe_total = root.findViewById(R.id.tv_spe_total);

        tv_kids_price = root.findViewById(R.id.tv_kids_price);
        tv_kids_walk_total = root.findViewById(R.id.tv_kids_walk_total);

        tv_activity_price = root.findViewById(R.id.tv_activity_price);
        tv_activity_total = root.findViewById(R.id.tv_activity_total);

        tv_rock_price = root.findViewById(R.id.tv_rock_price);
        tv_rock_total = root.findViewById(R.id.tv_rock_total);

        tv_camera_price = root.findViewById(R.id.tv_camera_price);
        tv_camera_total = root.findViewById(R.id.tv_camera_total);

        tv_video_price = root.findViewById(R.id.tv_video_price);
        tv_video_total = root.findViewById(R.id.tv_video_total);

        tv_total.setText("0.0");

        //adult park cycle
        tv_adult_park_cycle_price = root.findViewById(R.id.tv_park_cycle__price);
        et_adult_park_cycle = root.findViewById(R.id.et_adult_park_cycle);
        tv_adult_cycle_total = root.findViewById(R.id.tv_adult_park_cycle_total);

        //kids cycle
        tv_kids_park_cycle_price = root.findViewById(R.id.tv_kids_cycle__price);
        et_kids_park_cycle = root.findViewById(R.id.et_kids_park_cycle);
        tv_kids_cycle_total = root.findViewById(R.id.tv_kids_cycle_total);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences2.getString("peventID", "");

        if(eventID.equalsIgnoreCase("DRCV")) {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefOneDRCV", MODE_PRIVATE);
            ownCycle = sharedPreferences.getString("ownCycle", "");
            RentalCycle = sharedPreferences.getString("RentalCycle", "");
            adultRentalCycle = sharedPreferences.getString("RentalCycle", "");
            SpeCycle = sharedPreferences.getString("SpeCycle", "");
            kidsCycle = sharedPreferences.getString("kidsCycle", "");
            kidsWalk = sharedPreferences.getString("kidsWalk", "");
            sactivity = sharedPreferences.getString("activity", "");
            srock = sharedPreferences.getString("rock", "");
            scamera = sharedPreferences.getString("camera", "");
            svideo = sharedPreferences.getString("video", "");

        }else if(eventID.equalsIgnoreCase("PPCP")){

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefOnePPCP", MODE_PRIVATE);
            ownCycle = sharedPreferences.getString("ownCycle", "");
            RentalCycle = sharedPreferences.getString("RentalCycle", "");
            adultRentalCycle = sharedPreferences.getString("RentalCycle", "");
            SpeCycle = sharedPreferences.getString("SpeCycle", "");
            kidsCycle = sharedPreferences.getString("kidsCycle", "");
            kidsWalk = sharedPreferences.getString("kidsWalk", "");
            sactivity = sharedPreferences.getString("activity", "");
            srock = sharedPreferences.getString("rock", "");
            scamera = sharedPreferences.getString("camera", "");
            svideo = sharedPreferences.getString("video", "");

            layoutKW.setVisibility(View.GONE);
            layoutAW.setVisibility(View.GONE);
            layoutCamera.setVisibility(View.GONE);
            layoutVideo.setVisibility(View.GONE);
        }

        final String adultParkCycle = "Rs. "+adultRentalCycle+" x";
        tv_adult_park_cycle_price.setText(adultParkCycle);

        final String kidsParkCycle = "Rs. "+kidsCycle+" x";
        tv_kids_park_cycle_price.setText(kidsParkCycle);

        final String ownPrice = "Rs. "+ownCycle+" x";
        tv_own_price.setText(ownPrice);

        final String rentPrice = "Rs. "+RentalCycle+" x";
        tv_rent_price.setText(rentPrice);

        final String spePrice = "Rs. "+SpeCycle+" x";
        tv_spe_price.setText(spePrice);

        final String kidsWalkPrice = "Rs. "+kidsCycle+" x";
        tv_kids_price.setText(kidsWalkPrice);

        final String activityPrice = "Rs. "+sactivity+" x";
        tv_activity_price.setText(activityPrice);

        final String rockPrice = "Rs. "+srock+" x";
        tv_rock_price.setText(rockPrice);

        final String cameraPrice = "Rs. "+scamera+" x";
        tv_camera_price.setText(cameraPrice);

        final String videoPrice = "Rs. "+svideo+" x";
        tv_video_price.setText(videoPrice);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk = et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String mno = et_mob.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();

                int down, drent, dspe, dkids, dkidsw, dactivity, drock,dcamera,dvideo;

                if (own.length() == 0) {
                    down = 0;
                } else {
                    down = Integer.parseInt(own);
                }

                if (adultRent.length() == 0) {
                    drent = 0;
                } else {
                    drent = Integer.parseInt(adultRent);

                }
                if (spe.length() == 0) {
                    dspe = 0;
                } else {
                    dspe = Integer.parseInt(spe);
                }

                if (kidsRent.length() == 0) {
                    dkids = 0;
                } else {
                    dkids = Integer.parseInt(kidsRent);

                }

                if (kidsWalk.length() == 0) {
                    dkidsw = 0;
                } else {
                    dkidsw = Integer.parseInt(kidsWalk);

                }

                if (activity.length() == 0) {
                    dactivity = 0;
                } else {
                    dactivity = Integer.parseInt(activity);

                }
                if (rock.length() == 0) {
                    drock = 0;

                } else {
                    drock = Integer.parseInt(rock);
                }

                if (camera.length() == 0) {
                    dcamera = 0;

                } else {
                    dcamera = Integer.parseInt(camera);
                }
                if (video.length() == 0) {
                    dvideo = 0;

                } else {
                    dvideo = Integer.parseInt(video);
                }


                int totalCount = down + drent + dspe + dkids + dkidsw + dactivity + drock+ dcamera+ dvideo;
                Log.e("Total Count", totalCount + "");
                if (radioMode.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please select payment mode", Toast.LENGTH_SHORT).show();
                } else if (totalCount > 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You can not enter more than 10 !!!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if(totalCount==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please enter atleast one ticket !!!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String curTime = sdf.format(new Date());
                    String totalAmount = tv_total.getText().toString();
                    int selectedId = radioMode.getCheckedRadioButtonId();
                    RadioButton radioButton = root.findViewById(selectedId);

                    String radioType = radioButton.getText().toString();
                    //    sendDataToServer(fname,lname,mno,down,drent, dspe, dkids,dactivity,drock,totalCount,totalAmount,curTime,radioType);

                    Intent intent = new Intent(context, CardDetailsActivity.class);
                    intent.putExtra("fname", fname);
                    intent.putExtra("lname", lname);
                    intent.putExtra("mno", mno);
                    intent.putExtra("ownCycle", String.valueOf(down));
                    intent.putExtra("rentCycle", String.valueOf(drent));
                    intent.putExtra("speCycle", String.valueOf(dspe));
                    intent.putExtra("kidsCycle", String.valueOf(dkids));
                    intent.putExtra("kidsWalk", String.valueOf(dkidsw));
                    intent.putExtra("activity", String.valueOf(dactivity));
                    intent.putExtra("rock", String.valueOf(drock));
                    intent.putExtra("totalCount", String.valueOf(totalCount));
                    intent.putExtra("totalAmount", String.valueOf(totalAmount));
                    intent.putExtra("curTime", String.valueOf(curTime));
                    intent.putExtra("radioType", String.valueOf(radioType));
                    intent.putExtra("camera", String.valueOf(dcamera));
                    intent.putExtra("video", String.valueOf(dvideo));
                    startActivity(intent);
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
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk =  et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
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

        et_adult_park_cycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk =  et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);
                if (adultRent.length()!=0){
                    double own_total = Integer.parseInt(adultRentalCycle)*Integer.parseInt(adultRent);
                    String total = " = "+String.valueOf(own_total);
                    tv_adult_cycle_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_adult_cycle_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_kids_park_cycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk =  et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);
                if (kidsRent.length()!=0){
                    double own_total = Integer.parseInt(kidsCycle)*Integer.parseInt(kidsRent);
                    String total = " = "+String.valueOf(own_total);
                    tv_kids_cycle_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_kids_cycle_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_kids_walk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk =  et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);
                if (kidsWalk.length()!=0){
                    double own_total = Integer.parseInt(kidsCycle)*Integer.parseInt(kidsWalk);
                    String total = " = "+String.valueOf(own_total);
                    tv_kids_walk_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_kids_walk_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        et_spe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_rent.getText().toString();
                String kidsRent = et_kids_park_cycle.getText().toString();
                String kidsWalk =  et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();

                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (spe.length()!=0){
                    double own_total = Integer.parseInt(SpeCycle)*Integer.parseInt(spe);
                    String total = " = "+String.valueOf(own_total);
                    tv_spe_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_spe_total.setText(total);
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
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_adult_park_cycle.getText().toString();
                String kidsWalk = et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
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

        et_rock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_adult_park_cycle.getText().toString();
                String kidsWalk = et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();

                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);

                if (rock.length()!=0){
                    double own_total = Integer.parseInt(srock)*Integer.parseInt(rock);
                    String total = " = "+String.valueOf(own_total);
                    tv_rock_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_rock_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_camera.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_adult_park_cycle.getText().toString();
                String kidsWalk = et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);
                if (camera.length()!=0){
                    double own_total = Integer.parseInt(scamera)*Integer.parseInt(camera);
                    String total = " = "+String.valueOf(own_total);
                    tv_camera_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_camera_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_video.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String own = et_own.getText().toString();
                String adultRent = et_adult_park_cycle.getText().toString();
                String kidsRent = et_adult_park_cycle.getText().toString();
                String kidsWalk = et_kids_walk.getText().toString();
                String spe = et_spe.getText().toString();
                String activity = et_activity.getText().toString();
                String rock = et_rock.getText().toString();
                String camera = et_camera.getText().toString();
                String video = et_video.getText().toString();
                double dtotal = calculatePrice(own, adultRent, spe, kidsRent, kidsWalk, activity, rock,camera,video);
                String tvtotal = String.valueOf(dtotal);
                tv_total.setText(tvtotal);
                if (video.length()!=0){
                    double own_total = Integer.parseInt(svideo)*Integer.parseInt(video);
                    String total = " = "+String.valueOf(own_total);
                    tv_video_total.setText(total);
                }else {
                    String total = " = 0.00";
                    tv_video_total.setText(total);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private double calculatePrice(String own,String rent, String spe, String kidsRent, String kidsWalk, String activity, String rock, String camera, String video) {
        Double down,drent, dspe, dkids, dkidsw, dactivity, drock, dcamera, dvideo, dtotal = 0.0;

        /*SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        eventID = sharedPreferences2.getString("peventID", "");
        if(eventID.equalsIgnoreCase("DRCV")) {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefOne", MODE_PRIVATE);
            String ownCycle = sharedPreferences.getString("ownCycle", null);
            String RentalCycle = sharedPreferences.getString("RentalCycle", null);
            String SpeCycle = sharedPreferences.getString("SpeCycle", null);
            String kidsCycle = sharedPreferences.getString("kidsCycle", null);
            String sactivity = sharedPreferences.getString("activity", null);
            String srock = sharedPreferences.getString("rock", null);
            String scamera = sharedPreferences.getString("camera", null);
            String svideo = sharedPreferences.getString("video", null);

        }else if(eventID.equalsIgnoreCase("PPCP")){

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefOne", MODE_PRIVATE);
            String ownCycle = sharedPreferences.getString("ownCycle", null);
            String RentalCycle = sharedPreferences.getString("RentalCycle", null);
            String SpeCycle = sharedPreferences.getString("SpeCycle", null);
            String kidsCycle = sharedPreferences.getString("kidsCycle", null);
            String sactivity = sharedPreferences.getString("activity", null);
            String srock = sharedPreferences.getString("rock", null);
            String scamera = sharedPreferences.getString("camera", null);
            String svideo = sharedPreferences.getString("video", null);

        }*/
        int aown = Integer.parseInt(ownCycle);
        int arent = Integer.parseInt(RentalCycle);
        int aspe = Integer.parseInt(SpeCycle);
        int akids = Integer.parseInt(kidsCycle);
        int act = Integer.parseInt(sactivity);
        int arock = Integer.parseInt(srock);
        int acamera = Integer.parseInt(scamera);
        int avideo = Integer.parseInt(svideo);

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
        if (spe.length()==0){
            dspe = 0.0;
        }else {
            dspe = Double.parseDouble(spe);
            dspe = dspe*aspe;
        }
        if (kidsRent.length()==0){
            dkids = 0.0;
        }else {
            dkids = Double.parseDouble(kidsRent);
            dkids = dkids*akids;
        }

        if (kidsWalk.length()==0){
            dkidsw = 0.0;
        }else {
            dkidsw = Double.parseDouble(kidsWalk);
            dkidsw = dkidsw*akids;
        }
        
        if (activity.length()==0){
            dactivity = 0.0;
        }else {
            dactivity = Double.parseDouble(activity);
            dactivity = dactivity*act;
        }
        if (rock.length()==0){
            drock = 0.0;

        }else {
            drock = Double.parseDouble(rock);
            drock = drock*arock;
        }

        if (camera.length()==0){
            dcamera = 0.0;

        }else {
            dcamera = Double.parseDouble(camera);
            dcamera = dcamera*acamera;
        }

        if (video.length()==0){
            dvideo = 0.0;

        }else {
            dvideo = Double.parseDouble(video);
            dvideo = dvideo*avideo;
        }

        return down+drent+dspe+dkids+dkidsw+dactivity+drock+dcamera+dvideo;
    }
}
