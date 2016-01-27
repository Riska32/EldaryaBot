package com.riska.MiniGames.GemBomb;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

import java.util.Random;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameGemBomb extends MiniGamesBase
{
    static GameHackParameters gameHackParameters =
            new GameHackParameters("peggle", 5000, 10);

    Random random = new Random();

    @Override
    protected GameHackParameters GetGameHackParameters()
    {
        return gameHackParameters;
    }

    @Override
    protected By getEnterButtonCondition()
    {
        return By.xpath("//*[@id=\"minigames-peggle\"]/.//*[@class=\"button_purple_big\"]");
    }

    @Override
    protected String getGameName()
    {
        return "Gem'bomb";
    }
}
