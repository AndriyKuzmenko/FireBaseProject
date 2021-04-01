package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SortActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{
    ArrayList<String> studentsNames,studentsClasses,studentsGradesString;
    ArrayList<Student> studentsData;
    ArrayList<Integer> studentsGrades;
    String name;
    Student student;
    FirebaseDatabase database;
    DatabaseReference refStudents;
    AlertDialog.Builder adb;
    ArrayAdapter<String> adp,adp1;
    int position,id;
    ListView studentsList1;
    int byClass,byGrade,allVaccinated,allAlergic;
    Spinner spinner;

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
        studentsGradesString=new ArrayList<>();
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

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
                        studentsGradesString.add(""+t.getGrade());
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
        id=view.getId();

        if(id==byGrade)
        {
            adp1=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,studentsGradesString);
            spinner.setAdapter(adp1);
        }
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
        int x=5;
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        int grade=studentsGrades.get(position);
        ArrayList<String> list=new ArrayList<>();
        for(int i=0; i<studentsData.size(); i++)
        {
            if(studentsData.get(i).getGrade()==grade)
            {
                list.add(studentsNames.get(i));
            }
        }

        adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        studentsList1.setAdapter(adp);
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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
        else if(i==R.id.SecondScreen)
        {
            Intent si=new Intent(this, SecondVaccine.class);
            startActivity(si);
        }
        return true;
    }
}
