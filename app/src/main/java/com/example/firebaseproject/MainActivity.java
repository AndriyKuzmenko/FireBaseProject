package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText firstName, lastName, grade, class1;
    CheckBox canVaccinate;
    AlertDialog.Builder adb;
    DatabaseReference myRef;
    String name1;
    Student st;
    FirebaseDatabase database;
    ArrayList<String> studentsNames;

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

        database=FirebaseDatabase.getInstance("https://homework-9f97d-default-rtdb.firebaseio.com/");
        myRef=database.getReference("Students");
        studentsNames=new ArrayList<>();


        myRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String temp=data.getKey();
                    studentsNames.add(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void save(View view)
    {
        name1=firstName.getText().toString()+" "+lastName.getText().toString();
        if(studentsNames.contains(name1))
        {
            Toast.makeText(this,"You have already entered "+name1+"! Please enter another student!!!",Toast.LENGTH_LONG).show();
            return;
        }
        boolean bo=canVaccinate.isChecked();
        st=new Student(Integer.parseInt(grade.getText().toString()),Integer.parseInt(class1.getText().toString()),!bo);
        if(bo)
        {
            getDate(1);
        }
        else
        {
            myRef.child(name1).setValue(st);
        }
        studentsNames.add(name1);
    }

    public void getDate(final int x)
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
                getLocation(temp,x);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    public void getLocation(final int date, final int x)
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
                if(x==1)
                {
                    st.setFirst(temp);
                    askSecond();
                }
                else
                {
                    st.setSecond(temp);
                    myRef.child(name1).setValue(st);
                }
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    public void askSecond()
    {
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Did "+name1+" get two vaccines");
        adb.setCancelable(false);

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                getDate(2);
            }
        });

        adb.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                myRef.child(name1).setValue(st);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    /**
     * @param menu - the menu
     * @return - creates a menu with a list of all activities
     */

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    /**
     * @param item - the item that was selected
     * @return - when the user selects an item, this code will go to the specified activity.
     */

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i=item.getItemId();
        if(i==R.id.SecondScreen)
        {
            Intent si=new Intent(this, SecondVaccine.class);
            startActivity(si);
        }
        else if(i==R.id.SortScreen)
        {
            Intent si=new Intent(this, SortActivity.class);
            startActivity(si);
        }
        else if(i==R.id.creditsScreen)
        {
            Intent si=new Intent(this, CreditsActivity.class);
            startActivity(si);
        }
        return true;
    }
}
