package com.riska.MiniGames.BubblTemple;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameBubblTemple extends MiniGamesBase
{
    public MiniGameBubblTemple()
    {
        super(new GameHackParameters("flappy", 12000, 200));
    }

    @Override
    protected String getGameName()
    {
        return "Bubbl'temple";
    }
}
