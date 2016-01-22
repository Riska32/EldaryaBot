package com.riska;

import com.riska.Exceptions.MiniGameNotAvailableException;
import com.riska.Familier.Familier;
import com.riska.Minigames.IMinigame;
import com.riska.Minigames.InitParams;
import com.riska.Minigames.MiniGameType;
import com.riska.Minigames.MinigameFactory;
import com.riska.logger.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

/**
 * Created by Anastasia on 05.01.2016.
 */

public class Core
{

    private WebDriver driver = null;

    private LoginInfo loginInfo = null;

    private boolean closeWebDriverAtExit = true;

    Map<MiniGameType, String> minigameTypeDict = new HashMap<MiniGameType, String>();

    public Core()
    {

    }

    public void init(LoginInfo _loginInfo)
    {
        loginInfo = _loginInfo;
       // minigameTypeDict.put(MiniGameType.FlowerPawer, "flowerpawer");
       // minigameTypeDict.put(MiniGameType.InsectRush, "insectrush");
       // minigameTypeDict.put(MiniGameType.Baskanoid, "baskanoid");
       // minigameTypeDict.put(MiniGameType.MortalPillow, "mortalpillow");

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
        dispatchMiniGames();

        //Перейти к фамильяру и отправить на охоту
        familier.hunt();

        //Перейти к фамильяру и покормить, если нужно
        familier.feed();
    }

    private void login(LoginInfo _loginInfo)
    {
        driver.get(_loginInfo.Site);

        // Find the text input element by its name
        WebElement loginField = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("header-login")));
        WebElement passwordField = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("header-password")));

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
        }
        catch (Exception ex)
        {
            return new Vector<WebElement>();
        }
    }

    private void goToMiniGames()
    {
        WebElement element = driver.findElement(By.id("main-menu-minigames"));
        element.click();
    }

    private void dispatchMiniGames()
    {
        goToMiniGames();

        //PlayGame(MiniGameType.Baskanoid);
        //PlayGame(MiniGameType.InsectRush);
        //PlayGame(MiniGameType.FlowerPawer);
        //PlayGame(MiniGameType.MortalPillow);
    }

    private void PlayGame(final MiniGameType desired)
    {
        List<WebElement> buttons = null;
        try
        {
            buttons = getMiniGameButtons();
        }
        catch(Exception ex)
        {
            Logger.inst().Log("Core:PlayGame: exception " + ex.getMessage());
        }

        try
        {
            Optional<WebElement> button = buttons.stream().filter(p -> p.getAttribute("href").contains(minigameTypeDict.get(desired))).findFirst();

            if (button.isPresent())
            {
                IMinigame minigame = MinigameFactory.Create(desired);

                InitParams initParams = new InitParams(driver, button.get());
                minigame.Init(initParams);
                minigame.Start();
            }
            else
            {
                Logger.inst().Log("Core:PlayGame: " + desired + " not found along " + buttons.size() + " buttons");
            }
        }
        catch (MiniGameNotAvailableException ex)
        {
            Logger.inst().Log("Core:PlayGame: " + desired + " not available");
        }
        catch (Exception ex)
        {
            Logger.inst().Log("Core:PlayGame: " + desired + " exception: " + ex.getMessage());
        }
    }
}
