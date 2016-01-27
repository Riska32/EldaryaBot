package com.riska.MiniGames.CocooninPick;

import com.riska.MiniGames.GameHackParameters;
import com.riska.MiniGames.MiniGamesBase;
import org.openqa.selenium.By;

/**
 * Created by SteaveP on 25.01.2016.
 */
public class MiniGameCocooninPick extends MiniGamesBase
{
    public MiniGameCocooninPick()
    {
        super(new GameHackParameters("hatchlings", 15000, 20));
    }

    @Override
    protected String getGameName()
    {
        return "Cocoonin'pick";
    }
}
