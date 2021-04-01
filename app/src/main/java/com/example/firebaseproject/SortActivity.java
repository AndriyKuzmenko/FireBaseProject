package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SortActivity extends AppCompatActivity
{
    ArrayList<String> studentsNames,studentsClasses,list;
    ArrayList<Student> studentsData;
    ArrayList<Integer> studentsGrades;
    String name;
    Student student;
    FirebaseDatabase database;
    DatabaseReference refStudents;
    AlertDialog.Builder adb;
    ArrayAdapter<String> adp;
    int position;
    ListView studentsList1;
    int byClass,byGrade,allVaccinated,allAlergic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        studentsList1=(ListView)findViewById(R.id.studentsList1);
        database=FirebaseDatabase.getInstance("https://homework-9f97d-default-rtdb.firebaseio.com/");
        refStudents=database.getReference("Students");
        studentsNames=new ArrayList<>();
        studentsData=new ArrayList<>();
        studentsClasses=new ArrayList<>();
        studentsGrades=new ArrayList<>();

        byClass=R.id.byClass;
        byGrade=R.id.byGrade;
        allVaccinated=R.id.allVaccinated;
        allAlergic=R.id.allAlergic;

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
                    if(!studentsGrades.contains(t.getGrade()))
                    {
                        studentsGrades.add(t.getGrade());
                    }
                    if(!studentsClasses.contains(t.getGrade()+" "+t.getClass1()))
                    {
                        studentsClasses.add(t.getGrade()+" "+t.getClass1());
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
        adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studentsNames);
        studentsList1.setAdapter(adp);

        Toast.makeText(this,"Press on a student to see his data", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"You can filter the students using the filters on the bottom of the sreen.", Toast.LENGTH_LONG).show();
    }

    public void radioButtonPressed(View view)
    {
        int id=view.getId();

    }
}
