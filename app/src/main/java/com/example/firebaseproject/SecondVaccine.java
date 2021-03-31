package com.example.firebaseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class SecondVaccine extends AppCompatActivity
{
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_vaccine);

        Toast.makeText(this,"Press on a student and enter info about his second vaccine", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Only students who got just one vaccine appear on this screen", Toast.LENGTH_LONG).show();
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
