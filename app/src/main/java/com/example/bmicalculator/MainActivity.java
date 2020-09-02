package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText height, weight;
    TextView result;
    Button btn;
    public static final String TAG = "Result.....";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        btn = (Button) findViewById(R.id.btn);
        result = (TextView) findViewById(R.id.result);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmi();
            }
        });
    }

        public void bmi (){
            float h, w;
            String bmilabel ="";

            w = Float.parseFloat(weight.getText().toString());
            h = Float.parseFloat(height.getText().toString());

            float r = (w / (h * h));
            /*result.setText("BMI"+"="+r);*/

            if (r < 18.5){
                bmilabel = getString(R.string.underweight);
            }
            else if ((r >= 18.5) && (r <= 24.9)) {
                bmilabel = getString(R.string.healthy_normal_weight);
            }
            else if ((r >= 25) && (r <= 29.9)){
                bmilabel = getString(R.string.overweight);
            }

            bmilabel = "BMI"+"="+r + "\n" + bmilabel;

            result.setText(bmilabel);
            Log.v(TAG,"Result"+result);
        }
    }


