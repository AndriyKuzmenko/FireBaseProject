package com.example.firebaseproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    EditText firstName, lastName, grade, class1;
    CheckBox canVaccinate;
    AlertDialog.Builder adb;
    DatabaseReference myRef;
    String name1;
    Student st;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName=(EditText)findViewById(R.id.firstName);
        lastName=(EditText)findViewById(R.id.lastName);
        grade=(EditText)findViewById(R.id.grade);
        class1=(EditText)findViewById(R.id.class1);
        canVaccinate=(CheckBox)findViewById(R.id.canVaccinate);
    }

    public void save(View view)
    {
        FirebaseDatabase database=FirebaseDatabase.getInstance("https://homework-9f97d-default-rtdb.firebaseio.com/");
        name1=firstName.getText().toString()+" "+lastName.getText().toString();
        myRef=database.getReference("Students");
        boolean bo=canVaccinate.isChecked();
        st=new Student(Integer.parseInt(grade.getText().toString()),Integer.parseInt(class1.getText().toString()),!bo);
        if(bo)
        {
            getDate();
        }
        else
        {
            myRef.child(name1).setValue(st);
        }
    }

    public void getDate()
    {
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Choose date");
        final DatePicker d1=new DatePicker(this);
        adb.setView(d1);

        adb.setPositiveButton("Save Date", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int temp=d1.getYear()+d1.getMonth()*10000+d1.getDayOfMonth()*1000000;
                getLocation(temp);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    public void getLocation(final int date)
    {
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Choose location");
        final EditText e=new EditText(this);
        e.setHint("Location");
        adb.setView(e);

        adb.setPositiveButton("Save Location", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String location=e.getText().toString();
                Vaccine temp=new Vaccine(date,location);
                st.setFirst(temp);
                myRef.child("Students").child(name1).setValue(st);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }
}
