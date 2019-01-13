package com.riska.MiniGames.CocooninPick;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

public class MiniGameCocooninPick extends MiniGamesBase
{
    public MiniGameCocooninPick()
    {
        super(new GameHackParameters("hatchlings", 12000, 20));
    }

    @Override
    protected String getGameName()
    {
        return "Cocoonin'pick";
    }
}
