package com.riska.MiniGames;

import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.*;
import com.riska.logger.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by SteaveP on 25.01.2016.
 */
public abstract class MiniGamesBase implements IMiniGame
{
    protected WebDriver driver;
    protected GameHackParameters gameHackParameters;

    protected int checkAvailablenessWaitSec = 20;
    protected int loadGameWaitSec = 60;
    protected int exitGameWaitSec = 10;
    protected int playTimeDelayMSec = 500;

    protected By exitGameButtonLocator = By.xpath("//*[contains(@id,\"start-popup\")]/.//*[contains(@class,\"genericPopupClose\")]");

    public MiniGamesBase(GameHackParameters _gameHackParameters)
    {
        gameHackParameters = _gameHackParameters;

        /*if (gameHackParameters == null)
        {
            throw new Exception();
        }*/
    }

    @Override
    public void Init(WebDriver page)
    {
        driver = page;
    }

    @Override
    public void Start()
    {
        if (checkAvailableness() == false)
        {
            Logger.inst().Log(getGameName() + " is not available");
            return;
        }

        enterTheGame();
        waitForGameLoad();
        hackGame();
        exitGame();
    }

    protected WebElement getMinigameEnterBlock(int timeoutSec)
    {
        return (new WebDriverWait(driver, timeoutSec)).until(
                ExpectedConditions.elementToBeClickable(By.xpath( "//*[@id=\"minigames-" + gameHackParameters.GameName + "\"]")));
    }

    protected boolean checkAvailableness()
    {
        try
        {
            WebElement element = getMinigameEnterBlock(checkAvailablenessWaitSec);

            return element != null && element.getAttribute("class").toLowerCase().contains("playable");
        } catch (Exception e)
        {
            return false;
        }
    }

    protected void enterTheGame()
    {
        try
        {
            WebElement baseElement = getMinigameEnterBlock(checkAvailablenessWaitSec);
            baseElement.findElement(By.xpath(".//*[@class=\"button_purple_big\"]")).click();
        }
        catch (Exception e)
        {
            Logger.inst().Log(getGameName() + " : enterTheGame() exception: " + e.getMessage());
        }
    }

    private void waitForGameLoad()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, loadGameWaitSec)).until(
                    ExpectedConditions.elementToBeClickable(exitGameButtonLocator));
        }
        catch (WebDriverException e)
        {
            // continue
        }
    }

    protected void hackGame()
    {
        Logger.inst().Log(getGameName() + " : start hack the game");

        try
        {
            if (gameHackParameters == null)
            {
                Logger.inst().Log(getGameName() + " : hackGame : GameHackParameters is null");
                return;
            }

            JavascriptExecutor js = (JavascriptExecutor)driver;

            js.executeScript(
                "currentGame = arguments[0];\n" +
                "currentGameDelayTimeMilliSecs = arguments[1];\n" +
                "score_desired = arguments[2];\n" +
                "\n" +
                "result_exp = 0;\n" +
                "result_maana = 0;\n" +
                "\n" +
                "function sendDesiredScore(gameToken, score_desired)\n" +
                "{\n" +
                "    var emil_e = new EMiL_E();\n" +
                "    //emil_e.init();\n" +
                "\n" +
                "    console.log(\"send game \" + currentGame + \" desired score: \" + score_desired);\n" +
                "\n" +
                "    $.ajax({\n" +
                "        url: '/minigames/ajax_getPrizes',\n" +
                "        type: 'post',\n" +
                "        dataType: 'json',\n" +
                "        data: {game:currentGame, score:score_desired},\n" +
                "        success: function(json)\n" +
                "        {\n" +
                "            console.log(\"Result of ajax query:\");\n" +
                "            console.log(json);\n" +
                "\n" +
                "            console.log(\"result exp=\" + json.exp + \" maana=\" + json.maana);\n" +
                "            result_exp = json.exp;\n" +
                "            result_maana = json.maana;\n" +
                "\n" +
                "            console.log(\"sending encrypted token with score...\");\n" +
                "            var enc_token = emil_e.score.xorEncode(gameToken, score_desired.toString());\n" +
                "            emil_e.score.send(enc_token, score_desired, currentGame);\n" +
                "            console.log(\"sending encrypted token with score... done\");\n" +
                "        }\n" +
                "    }); \n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "function hackTheGame()\n" +
                "{\n" +
                "    console.log(\"try to get hacked game \" + currentGame + \" token\");\n" +
                "\n" +
                "    $.ajax({\n" +
                "        url: '/minigames/ajax_startGame',\n" +
                "        type: 'post',\n" +
                "        dataType: 'json',\n" +
                "        data: {game:currentGame},\n" +
                "        success: function(json)\n" +
                "        {\n" +
                "            console.log(\"Result of ajax query:\");\n" +
                "            console.log(json);\n" +
                "\n" +
                "\n" +
                "            if(json.result == 'success')\n" +
                "            {\n" +
                "                var gameToken = json.data;\n" +
                "\n" +
                "                console.log(\"Success! Game token is \" + gameToken);\n" +
                "\n" +
                "                console.log(\"Wait for \" + currentGameDelayTimeMilliSecs + \" milliseconds...\");\n" +
                "\n" +
                "                setTimeout(function(){\n" +
                "                    sendDesiredScore(gameToken, score_desired);\n" +
                "                }, currentGameDelayTimeMilliSecs);\n" +
                "            }\n" +
                "            else\n" +
                "            {\n" +
                "                console.log(\"Result is not success, aborting\");\n" +
                "            }\n" +
                "        }\n" +
                "    });\n" +
                "}\n" +
                "\n" +
                "hackTheGame();", gameHackParameters.GameName, gameHackParameters.DesiredPlayTimeMSec, gameHackParameters.DesiredScore);

            ThreadHelper.Sleep(gameHackParameters.DesiredPlayTimeMSec + playTimeDelayMSec);

            String result_exp = "empty";
            String result_maana = "empty";

            try
            {
                result_exp = js.executeScript("return result_exp;").toString();
            }
            catch (Exception e)
            {
                Logger.inst().Log(getGameName() + " : return result_exp exception: " + e.getMessage());
            }

            try
            {
                result_maana = js.executeScript("return result_maana;").toString();
            }
            catch (Exception e)
            {
                Logger.inst().Log(getGameName() + " : return result_maana exception: " + e.getMessage());
            }

            // print result to console
            Logger.inst().Log(getGameName() + " : reward : exp = " + result_exp + " maana = " + result_maana);
        }
        catch (Exception e)
        {
            Logger.inst().Log(getGameName() + " : hackGame js code : " + e.getMessage());
        }
    }

    protected void exitGame()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, exitGameWaitSec)).until(
                    ExpectedConditions.elementToBeClickable(exitGameButtonLocator));
            element.click();
        }
        catch (Exception e)
        {
            Logger.inst().Log(getGameName() + " : exitGame : " + e.getMessage());
        }
    }

    protected String getGameName()
    {
        return "MiniGameBase";
    }
}
