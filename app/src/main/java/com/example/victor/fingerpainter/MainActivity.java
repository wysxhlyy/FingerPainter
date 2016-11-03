package com.example.victor.fingerpainter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FingerPainterView myFingerPainterView;

    private Button changeColor;
    private Button changeBrush;

    static final int ACTIVITY_COLOR_REQUEST_CODE=1;
    static final int ACTIVITY_BRUSH_REQUEST_CODE=2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFingerPainterView=new FingerPainterView(this);
        myFingerPainterView=(FingerPainterView)findViewById(R.id.myPainter);

        if(getIntent().getData()!=null){
            Intent image=getIntent();
            myFingerPainterView.load(image.getData());
        }



        changeColor=(Button)findViewById(R.id.color);
        changeBrush=(Button)findViewById(R.id.brush);

        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Changecolor.class);
                Bundle bundle=new Bundle();
                bundle.putString("nowColor",myFingerPainterView.getColour()+"");
                intent.putExtras(bundle);
                startActivityForResult(intent,ACTIVITY_COLOR_REQUEST_CODE);
            }
        });


        changeBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Changebrush.class);
                Bundle bundle=new Bundle();
                bundle.putString("nowBrushType",myFingerPainterView.getBrush()+"");
                bundle.putString("nowBrushWidth",myFingerPainterView.getBrushWidth()+"");
                intent.putExtras(bundle);
                startActivityForResult(intent,ACTIVITY_BRUSH_REQUEST_CODE);
            }
        });



    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
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
}
