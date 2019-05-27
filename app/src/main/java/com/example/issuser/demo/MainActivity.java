package com.example.issuser.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button openCameraBtn=null;
    private EditText inputEditText=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        openCameraBtn=findViewById(R.id.main_open_camera_btn);
        inputEditText=findViewById(R.id.main_input_name_edt);
        openCameraBtn.setOnClickListener(this);

       // addTextViewWindow();

    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        if (isInMultiWindowMode){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        }else{
          //  getWindow().addFlags(WindowManager.LayoutParams.F);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        }
    }

    private void addTextViewWindow(Context context){

        TextView mview=new CustomTextView(context);

   // 关键点1  WindowManager mWindowManager  = new WindowManagerImpl(ctx);

   //关键点2-->
        WindowManager mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.width = 800;
        wmParams.height = 800;
   // <!--关键点3-->
                Log.i("MYH","add view right");
                mWindowManager.addView(mview, wmParams);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.main_open_camera_btn:
                if(isInMultiWindowMode()){

                    openCamera();
                }else{
                 openCameraForResult();}
                // openDemoTwoActivity(this);
                break;

        }
    }

    public  void openDemoTwoActivity(MainActivity mainActivity){

        try {
            Intent i=new Intent();
            i.setClassName("com.example.apptwo","com.example.apptwo.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainActivity.startActivity(i);

            inputEditText.setFocusable(true);

        }catch (Exception e){
            e.printStackTrace();

        }



    }



    private static final int CAMERA_CODE = 1;

    private Uri currentUri;
    private final int TakePhoto = 123;

    private void openTakePhoto() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "eos");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                file.createNewFile();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                currentUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);
                startActivityForResult(intent, TakePhoto);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "文件存储路径异常", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "请插入sd卡", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCamera(){

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(captureIntent);
    }
    public void openCameraForResult(){

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(captureIntent,CAMERA_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
