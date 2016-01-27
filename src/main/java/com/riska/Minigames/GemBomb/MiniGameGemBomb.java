package com.riska.MiniGames.GemBomb;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameGemBomb extends MiniGamesBase
{
    public MiniGameGemBomb()
    {
        super(new GameHackParameters("peggle", 5000, 10));
    }

    @Override
    protected String getGameName()
    {
        return "Gem'bomb";
    }
}
