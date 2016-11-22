package com.example.victor.fingerpainter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FingerPainterView myFingerPainterView;

    private Button changeColor;
    private Button changeBrush;
    private Button store;

    static final int ACTIVITY_COLOR_REQUEST_CODE=1;  //Identify the activity to change color
    static final int ACTIVITY_BRUSH_REQUEST_CODE=2;  //Identify the activity to change brush
    static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE=1; //Identify the permission


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFingerPainterView=new FingerPainterView(this);
        myFingerPainterView.setId(R.id.myFingerPainterViewId);
        myFingerPainterView=(FingerPainterView)findViewById(R.id.myPainter);

        //load picture from download and sd card
        if(getIntent().getData()!=null){
            Intent image=getIntent();
            myFingerPainterView.load(image.getData());
        }

        //Update the painter when activity resume.
        if(savedInstanceState !=null){
            myFingerPainterView.setColour(savedInstanceState.getInt("nowColor"));
            myFingerPainterView.setBrushWidth(savedInstanceState.getInt("nowBrushWidth"));

            if(savedInstanceState.getString("nowBrushType").equals("ROUND")){
                myFingerPainterView.setBrush(Paint.Cap.ROUND);
            }else if(savedInstanceState.getString("nowBrushType").equals("SQUARE")){
                myFingerPainterView.setBrush(Paint.Cap.SQUARE);
            }
        }

        changeColor=(Button)findViewById(R.id.color);
        changeBrush=(Button)findViewById(R.id.brush);
        store=(Button)findViewById(R.id.store);

        //handle the change color issue.
        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Changecolor.class);
                Bundle bundle=new Bundle();
                bundle.putInt("nowColor",myFingerPainterView.getColour());
                intent.putExtras(bundle);
                startActivityForResult(intent,ACTIVITY_COLOR_REQUEST_CODE);
            }
        });


        //handle the brush change issue.
        changeBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Changebrush.class);
                Bundle bundle=new Bundle();
                bundle.putString("nowBrushType",myFingerPainterView.getBrush().toString());
                bundle.putInt("nowBrushWidth",myFingerPainterView.getBrushWidth());
                intent.putExtras(bundle);
                startActivityForResult(intent,ACTIVITY_BRUSH_REQUEST_CODE);
            }
        });

        //ask for permission to store the photo.(additional functionality)
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        });

    }

    //Store the photo painted into gallery if get permission from user.(additional functionality)
    //checked the official android Api
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        if(requestCode==MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                ContentResolver content=MainActivity.this.getContentResolver();
                myFingerPainterView.setDrawingCacheEnabled(true);
                myFingerPainterView.buildDrawingCache();
                Bitmap bitmap=myFingerPainterView.getDrawingCache();
                String way= MediaStore.Images.Media.insertImage(content,bitmap,"","");
                Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+way));
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(),"Your paint has been stored in gallery ",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //handle the data transfered from other activities.
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        //handle the data from activity called Changebrush.
        if(requestCode==ACTIVITY_BRUSH_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                try {
                    if(bundle.getString("newBrushType").equals("round")){
                        myFingerPainterView.setBrush(Paint.Cap.ROUND);
                    }else if(bundle.getString("newBrushType").equals("square")){
                        myFingerPainterView.setBrush(Paint.Cap.SQUARE);
                    }
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"You didn't choose brush Type",Toast.LENGTH_SHORT).show();
                }

                int width=0;
                try{
                    width=Integer.parseInt(bundle.getString("newBrushWidth"));
                    myFingerPainterView.setBrushWidth(width);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"You didn't Input a valid brush width",Toast.LENGTH_SHORT).show();
                }
            }
        }

        //handle the data from activity called Changecolor.
        if(requestCode==ACTIVITY_COLOR_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                switch (bundle.getString("changedColor")){
                    case "red":myFingerPainterView.setColour(Color.RED);
                        break;
                    case "blue":myFingerPainterView.setColour(Color.BLUE);
                        break;
                    case "green":myFingerPainterView.setColour(Color.GREEN);
                        break;
                    case "black":myFingerPainterView.setColour(Color.BLACK);
                        break;
                    case "grey":myFingerPainterView.setColour(Color.GRAY);
                        break;
                    case "yellow":myFingerPainterView.setColour(Color.YELLOW);
                        break;
                    default:myFingerPainterView.setColour(Color.BLACK);
                        Toast.makeText(getApplicationContext(),"No color chosen",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    //store the information of activity into a bundle,used to update the painter when activity resume.
    protected void onSaveInstanceState(Bundle storeInfo){
        super.onSaveInstanceState(storeInfo);
        storeInfo.putInt("nowColor",myFingerPainterView.getColour());
        storeInfo.putString("nowBrushType",myFingerPainterView.getBrush().toString());
        storeInfo.putInt("nowBrushWidth",myFingerPainterView.getBrushWidth());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState !=null){
            myFingerPainterView.setColour(savedInstanceState.getInt("nowColor"));
            myFingerPainterView.setBrushWidth(savedInstanceState.getInt("nowBrushWidth"));

            if(savedInstanceState.getString("nowBrushType").equals("ROUND")){
                myFingerPainterView.setBrush(Paint.Cap.ROUND);
            }else if(savedInstanceState.getString("nowBrushType").equals("SQUARE")){
                myFingerPainterView.setBrush(Paint.Cap.SQUARE);
            }
        }

    }


}
