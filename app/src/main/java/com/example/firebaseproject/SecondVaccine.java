package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Originally, this activity was just for adding a second vaccine for students that only recorded info about the first one
//This is why it's called SecondVaccine

public class SecondVaccine extends AppCompatActivity implements View.OnCreateContextMenuListener
{
    ArrayList<String> studentsNames;
    ArrayList<Student> studentsData;
    ArrayList<Boolean> studentsNotSecond;
    ListView studentsList;
    String name;
    Student student;
    FirebaseDatabase database;
    DatabaseReference refStudents;
    AlertDialog.Builder adb;
    ArrayAdapter<String> adp;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_vaccine);

        studentsNames=new ArrayList<>();
        studentsData=new ArrayList<>();
        studentsNotSecond=new ArrayList<>();
        studentsList=(ListView)findViewById(R.id.studentsList);

        studentsList.setOnCreateContextMenuListener(this);

        database=FirebaseDatabase.getInstance("https://homework-9f97d-default-rtdb.firebaseio.com/");
        refStudents=database.getReference("Students");

        refStudents.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String temp=data.getKey();
                    Student t=data.getValue(Student.class);

                    studentsNames.add(temp);
                    studentsData.add(t);
                    studentsNotSecond.add(!t.getCant() && t.getSecond().getDate()==-1);
                }
                showData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void showData()
    {
        adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studentsNames);
        studentsList.setAdapter(adp);

        Toast.makeText(this,"Press on a student and enter info about his second vaccine", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Only students who got just one vaccine appear on this screen", Toast.LENGTH_LONG).show();
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
                student.setSecond(temp);
                refStudents.child(name).setValue(student);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add("Change class");
        menu.add("Change grade");

        AdapterView.AdapterContextMenuInfo menuInfo1=(AdapterView.AdapterContextMenuInfo)menuInfo;
        position=menuInfo1.position;
        name=studentsNames.get(position);
        student=studentsData.get(position);

        if(!student.getCant())
        {
            menu.add("Correct First date");
            menu.add("Correct First location");
        }
        if(student.getSecond().getDate()!=-1)
        {
            menu.add("Correct second date");
            menu.add("Correct second location");
        }
        else if(studentsNotSecond.get(position))
        {
            menu.add("Add Second vaccine");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        String s=item.getTitle().toString();

        if(s.equals("Add Second vaccine"))
        {
            getDate();
            studentsNotSecond.set(position,false);
        }

        return super.onContextItemSelected(item);
    }
}