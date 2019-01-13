package com.riska.Familier;

import com.riska.Utils.ThreadHelper;
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
        try
        {
            List<WebElement> elements = driver.findElements(By.xpath("//li[contains(@class,\"mapRegion tooltip\")]"));
            return elements;
        } catch (Exception e)
        {
            System.out.println("getRegions : " + e.getMessage());
            return null;
        }
    }

    public boolean attack(int energyCurrent)
    {
        try
        {
            //ThreadHelper.Sleep(400);
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
                if (element.isDisplayed())
                {
                    element.click();
                }
                return true;
            } else return false;
        } catch (Exception e)
        {
            System.out.println("attack : " + e.getMessage());
            return false;
        }
    }

    protected int energyLocation(WebElement location)
    {
        try
        {
            String idLocation = location.getAttribute("data-arrayid");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String energy = js.executeScript("return mapLocations[" + idLocation + "].energyRequired;").toString();
            return Integer.parseInt(energy);
        } catch (Exception ex)
        {
            Logger.inst().Log("energyLocation : " + ex.getMessage());
            return 1000;
        }
    }
}
