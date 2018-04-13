package com.example.a.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {
    EditText et_type,et_message;
    Switch loc_switch;
    HelpUtils mHelpUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mHelpUtils=HelpUtils.getInstance(this);

        et_type=(EditText)findViewById(R.id.et_type);
        et_message=(EditText)findViewById(R.id.et_message);

        loc_switch=(Switch)findViewById(R.id.sw);

    }

    public void searchForHelp(View view){
        String type=et_type.getText().toString();
        String message=et_message.getText().toString();

        Boolean loc=loc_switch.isChecked();
        String location=(loc)?"fixed":"moving";
        mHelpUtils.help(type,message,location);
        Toast.makeText(this,"Help sent,Have a nice day ",Toast.LENGTH_SHORT).show();
        finish();
    }
}
