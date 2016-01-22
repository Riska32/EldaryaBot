package com.riska.Minigames.InsectRush;

import com.riska.logger.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.Semaphore;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class InsectKillerThread extends Thread
{
    private volatile boolean isFinish = false;

    private InsectType insectType;
    private Semaphore semaphore;
    private WebDriver page;

    private int insectWaitTimeoutSec = 2;

    public InsectKillerThread(WebDriver _page, Semaphore _semaphore, InsectType _insectType)
    {
        super(_insectType.toString());

        insectType = _insectType;
        semaphore = _semaphore;
        page = _page;

        isFinish = false;
    }

    public void Stop()
    {
        isFinish = true;
    }

    @Override
    public void run()
    {
        // TODO log about thread creation
        Logger.inst().Log(insectType + ": start");

        while(true)
        {
            if (isFinish)
                break;

            try
            {
                WebElement insect = (new WebDriverWait(page, insectWaitTimeoutSec))
                        .until(ExpectedConditions.presenceOfElementLocated(dispatchInsectElementByInsectType(insectType)));

                Logger.inst().Log(insectType + ": " + insect.getAttribute("id") + " " + insect.getAttribute("class"));
                ProcessInsect(insect);
            }
            catch (Exception ex)
            {
                Logger.inst().Log(insectType + ": exception");
                //do nothing or log message?
            }
        }

        // TODO log about thread deletion
        Logger.inst().Log(insectType + ": end");
    }
    private synchronized boolean ProcessInsect(WebElement insect)
    {
        try
        {
            semaphore.acquire();

            selectSpray(insectType);
            insect.click();
        }
        catch (InterruptedException ex)
        {
            // do nothing
        }
        finally
        {
            semaphore.release();
            Thread.yield();
            try { Thread.sleep(50); } catch (InterruptedException ex) {}
        }

        return true;
    }

    private void selectSpray(InsectType _insectType)
    {
        WebElement sprayDiv = page.findElement(By.id("sprays"));
        WebElement spray = sprayDiv.findElement(dispatchSprayElementByInsectType(_insectType));
        spray.click();
    }
    private By dispatchSprayElementByInsectType(InsectType _insectType)
    {
        switch (_insectType)
        {
            case Flyblue:
                return By.className("flyblue");
            case Bug:
                return By.className("bug");
            case Bee:
                return By.className("bee");
            default:
                // TODO throw exception
                return By.className("bug");
        }
    }
    private By dispatchInsectElementByInsectType(InsectType _insectType)
    {
        switch (_insectType)
        {
            case Flyblue:
                return By.cssSelector("#insects .flyblue");
            case Bug:
                return By.cssSelector("#insects .bug");
            case Bee:
                return By.cssSelector("#insects .bee");
            default:
                // TODO throw exception
                return By.cssSelector("#insects .bug");
        }
    }
}
