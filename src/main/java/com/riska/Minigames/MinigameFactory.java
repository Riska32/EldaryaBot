package com.riska.Minigames;

import com.riska.Minigames.Baskanoid.MinigameBaskanoid;
import com.riska.Minigames.FlowerPawer.MinigameFlowerPawer;
import com.riska.Minigames.InsectRush.MinigameInsectRush;
import com.riska.Minigames.MortalPillow.MiniGameMortalPillow;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class MinigameFactory
{
    static public IMinigame Create(MiniGameType type)
    {
        switch (type)
        {
            case FlowerPawer:
                return new MinigameFlowerPawer();
            case InsectRush:
                return new MinigameInsectRush();
            case Baskanoid:
                return new MinigameBaskanoid();
            case MortalPillow:
                return new MiniGameMortalPillow();
            default:
                throw new IllegalArgumentException(type.toString());
        }
    }
}
