package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
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

public class SecondVaccine extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ArrayList<String> studentsNames;
    ArrayList<Student> studentsData;
    ListView studentsList;
    String name;
    Student student;
    FirebaseDatabase database;
    DatabaseReference refStudents;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_vaccine);

        studentsNames=new ArrayList<>();
        studentsData=new ArrayList<>();
        studentsList=(ListView)findViewById(R.id.studentsList);

        studentsList.setOnItemClickListener(this);

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

                    if(t.getFirst().getDate()!=-1 && t.getSecond().getDate()==-1)
                    {
                        studentsNames.add(temp);
                        studentsData.add(t);
                    }
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
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studentsNames);
        studentsList.setAdapter(adp);

        Toast.makeText(this,"Press on a student and enter info about his second vaccine", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"Only students who got just one vaccine appear on this screen", Toast.LENGTH_LONG).show();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        name=studentsNames.get(position);
        student=studentsData.get(position);
        getDate();
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
}