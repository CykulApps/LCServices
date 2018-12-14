package com.cykulapps.lcservices.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.utils.Constants;
import com.cykulapps.lcservices.views.CustomButton;


public class QRCodeResultsActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    CustomButton rescan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_status_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String msg= getIntent().getStringExtra("msg");
        String status = getIntent().getStringExtra("status");
        textView = (TextView) findViewById(R.id.textview);
        imageView = (ImageView) findViewById(R.id.imageview);
        rescan = (CustomButton) findViewById(R.id.scanagain);
        if (status.equals(Constants.TRUE)) {
            textView.setText(msg);
            imageView.setImageResource(R.drawable.tick);
        }else{
            textView.setText(msg);
            imageView.setImageResource(R.drawable.exclamation);
        }
//        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
//        rotateAnimation.setDuration(1500);
//        rotateAnimation.setRepeatCount(Animation.INFINITE);
//        LinearLayout layout = (LinearLayout) findViewById(R.id.imageLayout);
//        if (msg.contains("beverage")){
//            layout.setVisibility(View.VISIBLE);
//            int mCount;
//            if (!msg.replaceAll("[\\D]", "").equalsIgnoreCase("")) {
//                mCount = Integer.parseInt(msg.replaceAll("[\\D]", ""));
//            }else {
//                mCount = 0;
//            }
//            for(int i = 0; i< mCount; i++) {
//                ImageView image = new ImageView(this);
//                image.setImageDrawable(getDrawable(R.drawable.beverage));
//                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(63,200));
//                image.setPadding(5,5,5,5);
//                image.setMaxHeight(200);
//                image.setMaxWidth(63);
//                layout.addView(image);
//                image.startAnimation(rotateAnimation);
//            }
//        }else {
//            layout.setVisibility(View.GONE);
//        }

        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(QRCodeResultsActivity.this, QRCodeReader1.class);
                startActivity(refresh);
                QRCodeResultsActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
