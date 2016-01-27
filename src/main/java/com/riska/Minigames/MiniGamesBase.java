package com.riska.MiniGames;
import com.riska.Utils.ThreadHelper;
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
        gameOver();
    }

    protected boolean checkAvailableness()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 3)).until(
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
        ThreadHelper.Sleep(1000);
        try
        {
            WebElement element = (new WebDriverWait(driver, 60)).until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,\"start-popup\")]/.//*[contains(@class,\"button_blue_big\")]")));
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
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,\"gameOver-popup\")]/.//*[contains(@class,\"button_blue_big\")]")));
            element.click();
        } catch (Exception e)
        {
            System.out.println(getGameName() + " : gameOver : " + e.getMessage());
        }
        ThreadHelper.Sleep(300);
    }

    protected void checkGameOverResult()
    {
        WebElement element = (new WebDriverWait(driver, getWaitGameOverTimeSec())).until(
                ExpectedConditions.presenceOfElementLocated(getCheckGameOverResultCondition()));
        System.out.println(getGameName() + " result is : " + element.getText());
    }

    protected abstract void play();
    protected abstract By getEnterButtonCondition();
    protected abstract By getCheckGameOverResultCondition();
    protected int getWaitGameOverTimeSec()
    {
        return 130;
    }
    protected String getGameName()
    {
        return "MiniGameBase";
    }
}
