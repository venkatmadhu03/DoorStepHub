package appsnova.com.doorstephub.ownlibraries;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import appsnova.com.doorstephub.R;

public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
        init(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void init(Context context,AttributeSet attributeSet){


        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.MyTextView);
        String fontName = a.getString(R.styleable.MyTextView_textfont);

        try {
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), fontName);
                setTypeface(myTypeface);
            }else{
                Typeface myTypeface =Typeface.createFromAsset(context.getAssets(),"Righteous.ttf");
                setTypeface(myTypeface);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        a.recycle();

    }


    //file:///android_asset/Roboto-Black.ttf

}
