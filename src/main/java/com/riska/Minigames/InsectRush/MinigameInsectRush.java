package com.riska.Minigames.InsectRush;

import com.riska.Minigames.MinigameBase;
import com.riska.logger.Logger;
import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class MinigameInsectRush extends MinigameBase
{
    private int completeWaitTimeoutSec = 180;
    private int closeStartBarWaitTimeoutSec = 5;

    private int newSpawnIntervalMin = 150;
    private int newSpawnIntervalMax = 550;
    private int newSpawnIntervalRange = newSpawnIntervalMax - newSpawnIntervalMin;

    private List<InsectKillerThread> insectKillerThreads = new Vector<InsectKillerThread>(InsectType.values().length);
    private Semaphore semaphore = new Semaphore(1);

    @Override
    protected String getGameName()
    {
        return "InsectRushGame";
    }

    @Override
    protected By checkAvailablenessAction()
    {
        return By.id("insectrush_wrap");
    }

    @Override
    protected void mainAction()
    {
        intersectToGameCode();

        startInsectKillerThreads();

        try
        {
            startButtonClick();
            waitUntilStartBarClosed();

            WebElement resultMessage = (new WebDriverWait(initParams.page, completeWaitTimeoutSec))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("tutorial")));

            Logger.inst().Log(getGameName() + ": Result is " + resultMessage.getText());
        }
        catch (Exception ex)
        {
            // TODO only if debug
            Logger.inst().Log(getGameName() + ": exception:" + ex.getMessage());

            Logger.inst().Log(getGameName() + ": Cant wait notification message, continuing...");
        }
        finally
        {
            finishInsectKillerThreads();
        }
    }

    // main actions

    private void intersectToGameCode()
    {
        // меняем код игры для установки 9 уровны и более быстрого спавна жуков для того, чтобы не ждать долго
        JavascriptExecutor js = (JavascriptExecutor)initParams.page;
        try
        {
            Random rand = new Random();
            int newSpawnInterval = newSpawnIntervalMin + rand.nextInt(newSpawnIntervalRange);

            js.executeScript("var InsectRush = Beemoov.Namespace.create(\"Beemoov.AmourSucre.Model.Minigame.InsectRush\");" +
                    "var stt = InsectRush.toSource().replace(\"spawnInterval:1500\",\"spawnInterval:" + newSpawnInterval + "\").replace(\"nbWin:0\",\"nbWin:80\")" +
                    ".replace(/^\\(?function[^{]+{/i,\"\").replace(/}[^}]*$/i, \"\"); " +
                    "Beemoov.AmourSucre.Model.Minigame.InsectRush = new Function(\"a\",\"b\", stt );" +
                    "Beemoov.AmourSucre.Model.Minigame.InsectRush.Entity  = InsectRush.Entity;");
        }
        catch (Exception ex)
        {
            Logger.inst().Log(getGameName() + ": intersectToGameCode exception: " + ex.getMessage());
        }
    }

    private void startInsectKillerThreads()
    {
        // TODO assert insectKillerThreads list is empty
        for(int i = 0; i < InsectType.values().length; ++i)
        {
            InsectKillerThread thread = new InsectKillerThread(initParams.page,
                    semaphore, InsectType.values()[i]);

            insectKillerThreads.add(thread);

            thread.start();
        }
    }

    private void finishInsectKillerThreads()
    {
        // remove threads
        for (int i = 0; i < insectKillerThreads.size(); i++)
        {
            insectKillerThreads.get(i).Stop();
        }
        for (int i = 0; i < insectKillerThreads.size(); i++)
        {
            try { insectKillerThreads.get(i).join(); } catch (InterruptedException ex) {}
        }
    }
    private void startButtonClick()
    {
        WebElement startButton = initParams.page.findElement(By.id("start_insectrush"));
        startButton.click();
    }
    private void waitUntilStartBarClosed()
    {
        try
        {
            Boolean invisible = (new WebDriverWait(initParams.page, closeStartBarWaitTimeoutSec))
                    .until(ExpectedConditions.invisibilityOfElementLocated(By.id("tutorial")));
        }
        catch (Exception ex)
        {
           Logger.inst().Log(getGameName() + ": Cant wait till start bar closed, continuing...");
        }
    }
}
