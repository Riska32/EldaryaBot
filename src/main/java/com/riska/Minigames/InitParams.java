package com.riska.Minigames;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class InitParams
{
    public WebDriver page = null;
    public WebElement startButton = null;

    public InitParams(WebDriver webDriver, WebElement _startButton)
    {
        page = webDriver;
        startButton = _startButton;
    }
    // TODO
}
