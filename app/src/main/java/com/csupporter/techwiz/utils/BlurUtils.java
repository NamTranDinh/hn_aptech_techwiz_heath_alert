package com.csupporter.techwiz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class BlurUtils {

    private static final float DEFAULT_SCALE = 1f;
    private static final float DEFAULT_BLUR_RADIUS = 4f;

    private static float scale = DEFAULT_SCALE;
    private static float radius = DEFAULT_BLUR_RADIUS;

    public static void config(float scale, float radius) {
        BlurUtils.scale = scale;
        BlurUtils.radius = radius;
    }

    public static void resetConfig() {
        BlurUtils.scale = DEFAULT_SCALE;
        BlurUtils.radius = DEFAULT_BLUR_RADIUS;
    }

    public static Bitmap blur(@NonNull View v) {
        return blur(v.getContext(), getScreenshot(v));
    }

    public static Bitmap blur(Context context, @DrawableRes int drawableRes) {
        return blur(context, drawableToBitmap(context, drawableRes));
    }

    public static Bitmap blur(Context context, Drawable drawable) {
        return blur(context, drawableToBitmap(drawable));
    }

    public static Bitmap blur(Context context, Bitmap image) {
        if (image == null) {
            return null;
        }
        int width = Math.round(image.getWidth() * scale);
        int height = Math.round(image.getHeight() * scale);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    private static Bitmap getScreenshot(@NonNull View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private static Bitmap drawableToBitmap(Context context, int drawableId) {
        return drawableToBitmap(ContextCompat.getDrawable(context, drawableId));
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        Bitmap bitmap;

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private BlurUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

}