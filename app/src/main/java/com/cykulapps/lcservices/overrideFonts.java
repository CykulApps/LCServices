package com.cykulapps.lcservices;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Daya on 11/17/2017.
 */

public class overrideFonts {

    public void overrideFonts(final Context context, final View v) {
        Typeface typeace = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fontname));
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeace);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(typeace);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(typeace);
            }
        } catch (Exception e) {
        }
    }
    public void overrideFonts2(final Context context, final View v) {
        Typeface typeace = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fontname_aa));
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts2(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeace);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(typeace);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(typeace);
            }
        } catch (Exception e) {
        }
    }
    public void overrideFontsBold(final Context context, final View v) {
        Typeface typeace = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fontname_bold));
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsBold(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeace);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(typeace);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(typeace);
            }
        } catch (Exception e) {
        }
    }

}
