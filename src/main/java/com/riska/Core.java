package com.riska;

import com.riska.Familier.Familier;
import com.riska.MiniGames.BubblTemple.MiniGameBubblTemple;
import com.riska.MiniGames.CocooninPick.MiniGameCocooninPick;
import com.riska.MiniGames.GemBomb.MiniGameGemBomb;
import com.riska.MiniGames.IMiniGame;
import com.riska.MiniGames.MiniGameType;
import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

public class Core
{

    private WebDriver driver = null;

    private LoginInfo loginInfo = null;

    private boolean closeWebDriverAtExit = true;

    public Core()
    {

    }

    public void init(LoginInfo _loginInfo)
    {
        loginInfo = _loginInfo;

        System.setProperty("webdriver.log.file", "d:\temp\fxlogfile1.log");
        System.setProperty("webdriver.firefox.logfile", "d:\temp\fxlogfile2.log");
        System.setProperty("webdriver.edge.driver", "C:\\Program Files (x86)\\Microsoft Web Driver\\MicrosoftWebDriver.exe");

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.

        final FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("xpinstall.signatures.required", false);
        firefoxProfile.setEnableNativeEvents(true);

        if (loginInfo.hasProxy())
        {
            firefoxProfile.setPreference("network.proxy.type", 1);
            firefoxProfile.setPreference("network.proxy.http", loginInfo.ProxyHost);
            firefoxProfile.setPreference("network.proxy.http_port", loginInfo.ProxyPort);
        }

        driver = new FirefoxDriver(firefoxProfile);
    }

    public void shutdown()
    {
        if (closeWebDriverAtExit)
        {
            if (driver != null)
                driver.quit();
        }
    }

    public void run()
    {
        login(loginInfo);
        Familier familier = new Familier(driver);

        //Перейти к фамильяру и забрать награду
        familier.getReward();

        //Перейти к минииграм и отыграть миниигры
        playMiniGames();

        //Перейти к фамильяру и отправить на охоту
        familier.hunt();

        //Перейти к фамильяру и покормить, если нужно
        familier.feed();
    }

    private void login(LoginInfo _loginInfo)
    {
        driver.get(_loginInfo.Site);

        // Find the text input element by its name
        WebElement loginField = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("header-login")));
        WebElement passwordField = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("header-password")));

        // Enter something to search for
        loginField.sendKeys(_loginInfo.Login);
        passwordField.sendKeys(_loginInfo.Password);

        // Now submit the form. WebDriver will find the form for us from the element
        WebElement element = driver.findElement(By.className("button_blue_small"));
        element.click();

        // TODO check was login successful
    }

    private List<WebElement> getMiniGameButtons()
    {
        try
        {
            List<WebElement> buttons = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("auto-button-pink")));

            return buttons;
        } catch (Exception ex)
        {
            return new Vector<WebElement>();
        }
    }

    private void goToMiniGames()
    {
        //driver.navigate().to("http://www.eldarya.com/#/minigames");
        WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("main-menu-minigames")));
        element.click();
    }

    private void playMiniGames()
    {
        goToMiniGames();
        playMiniGame(MiniGameType.GemBomb);
        playMiniGame(MiniGameType.BubblTemple);
        playMiniGame(MiniGameType.CocooninPick);
        ThreadHelper.Sleep(2000);
    }

    private void playMiniGame(MiniGameType miniGameType)
    {
        try
        {
            IMiniGame miniGame = createGame(miniGameType);
            miniGame.Init(driver);
            miniGame.Start();
        }
        catch (Exception e)
        {
            System.out.println("PlayMiniGame : " + miniGameType + " : " + e.getMessage());
        }
    }

    private IMiniGame createGame(MiniGameType miniGameType) throws Exception
    {
        switch (miniGameType)
        {
            case GemBomb:
                return new MiniGameGemBomb();
            case CocooninPick:
                return new MiniGameCocooninPick();
            case BubblTemple:
                return new MiniGameBubblTemple();
            default: throw new Exception("createGame erorr" + miniGameType);
        }
    }
}
