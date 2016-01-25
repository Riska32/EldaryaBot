package com.riska.MiniGames.CocooninPick;

import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameCocooninPick extends MiniGamesBase
{
    @Override
    protected void play()
    {

    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-hatchlings\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected By getStartButtonCondition()
    {
        return By.xpath("//*[@id=\"hatchlings-start-popup\"]/.//*[@class=\"button_blue_big\"]");
    }

    @Override
    protected By getEndButtonCondition()
    {
        return By.xpath("//*[@id=\"hatchlings-gameOver-popup\"]/.//*[@class=\"button_blue_big\"]");
    }

    @Override
    protected By getCheckGameOverResultCondition()
    {
        return By.xpath("//*[@id=\"hatchlings-gameOver-score\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Cocoonin'pick";
    }
}
