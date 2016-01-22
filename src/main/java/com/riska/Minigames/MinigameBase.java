package com.riska.Minigames;

import com.riska.Exceptions.MiniGameNotAvailableException;
import com.riska.Exceptions.MinigameException;
import com.riska.logger.Logger;
import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Anastasia on 05.01.2016.
 */
public abstract class MinigameBase implements IMinigame {

    protected InitParams initParams;

    public void Init(InitParams _initParams)
    {
        initParams = _initParams;

        // TODO
    }

    public void Start() throws MinigameException
    {
        enterGame();

        postEnterAction();

        if (!checkAvailableness(checkAvailablenessAction()))
        {
            Logger.inst().Log(getGameName() + ": not available");

            isNotAvailableAction();

            exitToLobby();
            throw new MiniGameNotAvailableException();
        }

        mainAction();

        exitToLobby();
    }

    // overrided actions

    protected String getGameName()
    {
        // TODO add ID (incremental) at the end
        return "MinigameBase";
    }
    protected void postEnterAction()
    {
        // override in subclasses
    }
    protected void isNotAvailableAction()
    {
        // override in subclasses
    }
    protected void mainAction()
    {
        // override in subclasses
    }
    protected By checkAvailablenessAction()
    {
        // override in subclasses
        return By.id("nonexist");
    }

    // other

    protected boolean checkAvailableness(By condition)
    {
        try
        {
            WebElement resultMessage = (new WebDriverWait(initParams.page, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(condition));
        }
        catch (Exception ex)
        {
            //Logger.inst().Log(getGameName() + ": checkAvailableness: exception(" + condition.toString() + "): " + ex.getMessage());
            return false;
        }

        return true;
    }

    protected void enterGame()
    {
        initParams.startButton.click();
    }

    protected void exitToLobby()
    {
        initParams.page.navigate().back();

        // TODO check if is not in lobby, go to it
    }
}
