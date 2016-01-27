package com.riska.MiniGames.GemBomb;

import com.riska.MiniGames.MiniGamesBase;
import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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
            ThreadHelper.Sleep(1000);
            for (int i = 0; i < 10; i++)
            {
                builder.moveToElement(element, i*80 + 40, 300).click().build().perform();
                ThreadHelper.Sleep(200);
            }
            ThreadHelper.Sleep(14000);
            builder.moveToElement(element, 200, 20).click().build().perform();
        } catch (Exception e)
        {
            System.out.println(getGameName() + " error play : " + e.getMessage());
        }
    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-peggle\"]/.//*[@class=\"button_purple_big\"]");
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
