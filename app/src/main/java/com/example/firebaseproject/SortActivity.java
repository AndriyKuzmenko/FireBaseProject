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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class SortActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{
    ArrayList<String> studentsNames,studentsClasses,studentsGradesString,vaccinatedStudents,alergicStudents;
    ArrayList<Student> studentsData,list1;
    ArrayList<Integer> studentsGrades;
    FirebaseDatabase database;
    DatabaseReference refStudents;
    AlertDialog.Builder adb;
    ArrayAdapter<String> adp,adp1;
    int id;
    ListView studentsList1;
    int byClass,byGrade,allVaccinated,allAlergic;
    Spinner spinner;
    ArrayList<String> list;

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
        vaccinatedStudents=new ArrayList<>();
        alergicStudents=new ArrayList<>();
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        studentsList1.setOnItemClickListener(this);

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
                    if(t.getCant())
                    {
                        alergicStudents.add(temp);
                    }
                    else
                    {
                        vaccinatedStudents.add(temp);
                    }
                }
                showData(studentsNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /**
     * @param studentsNames - an array list with all the names of the sudents that need to be shown on the screen
     * @return shows all the students on a ListView.
     */

    public void showData(ArrayList<String> studentsNames)
    {
        adp=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, studentsNames);
        studentsList1.setAdapter(adp);
        list=studentsNames;

        if(studentsNames!=this.studentsNames) return;
        Toast.makeText(this,"Press on a student to see his data", Toast.LENGTH_LONG).show();
        Toast.makeText(this,"You can filter the students using the filters on the bottom of the sreen.", Toast.LENGTH_LONG).show();
    }

    /**
     * @param view - the radio button pressed
     * @return this method checks which radioButtton was pressed and filters the students.
     */
    public void radioButtonPressed(View view)
    {
        this.id=view.getId();

        if(id==byGrade)
        {
            adp1=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,studentsGradesString);
            spinner.setAdapter(adp1);
        }
        else if(id==allVaccinated)
        {
            adp1=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,new String[]{});
            spinner.setAdapter(adp1);
            showData(vaccinatedStudents);
        }
        else if(id==allAlergic)
        {
            adp1=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,new String[]{});
            spinner.setAdapter(adp1);
            showData(alergicStudents);
        }
        else if(id==byClass)
        {
            adp1=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,studentsClasses);
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
        adb=new AlertDialog.Builder(this);
        adb.setTitle("Student Info");
        String st=list.get(position);
        int i=studentsNames.indexOf(st);
        Student stu=studentsData.get(i);
        String message="Name: "+st+"\nClass: "+stu.getGrade()+" "+stu.getClass1()+"\nAlergic: "+stu.getCant();
        if(!stu.getCant())
        {
            message+="\nFirst Vaccine date: "+dateToString(stu.getFirst().getDate())+"\nFirst Vaccine location: "+stu.getFirst().getLocation();
            if(stu.getSecond().getDate()!=-1)
            {
                message+="\nSecond Vaccine date: "+dateToString(stu.getSecond().getDate())+"\nSecond Vaccine location: "+stu.getSecond().getLocation();
            }
        }
        adb.setMessage(message);
        AlertDialog ad=adb.create();
        ad.show();
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
     * @return This function creates a list of studets.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(this.id==byGrade)
        {
            int grade=studentsGrades.get(position);
            list=new ArrayList<>();
            list1=new ArrayList<>();
            for (int i=0; i<studentsData.size(); i++)
            {
                if (studentsData.get(i).getGrade()==grade)
                {
                    list.add(studentsNames.get(i));
                    list1.add(studentsData.get(i));
                }
            }
            list=sortByClass(list,list1);

            showData(list);
        }
        else if(this.id==byClass)
        {
            String[] class1=studentsClasses.get(position).split(" ");
            list=new ArrayList<>();
            int grade=Integer.parseInt(class1[0]);
            int class2=Integer.parseInt(class1[1]);
            for (int i=0; i<studentsData.size(); i++)
            {
                if (studentsData.get(i).getGrade()==grade && studentsData.get(i).getClass1()==class2)
                {
                    list.add(studentsNames.get(i));
                }
            }

            showData(list);
        }
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
        else if(i==R.id.creditsScreen)
        {
            Intent si=new Intent(this, CreditsActivity.class);
            startActivity(si);
        }
        return true;
    }

    /**
     * @param x - the sate stored in an integer variable
     * @return converts it to string. 12115678 - > 12/11/5678
     */

    public String dateToString(int x)
    {
        return (x/1000000)+"/"+(x%10000/100)+"/"+(x%10000);
    }

    /**
     * Deletes the info about this acticity once the user exits.
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

    public ArrayList<String> sortByClass(ArrayList<String> b, ArrayList<Student> a)
    {
        for(int i=0; i<a.size(); i++)
        {
            for(int j=0; j<a.size()-1; j++)
            {
                if(a.get(j).getClass1()>a.get(j+1).getClass1())
                {
                    Student temp = a.get(j);
                    a.set(j, a.get(j + 1));
                    a.set(j + 1, temp);

                    String t = b.get(j);
                    b.set(j, b.get(j + 1));
                    b.set(j + 1, t);
                }
            }
        }
        return b;
    }
}