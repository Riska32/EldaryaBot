package com.riska.MiniGames.BubblTemple;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

public class MiniGameBubblTemple extends MiniGamesBase
{
    public MiniGameBubblTemple()
    {
        super(new GameHackParameters("flappy", 15000, 200));
    }

    @Override
    protected String getGameName()
    {
        return "Bubbl'temple";
    }
}
