package com.csupporter.techwiz.presentation.view.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.csupporter.techwiz.R;

public class OtpEditText extends AppCompatEditText {
    private int mMaxLength = 4; //4 digit by default
    private float mSpace = 24; //24 dp by default, space between the lines
    private float mLineSpacing = 8; //8dp by default, height of the text from our lines
    private Paint mLinesPaint;
    private OnClickListener mClickListener;
    private float[] mTextWidths;

    public OtpEditText(Context context) {
        super(context);
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OtpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OtpEditText);
        try {
            mMaxLength = typedArray.getInt(R.styleable.OtpEditText_otp_max_length, 4);
            mSpace = typedArray.getDimension(R.styleable.OtpEditText_otp_space, multi * 24);
            mLineSpacing = multi * mLineSpacing; //convert to pixels for our density
            float mLineStroke = typedArray.getDimension(R.styleable.OtpEditText_otp_line_stroke, multi * 2f);
            int mTextColor = typedArray.getColor(R.styleable.OtpEditText_otp_text_color, Color.BLACK);
            int mLineColor = typedArray.getColor(R.styleable.OtpEditText_otp_line_color, Color.BLACK);

            mTextWidths = new float[mMaxLength];
            mLinesPaint = new Paint(getPaint());
            mLinesPaint.setStrokeWidth(mLineStroke);
            mLinesPaint.setColor(mLineColor);
            getPaint().setColor(mTextColor);
            setBackgroundResource(0);
        } finally {
            typedArray.recycle();
        }

        super.setOnClickListener(v -> {
            // When tapped, move cursor to end of text.
            setSelection(getTextString().length());
            if (mClickListener != null) {
                mClickListener.onClick(v);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    public String getTextString() {
        return super.getText() == null ? "" : super.getText().toString();
    }

    public int getOtp() {
        try {
            return Integer.parseInt(getTextString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        float mCharSize;
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mMaxLength * 2f - 1));
        } else {
            mCharSize = (availableWidth - (mSpace * (mMaxLength - 1))) / mMaxLength;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        //Text Width
        String text = getTextString();
        int textLength = text.length();
        getPaint().getTextWidths(getText(), 0, textLength, mTextWidths);
        for (int i = 0; i < mMaxLength; i++) {
            canvas.drawLine(startX, bottom, startX + mCharSize, bottom, mLinesPaint);
            if (textLength > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text, i, i + 1, middle - mTextWidths[0] / 2, bottom - mLineSpacing, getPaint());
            }
            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }
}