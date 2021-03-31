package com.example.firebaseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

public class SecondVaccine extends AppCompatActivity
{
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_vaccine);

        //Toast.makeText(this,"This is a list of all students who did just one vaccine. If one of them did two, press on his name and enter info about the second vaccine", Toast.LENGTH_LONG);
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Press on a student and enter info about his second vaccine");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //exit
            }
        });
        AlertDialog a=adb.create();
        a.show();
    }
}
