package com.csupporter.techwiz.utils;

import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.FragmentActivity;

public class WindowUtils {

    public static void setWindowBackground(FragmentActivity activity, @DrawableRes int drawable) {
        if (activity == null) {
            return;
        }
        activity.getWindow().setBackgroundDrawable(new BitmapDrawable(activity.getResources(), BlurUtils.blur(activity, drawable)));
    }
}
