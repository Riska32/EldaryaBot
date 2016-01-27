package com.riska.MiniGames.BubblTemple;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameBubblTemple extends MiniGamesBase
{
    static GameHackParameters gameHackParameters =
            new GameHackParameters("flappy", 12000, 200);

    @Override
    protected GameHackParameters GetGameHackParameters()
    {
        return gameHackParameters;
    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-flappy\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Bubbl'temple";
    }
}
