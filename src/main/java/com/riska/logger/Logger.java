package com.riska.logger;

import java.util.Date;

public class Logger
{
    private static Logger ourInstance = new Logger();

    public static Logger inst()
    {
        return ourInstance;
    }

    private boolean isEnableLogging = true;

    private Logger()
    {
    }

    public void Log(String message)
    {
        // TODO insert date and time first
        System.out.println(message);
    }
    public boolean isEnableLogging()
    {
        return isEnableLogging;
    }
    public void seEnableLogging(boolean value)
    {
        isEnableLogging = value;
    }

}
