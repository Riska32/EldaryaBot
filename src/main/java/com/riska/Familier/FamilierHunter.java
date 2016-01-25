package com.riska.Familier;

import com.riska.logger.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SteaveP on 22.01.2016.
 */
public class FamilierHunter
{
    WebDriver driver;
    protected Random random = new Random();

    public FamilierHunter(WebDriver page)
    {
        driver = page;
    }

    public List<WebElement> getRegions()
    {
        List<WebElement> elements = driver.findElements(By.xpath("//li[contains(@class,\"mapRegion tooltip\")]"));
        return elements;
    }

    public boolean attack(int energyCurrent)
    {
        List<WebElement> locations = driver.findElements(By.xpath("//div[contains(@class,\"mapLocation tooltip\")]"));

        int locSize = locations.size();

        for (int i = locSize - 1; i >= 0; i--)
        {
            int energyLoc = energyLocation(locations.get(i));
            if ((energyLoc > energyCurrent) || ((energyCurrent <= Familier.minEnergyFamiliar) && (energyLoc > Familier.minEnergyFamiliar - Familier.minEnergyLoc)))
            {
                locations.remove(i);
            }
        }

        if (locations.size() != 0)
        {
            locations.get(random.nextInt(locations.size())).click();
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("explore")));
            element.click();
            return true;
        }
        else
            return false;
    }

    protected int energyLocation(WebElement location)
    {
        String idLocation = location.getAttribute("data-arrayid");

        JavascriptExecutor js = (JavascriptExecutor)driver;
        try
        {
            String energy = js.executeScript("return mapLocations[" + idLocation + "].energyRequired;").toString();
            return Integer.parseInt(energy);
        }
        catch (Exception ex)
        {
            Logger.inst().Log("energyLocation : " + ex.getMessage());
            return 1000;
        }
    }
}
