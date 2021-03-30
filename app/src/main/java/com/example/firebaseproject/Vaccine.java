package com.example.firebaseproject;

public class Vaccine
{
    private int date;
    private String location;

    public Vaccine(int date, String location)
    {
        this.date=date;
        this.location=location;
    }

    public Vaccine()
    {
        date=-1;
        location="";
    }

    public void setDate(int date)
    {
        this.date=date;
    }

    public void setLocation(String location)
    {
        this.location=location;
    }

    private int getDate()
    {
        return date;
    }

    private String getLocation()
    {
        return location;
    }
}
