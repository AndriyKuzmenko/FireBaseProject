package com.example.firebaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CreditsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
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
        else if(i==R.id.InputScreen)
        {
            Intent si=new Intent(this, MainActivity.class);
            startActivity(si);
        }
        return true;
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
}
