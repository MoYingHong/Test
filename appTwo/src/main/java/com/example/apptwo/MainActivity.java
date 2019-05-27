package com.example.apptwo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    OrientationEventListener mOrientationListener;
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.text);

       /*try{
           Thread.sleep(2000);
       }catch (Exception e){

       }


        finish();*/
       // showText(getDisplaySize());

        mOrientationListener=new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                Log.i(TAG,"--------------");
                Log.i(TAG,"orientation = " +orientation);
            }
        };
        mOrientationListener.enable();

    }

    private void showText(String text){

        mTextView.setText(text);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    //    showText(newConfig.screenHeightDp+":" +newConfig.screenWidthDp);
        Log.i(TAG,newConfig.screenHeightDp+":" +newConfig.screenWidthDp);
        //  // showText(getDisplaySize().widthPixels+" : " +);
    }

    DisplayMetrics dm =null;

    private void getDisplaySize(){
       // DisplayMetrics ds=new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        dm= new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
       float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        dm.widthPixels= (int) (width / density);  // 屏幕宽度(dp)
        dm.heightPixels = (int) (height / density);// 屏幕高度(dp)

    }
}
