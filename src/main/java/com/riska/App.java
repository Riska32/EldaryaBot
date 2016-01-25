package com.riska;

import com.riska.Utils.ThreadHelper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LoginInfo[] logins = new LoginInfo[]
        {
        };

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
                System.out.println(String.format("Try only %d/%d of %s at %s...",
                        i + 1, logins.length, li.Login, li.Site));

                core = new Core();
                core.init(li);
                core.run();
            } catch (Throwable ex)
            {
                System.out.println("Unhandled exception: " + ex.getMessage());
            } finally
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException ex)
                {
                }
                if (core != null)
                    core.shutdown();
            }
        }
    }
}
