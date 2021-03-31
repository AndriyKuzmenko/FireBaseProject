package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecondVaccine extends AppCompatActivity
{
    ArrayList<String> studentsNames;
    ArrayList<Student> studentsData;
    ListView studentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_vaccine);

        studentsNames=new ArrayList<>();
        studentsData=new ArrayList<>();
        studentsList=(ListView)findViewById(R.id.studentsList);

        FirebaseDatabase database=FirebaseDatabase.getInstance("https://homework-9f97d-default-rtdb.firebaseio.com/");
        DatabaseReference refStudents=database.getReference("Students");

        refStudents.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String temp=data.getKey();
                    studentsNames.add(temp);
                    studentsData.add(data.getValue(Student.class));
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
}