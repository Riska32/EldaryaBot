package com.riska.Minigames;

import com.riska.Exceptions.MinigameException;

/**
 * Created by Anastasia on 05.01.2016.
 */

public interface IMinigame
{
    public void Init(InitParams initParams);
    public void Start() throws MinigameException;
}
