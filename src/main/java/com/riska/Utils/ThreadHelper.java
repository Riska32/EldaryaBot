package com.riska.Utils;

/**
 * Created by Anastasia on 10.01.2016.
 */
public class ThreadHelper
{
    public static void Sleep(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex)
        {
        }
    }
}
