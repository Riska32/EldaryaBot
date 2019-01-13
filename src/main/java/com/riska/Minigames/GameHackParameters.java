package com.riska.MiniGames;

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
