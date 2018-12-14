package com.cykulapps.lcservices.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fonts.otf");
        setTypeface(tf ,1);

    }
}
