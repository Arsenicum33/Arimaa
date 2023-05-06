package cz.cvut.fel.pjv.Models;


public class Time
{
    int minutes, seconds;
    public Time(int milliseconds)
    {
        seconds = milliseconds/1000;
        minutes = seconds/60;
        seconds=seconds%60;
    }

    @Override
    public String toString()
    {
        return minutes+":"+seconds;
    }
}
