package com.riska.MiniGames;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by SteaveP on 25.01.2016.
 */
public abstract class MiniGamesBase implements IMiniGame
{
    protected WebDriver driver;

    @Override
    public void Init(WebDriver page)
    {
        driver = page;
    }

    @Override
    public void Start()
    {
        if (checkAvailableness() == false)
        {
            System.out.println(getGameName() + " is not available");
            return;
        }

        enterTheGame();
        startGame();
        play();
        waitGameOver();
        gameOver();
    }

    protected boolean checkAvailableness()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 5)).until(
                    ExpectedConditions.presenceOfElementLocated(getEnterButtonCondition()));
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    protected void enterTheGame()
    {
        WebElement element = driver.findElement(getEnterButtonCondition());
        element.click();
    }

    protected void startGame()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 60)).until(
                    ExpectedConditions.presenceOfElementLocated(getStartButtonCondition()));
            element.click();
        } catch (Exception e)
        {
            System.out.println(getGameName() + " : startGame : " + e.getMessage());
        }
    }

    protected void gameOver()
    {
        checkGameOverResult();
        try
        {
            WebElement element = (new WebDriverWait(driver, 60)).until(
                    ExpectedConditions.presenceOfElementLocated(getEndButtonCondition()));
            element.click();
        } catch (Exception e)
        {
            System.out.println(getGameName() + " : gameOver : " + e.getMessage());
        }
    }

    protected void checkGameOverResult()
    {
        WebElement element = (new WebDriverWait(driver, getWaitGameOverTimeSec())).until(
                ExpectedConditions.presenceOfElementLocated(getCheckGameOverResultCondition()));
        System.out.println(getGameName() + " result is : " + element.getText());
    }

    protected abstract void play();
    protected abstract By getEnterButtonCondition();
    protected abstract By getStartButtonCondition();
    protected abstract By getEndButtonCondition();
    protected abstract By getCheckGameOverResultCondition();
    protected int getWaitGameOverTimeSec()
    {
        return 120;
    }
    protected void waitGameOver()
    {

    }
    protected String getGameName()
    {
        return "MiniGameBase";
    }
}
