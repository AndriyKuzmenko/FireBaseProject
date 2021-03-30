package com.example.firebaseproject;

public class Student
{
    private int grade, class1;
    private Vaccine first, second;
    private boolean cant;

    public Student(int grade, int class1, boolean cant)
    {
        this.grade=grade;
        this.class1=class1;
        this.cant=cant;
        first=second=null;
    }

    public void setGrade(int Grade)
    {
        this.grade=grade;
    }

    public void setClass1(int class1)
    {
        this.class1=class1;
    }

    public void setCant(boolean cant)
    {
        this.cant=cant;
        if(cant)
        {
            first=second=new Vaccine();
        }
    }

    public void setFirst(Vaccine first)
    {
        this.first=first;
    }

    public void setSecond(Vaccine second)
    {
        this.second = second;
    }

    public int getGrade()
    {
        return grade;
    }

    public int getClass1()
    {
        return class1;
    }

    public boolean getCant()
    {
        return cant;
    }

    public Vaccine getFirst()
    {
       return first;
    }

    public Vaccine getSecond()
    {
        return second;
    }
}
