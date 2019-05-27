package com.example.issuser.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;




/**
 * Created by hexj on 2019/5/8.
 */

public class CustomTextView extends AppCompatTextView{


    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i("myh"," textview init ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("myh"," textview ondraw");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("myh"," textview onAttachedToWindow");
    }
}
