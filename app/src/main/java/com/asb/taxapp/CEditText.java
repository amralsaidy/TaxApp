package com.asb.taxapp;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CEditText extends AppCompatEditText {

    private Typeface font;

    public CEditText(Context context) {
        super(context);
        setFonts(context);
    }

    public CEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFonts(context);
    }

    public CEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFonts(context);
    }

    public void setFonts(Context context) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/questv.otf");
        setTypeface(font);
    }
}
