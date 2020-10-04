package com.example.ruber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String btn_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showDriver(View view) {
        btn_text =((Button) view).getText().toString();

        if(btn_text.equals("I'M A DRIVER")){
            Intent intent = new Intent(this,DriverLogin.class);
            startActivity(intent);
        }
    }

    public void showCustomer(View view) {
        btn_text =((Button) view).getText().toString();
        if (btn_text.equals("I'M A CUSTOMER")){
            Intent intent = new Intent(this,CustomerLogin.class);
            startActivity(intent);
        }
    }
}
