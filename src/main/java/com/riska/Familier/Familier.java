package com.riska.Familier;

import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Collator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SteaveP on 21.01.2016.
 */
public class Familier
{
    WebDriver driver;

    public Familier(WebDriver page)
    {
        driver = page;
    }

    public void feed()
    {
        goToFamiliar();
        FamilierFeeder familierFeeder = new FamilierFeeder(driver);

        int needEnergyForFeed = 45;
        if (familierFeeder.getEnergyCurrent() <= needEnergyForFeed)
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
        FamilierHunter familierHunter = new FamilierHunter(driver);

    }

    public void getReward()
    {
        goToFamiliar();
        try
        {
            WebElement element = driver.findElement(By.cssSelector("#messageBar span"));
            element.click();
            ThreadHelper.Sleep(2000);

            element = driver.findElement(By.id("comeback"));
            element.click();
        } catch (Exception e)
        {
            System.out.println("getReward: " + e.getMessage());
        }
    }

    public void goToFamiliar()
    {
        WebElement element = driver.findElement(By.id("pet-headerPortrait-base"));
        element.click();
    }
}
