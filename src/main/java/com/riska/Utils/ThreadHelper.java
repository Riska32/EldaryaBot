package com.riska.Utils;

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
