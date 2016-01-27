package com.riska.MiniGames.CocooninPick;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

import java.util.Random;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameCocooninPick extends MiniGamesBase
{
    static GameHackParameters gameHackParameters =
            new GameHackParameters("hatchlings", 15000, 20);

    Random random = new Random();

    @Override
    protected GameHackParameters GetGameHackParameters()
    {
        return gameHackParameters;
    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-hatchlings\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Cocoonin'pick";
    }
}
