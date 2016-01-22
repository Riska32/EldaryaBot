package com.riska.Minigames.Baskanoid;

import com.riska.Minigames.MinigameBase;
import com.riska.logger.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class MinigameBaskanoid extends MinigameBase
{
    private int completeWaitTimeoutSec = 600;
    private int startButtonWaitTimeoutSec = 10;

    @Override
    protected String getGameName()
    {
        return "BaskanoidGame";
    }

    @Override
    protected By checkAvailablenessAction()
    {
        return By.id("baskanoid_game");
    }

    @Override
    protected void postEnterAction()
    {
        try
        {
            Alert alert = initParams.page.switchTo().alert();
            Logger.inst().Log(getGameName() + ": alert \"" + alert.getText() + "\" accepted");
            alert.accept();
            Thread.sleep(50);
        }
        catch (InterruptedException ex)
        {
            // do nothing
        }
        catch (Exception ex)
        {
            Logger.inst().Log(getGameName() + ": error when try access alert message");
        }
    }

    @Override
    protected void mainAction()
    {
        cancelFreezeOnLostFocus();

        try
        {
            startButtonClick();
            launchBall();
            incrementLevel();

            WebElement resultMessage = (new WebDriverWait(initParams.page, completeWaitTimeoutSec))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("end")));

            Logger.inst().Log(getGameName() + ": Result is " + resultMessage.getText());
        }
        catch (Exception ex)
        {
            // TODO only if debug
            Logger.inst().Log(getGameName() + ": exception:" + ex.getMessage());

            Logger.inst().Log(getGameName() + ": Cant wait notification message, continuing...");
        }
    }

    // other methods

    private void cancelFreezeOnLostFocus()
    {
        try {
            JavascriptExecutor js = (JavascriptExecutor)initParams.page;
            Object val =
                    js.executeScript("var Bask = MinigameView.Baskanoid;" +
                            "var stt = Bask.startGame.toSource().replace(\"window.onblur=function(){window.cancelRequestAnimFrame(b.timer)};window.onfocus=function(){w=(new Date).getTime();a(w)}\",\"\")" +
                            ".replace(/^\\(?function[^{]+{/i,\"\").replace(/}[^}]*$/i, \"\"); " +
                            "Beemoov.AmourSucre.View.Minigame.Baskanoid.startGame = new Function( stt );"
                    );
        }
        catch (Exception ex)
        {
            Logger.inst().Log(getGameName() + ": cancelFreezeOnLostFocus exception: " + ex.getMessage());
        }
    }

    private void incrementLevel()
    {
        try
        {
            JavascriptExecutor js = (JavascriptExecutor)initParams.page;

            js.executeScript("var bv = MinigameView.Baskanoid; var bm = bv.baskanoidModel; " +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());" +
                    "bm.increaseLevel(); bv.updateLevel(bm.getLevel());"
            );
        }
        catch (Exception ex)
        {
            Logger.inst().Log(getGameName() + ": incrementLevel exception: " + ex.getMessage());
        }
    }

    private void startButtonClick()
    {
        WebElement startButton = (new WebDriverWait(initParams.page, startButtonWaitTimeoutSec))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("start_insectrush")));
        startButton.click();
    }

    private void launchBall()
    {
        WebElement startButton = initParams.page.findElement(By.id("baskanoid_wrap"));
        startButton.click();
    }
}
