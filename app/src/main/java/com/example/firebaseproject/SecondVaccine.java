package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
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

        Toast.makeText(this,"This is a list of all the students you entered", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Long click on the one you want to update and choose which parameter you want to update", Toast.LENGTH_LONG).show();
    }

    public void getDate(final int b)
    {
        adb=new AlertDialog.Builder(this);
        if(b==0)adb.setTitle("Choose date");
        else if(b==1) adb.setTitle("First Date: "+dateToString(student.getFirst().getDate()));
        else if(b==2) adb.setTitle("Second Date: "+dateToString(student.getSecond().getDate()));
        final DatePicker d1=new DatePicker(this);
        adb.setView(d1);

        adb.setPositiveButton("Save Date", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int temp=d1.getYear()+d1.getMonth()*10000+d1.getDayOfMonth()*1000000;
                if(b==0) getLocation(temp);
                else if (b==1)
                {
                    student.getFirst().setDate(temp);
                    refStudents.child(name).setValue(student);
                }
                else if (b==2)
                {
                    student.getSecond().setDate(temp);
                    refStudents.child(name).setValue(student);
                }
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
        if(date==1)
        {
            e.setText(student.getFirst().getLocation());
        }
        else if(date==2)
        {
            e.setText(student.getSecond().getLocation());
        }
        e.setHint("Location");
        adb.setView(e);

        adb.setPositiveButton("Save Location", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String location=e.getText().toString();
                Vaccine temp;
                if(date>2)
                {
                    temp=new Vaccine(date,location);
                    student.setSecond(temp);
                }
                else if(date==2)
                {
                    temp=student.getSecond();
                    temp.setLocation(location);
                }
                else
                {
                    temp=student.getFirst();
                    temp.setLocation(location);
                }
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
            getDate(0);
            studentsNotSecond.set(position,false);
        }
        else if(s.equals("Correct First date"))
        {
            getDate(1);
        }
        else if(s.equals("Correct second date"))
        {
            getDate(2);
        }
        else if(s.equals("Correct First location"))
        {
            getLocation(1);
        }
        else if(s.equals("Correct second location"))
        {
            getLocation(2);
        }
        else if(s.equals("Change class"))
        {
            changeClass();
        }

        return super.onContextItemSelected(item);
    }

    public void changeClass()
    {
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Change class");
        adb.setMessage("Please type two numebrs: the grade and the class. 11E=11 5");
        final EditText e=new EditText(this);
        e.setText(student.getGrade()+" "+student.getClass1());
        e.setHint("Class");
        adb.setView(e);

        adb.setPositiveButton("Save Location", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String[] temp=e.getText().toString().split(" ");
                student.setGrade(Integer.parseInt(temp[0]));
                student.setClass1(Integer.parseInt(temp[1]));
                refStudents.child(name).setValue(student);
            }
        });

        AlertDialog ad=adb.create();
        ad.show();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i=item.getItemId();
        if(i==R.id.InputScreen)
        {
            Intent si=new Intent(this, MainActivity.class);
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

    public String dateToString(int x)
    {
        return (x/1000000)+"/"+(x%10000/100)+"/"+(x%10000);
    }
}