package com.riska;

import com.riska.Utils.ThreadHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App 
{
    public static void main( String[] args )
    {
        LoginInfo[] logins = new LoginInfo[]
        {
            new LoginInfo("http://www.eldarya.ru/", "test_login1@gmail.com", "password"), // last 2 parametrs can be proxy ip adress and port number. Ex "168.1.47.248", 8080

        };

        // accountIndex is used for split jobs to several instances
        int accountIndex = args.length > 0 && args[0].equals("2") ? 1 : 0;

        int startindex = 0, endindex = 0;

        if (accountIndex == 0)
        {
            startindex = 0;
            endindex = logins.length/2;
        }
        else
        {
            startindex = logins.length/2;
            endindex = logins.length;
        }

        for (int i = startindex; i<endindex; i++)
        {
            LoginInfo li = logins[i];
            Core core = null;
            try
            {
                System.out.println(String.format("\n%s: Try only %d/%d of %s at %s ...",
                        getCurrentTimestamp(), i + 1, logins.length, li.Login, li.Site));

                core = new Core();
                core.init(li);
                core.run();
            } catch (Throwable ex)
            {
                System.out.println("Unhandled exception: " + ex.getMessage());
            } finally
            {
                if (core != null)
                    core.shutdown();
            }
        }
    }

    static String getCurrentTimestamp()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Date now = new Date();

        return dateFormat.format(now);
    }
}
