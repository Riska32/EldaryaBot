package com.riska.MiniGames.CocooninPick;
import com.riska.MiniGames.MiniGamesBase;
import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Random;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameCocooninPick extends MiniGamesBase
{
    Random random = new Random();

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
