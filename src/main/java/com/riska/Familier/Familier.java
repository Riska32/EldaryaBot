package com.riska.Familier;

import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SteaveP on 21.01.2016.
 */
public class Familier
{
    WebDriver driver;
    public static final int minEnergyFamiliar = 30;
    public static final int energyRemoveNight = 20;
    public static final int minEnergyLoc = 10;

    public Familier(WebDriver page)
    {
        driver = page;
    }

    public void feed()
    {
        goToFamiliar();
        FamilierFeeder familierFeeder = new FamilierFeeder(driver);

        if (getEnergyCurrent() <= minEnergyFamiliar + energyRemoveNight)
        {
            WebElement element = driver.findElement(By.cssSelector("#pet-info-food span"));
            if (element.getText().equals("x0"))
            {
                familierFeeder.recharger();

                if (familierFeeder.maxFoodQuantity() > 0)
                {
                    familierFeeder.giveFood();
                }
                else
                {
                    if (familierFeeder.buyFoodForFamiliar(familierFeeder.nameFood()))
                    {
                        goToFamiliar();
                        familierFeeder.recharger();
                        familierFeeder.giveFood();
                    }
                }
            }
        }
    }

    public void hunt()
    {
        goToFamiliar();

        if (getEnergyCurrent() < minEnergyLoc)
        {
            return;
        }

        ThreadHelper.Sleep(200);
        FamilierHunter familierHunter = new FamilierHunter(driver);
        List<WebElement> regions = familierHunter.getRegions();

        for (int i = regions.size() - 1; i >= 0; i--)
        {
            WebElement element = regions.get(i);
            element.click();
            ThreadHelper.Sleep(1000);

            if (element.getAttribute("class").contains("selected"))
            {
                if (familierHunter.attack(getEnergyCurrent()))
                {
                    element = regions.get(i).findElement(By.xpath(".//.[contains(@class,\"regionName\")]"));
                    element.click();
                    break;
                }
            }
        }
    }

    public void getReward()
    {
        goToFamiliar();
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#messageBar span")));
            element.click();
            ThreadHelper.Sleep(5000);

            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("comeback")));
            element.click();

            Boolean result = (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("messageBar")));

        } catch (Exception e)
        {
            System.out.println("getReward: " + e.getMessage());
        }
    }

    public void goToFamiliar()
    {
        WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("pet-headerPortrait-base")));
        element.click();
    }

    public int getEnergyCurrent()
    {
        WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#pet-info-energy span")));
        String text = element.getText();
        String regex = "\\((\\d+)/(\\d+)\\)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
        {
            String current = matcher.group(1);
            try
            {
                return Integer.parseInt(current);
            }
            catch (Exception e)
            {
                System.out.println("feed: " + e.getMessage());
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }
}
