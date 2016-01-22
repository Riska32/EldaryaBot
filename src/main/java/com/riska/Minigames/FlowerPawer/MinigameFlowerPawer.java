package com.riska.Minigames.FlowerPawer;

import com.riska.Minigames.MinigameBase;
import com.riska.logger.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class MinigameFlowerPawer extends MinigameBase
{
    private int completeWaitTimeoutSec = 20;

    @Override
    protected String getGameName()
    {
        return "FlowerPawerGame";
    }

    @Override
    protected By checkAvailablenessAction()
    {
        return By.id("flowerpawerMap");
    }

    @Override
    protected void mainAction()
    {
        WebElement element = initParams.page.findElement(By.id("flowerpawerMap"));
        List<WebElement> areas = element.findElements(By.tagName("area"));

        // нажимаем на рандомно выбранный лепесток
        Random rand = new Random();
        element = areas.get(rand.nextInt(areas.size()));
        element.click();

        try
        {
            WebElement resultMessage = (new WebDriverWait(initParams.page, completeWaitTimeoutSec))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("notification")));

            Logger.inst().Log(getGameName() + ": Result is " + resultMessage.getText());
        }
        catch (Exception ex)
        {
            // TODO only if debug
            Logger.inst().Log(getGameName() + ": exception:" + ex.getMessage());

            Logger.inst().Log(getGameName() + ": Cant wait notification message, continuing...");
        }
    }
}
