package com.riska.MiniGames.BubblTemple;

import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameBubblTemple extends MiniGamesBase
{
    @Override
    protected void play()
    {

    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-flappy\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected By getCheckGameOverResultCondition()
    {
        return By.xpath("//*[@id=\"flappy-gameOver-score\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Bubbl'temple";
    }
}
