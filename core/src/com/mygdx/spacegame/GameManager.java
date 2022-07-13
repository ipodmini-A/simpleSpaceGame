package com.mygdx.spacegame;

import java.util.concurrent.TimeUnit;

//Currently this "GameManager" manages time.
public class GameManager
{
   private float startTime;
   private float currentTime;
   private float elapsedTime;
    //These need to be put in a separate class. Managing the settings of the game requires some work.
    //Currently these values don't do anything.
   private int windowWidth = 640;
   private int windowHeight = 480;
   private int stages = 1;

   public GameManager()
   {
        startTime = System.nanoTime();
   }

    public float getStartTime()
    {
        return startTime;
    }

    public void setStartTime(float startTime)
    {
        this.startTime = startTime;
    }

    public float getCurrentTime()
    {
        currentTime = System.nanoTime();
        return currentTime;
    }

    public void setCurrentTime(float currentTime)
    {
        this.currentTime = currentTime;
    }

    public float getElapsedTime()
    {
        elapsedTime = TimeUnit.MILLISECONDS.convert((long) (getCurrentTime() - getStartTime()), TimeUnit.NANOSECONDS);
        return elapsedTime ;
    }

    public void resetElapsedTime()
    {
        setStartTime(System.nanoTime());
    }

    public void setElapsedTime(float elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    public int getWindowWidth()
    {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth)
    {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight()
    {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight)
    {
        this.windowHeight = windowHeight;
    }

    public int getStages()
    {
        return stages;
    }

    public void setStages(int stages)
    {
        this.stages = stages;
    }
}
