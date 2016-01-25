package com.riska.MiniGames.GemBomb;

import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameGemBomb extends MiniGamesBase
{
    Random random = new Random();

    @Override
    protected void play()
    {
        WebElement element = driver.findElement(By.xpath("//*[@id=\"gameCanvas\"]"));
        Actions builder = new Actions(driver);
        try
        {
            for (int i = 0; i < 10; i++)
            {

                builder.moveToElement(element, random.nextInt(400), random.nextInt(300)).click().build().perform();

            }
        } catch (Exception e)
        {
            System.out.println(getGameName() + " error play : " + e.getMessage());
        }
    }

    @Override
    protected void waitGameOver()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, getWaitGameOverTimeSec())).until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"gameCanvas\"]")));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 100, 10).click().build().perform();
        } catch (Exception e)
        {
            System.out.println(getGameName() + " : waitGameOver : " + e.getMessage());
        }
    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-peggle\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected By getStartButtonCondition()
    {
        return By.xpath("//*[@id=\"peggle-start-popup\"]/.//*[@class=\"button_blue_big\"]");
    }

    @Override
    protected By getEndButtonCondition()
    {
        return By.xpath("//*[@id=\"peggle-gameOver-popup\"]/.//*[@class=\"button_blue_big\"]");
    }

    @Override
    protected By getCheckGameOverResultCondition()
    {
        return By.xpath("//*[@id=\"peggle-gameOver-score\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Gem'bomb";
    }
}
