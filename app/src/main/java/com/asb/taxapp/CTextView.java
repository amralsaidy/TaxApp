package com.asb.taxapp;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Amr Alsaidy on 2017/11/1.
 */

public class CTextView extends AppCompatTextView {
    private Typeface font;

    public CTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
        setFonts(context);
    }

    public CTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setFonts(context);

    }

    public CTextView(Context context) {
        super(context);
        init(null);
        setFonts(context);
    }

    public void setFonts(Context context) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/questv.otf");
        setTypeface(font);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
//            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CTextView);
//            String fontName = a.getString(R.styleable.CTextView_fontName);
//            if (fontName!=null) {
//                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
//                setTypeface(myTypeface);
//            }
//            a.recycle();

        }
    }
}
