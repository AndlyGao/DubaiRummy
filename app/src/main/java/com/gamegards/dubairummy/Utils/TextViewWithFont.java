package com.gamegards.dubairummy.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gamegards.dubairummy.R;

import static com.gamegards.dubairummy.Utils.FontManager.Helvetica;


public class TextViewWithFont extends TextView {
    private int defaultDimension = 0;
    private int TYPE_BOLD = 1;
    private int TYPE_ITALIC = 2;
    private int fontType;
    private int fontName;

    public TextViewWithFont(Context context) {
        super(context);
        init(null, 0);
    }
    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public TextViewWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TextViewWithFont, defStyle, 0);
        String fontPath = a.getString(R.styleable.TextViewWithFont_fontFilePath);
        fontType = a.getInt(R.styleable.TextViewWithFont_type, defaultDimension);
        a.recycle();

        if (fontPath != null) {
            setFontType(FontManager.getTypeface(getContext(), fontPath));
        }
        else {
            fontPath = Helvetica;
            setFontType(FontManager.getTypeface(getContext(), fontPath));
        }
    }
    private void setFontType(Typeface font) {
        if (fontType == TYPE_BOLD) {
            setTypeface(font, Typeface.BOLD);
        } else if (fontType == TYPE_ITALIC) {
            setTypeface(font, Typeface.ITALIC);
        } else {
            setTypeface(font);
        }
    }
}
