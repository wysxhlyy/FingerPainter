package com.example.victor.fingerpainter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Changebrush extends AppCompatActivity {

    private TextView brushType;
    private TextView brushWidth;

    private RadioGroup newBrushType;
    private String newBT;
    private String newBW;

    private EditText newBrushWidth;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changebrush);

        //Get the brushtype and brushwidth used now.
        Bundle bundle = getIntent().getExtras();
        brushType = (TextView) findViewById(R.id.brushType);
        brushWidth = (TextView) findViewById(R.id.brushWidth);
        brushType.setText(bundle.getString("nowBrushType"));
        brushWidth.setText(bundle.getInt("nowBrushWidth")+"");

        //set the new brushtype using radiogroup.
        newBrushType=(RadioGroup)findViewById(R.id.newBrushType);
        newBrushType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.round){
                    newBT="round";
                }
                if(radioGroup.getCheckedRadioButtonId()==R.id.square){
                    newBT="square";
                }
            }
        });

        //transfer the new brushtype and color data back to MainActivity.
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result=new Intent();
                Bundle bundle1=new Bundle();
                bundle1.putString("newBrushType",newBT);
                newBrushWidth=(EditText)findViewById(R.id.newWidth);
                newBW=newBrushWidth.getText().toString();
                bundle1.putString("newBrushWidth",newBW);
                result.putExtras(bundle1);
                setResult(Activity.RESULT_OK,result);
                finish();

            }
        });
    }
}
