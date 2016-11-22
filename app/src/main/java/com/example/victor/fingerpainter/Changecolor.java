package com.example.victor.fingerpainter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;

public class Changecolor extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;

    private String changedColor;
    private ImageView iv;

    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecolor);

        //Reflect the color used now.
        Bundle bundle=getIntent().getExtras();
        int nowColor=bundle.getInt("nowColor");
        iv=(ImageView)findViewById(R.id.nowColor);
        iv.setBackgroundColor(nowColor);

        //handle the spinner to choose the new color
        spinner=(Spinner)findViewById(R.id.spin);
        spinnerAdapter=ArrayAdapter.createFromResource(this,R.array.color,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                changedColor=spinnerAdapter.getItem(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //transfer the data back to MainActivity.
        submit=(Button)findViewById(R.id.submitColor);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1=new Bundle();
                Intent result=new Intent();
                bundle1.putString("changedColor",changedColor);
                result.putExtras(bundle1);
                setResult(Activity.RESULT_OK,result);
                finish();
            }
        });




    }
}
