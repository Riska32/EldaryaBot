package com.riska.MiniGames;

/**
 * Created by Anastasia on 27.01.2016.
 */
public class GameHackParameters
{
    public String GameName;
    public int DesiredPlayTimeMSec;
    public int DesiredScore;

    public GameHackParameters(String gameName, int desiredPlayTimeMSec, int desiredScore)
    {
        GameName = gameName;
        DesiredPlayTimeMSec = desiredPlayTimeMSec;
        DesiredScore = desiredScore;
    }
}
