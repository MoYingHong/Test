package com.example.issuser.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hexj on 2019/5/7.
 */

public class SubMainActivity extends Activity implements View.OnClickListener{
    //用于展示选择的图片
    private ImageView mImageView;

    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);

        initView();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.show_image);
        Button chooseCamera = (Button) findViewById(R.id.choose_camera);
        chooseCamera.setOnClickListener(this);
        Button chooseGallery = (Button) findViewById(R.id.choose_gallery);
        chooseGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_camera:
                //拍照选择
                chooseFromCamera();
                break;
            case R.id.choose_gallery:
                //从相册选取
                chooseFromGallery();
                break;
            default:
                break;
        }
    }

    /**
     * 拍照选择图片
     */
    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
    }

    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {
        //构建一个内容选择的Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择类型为图片类型
        intent.setType("image/*");
        //打开图片选择
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "取消了拍照", Toast.LENGTH_LONG).show();
                    return;
                }
                if(data != null){
                    Bitmap photo = data.getParcelableExtra("data");

                    //将Bitmap转化为uri
                    Uri uri = saveBitmap(photo, "temp");
                    mImageView.setBackground(new BitmapDrawable(photo));
                    //启动图像裁剪
                //    startImageZoom(uri);
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else{
                    //用户从图库选择图片后会返回所选图片的Uri
                    Uri uri;
                    //获取到用户所选图片的Uri
                    uri = data.getData();
                    //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                    uri = convertUri(uri);
                    startImageZoom(uri);
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获取到裁剪后的图像
                        Bitmap bm = extras.getParcelable("data");
                        mImageView.setImageBitmap(bm);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将content类型的Uri转化为文件类型的Uri
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri){
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/avator.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }
}
