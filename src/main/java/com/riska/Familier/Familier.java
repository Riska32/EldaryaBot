package com.riska.Familier;

import com.riska.Utils.ThreadHelper;
import com.riska.logger.Logger;
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

        if (getEnergyCurrent() <= (minEnergyFamiliar + energyRemoveNight))
        {
            try
            {
                WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#pet-info-food span")));
                if (element.getText().equals("x0"))
                {
                    familierFeeder.recharger();

                    if (familierFeeder.maxFoodQuantity() > 0)
                    {
                        familierFeeder.giveFood();
                    } else
                    {
                        if (familierFeeder.buyFoodForFamiliar(familierFeeder.nameFood()))
                        {
                            goToFamiliar();
                            familierFeeder.recharger();
                            familierFeeder.giveFood();
                        }
                    }
                }
                else
                {
                    System.out.println("Already fed");
                }
            }
            catch (Exception e)
            {
                System.out.println("Feed : " + e.getMessage());
            }
        }
        else
        {
            System.out.println("Food is not required");
        }
    }

    public void hunt()
    {
        goToFamiliar();
        //ThreadHelper.Sleep(300);

        if ((getEnergyCurrent() < minEnergyLoc) || familierIsBusy())
        {
            System.out.println("Familiar can't hunt");
            return;
        }

        //ThreadHelper.Sleep(200);
        FamilierHunter familierHunter = new FamilierHunter(driver);
        List<WebElement> regions = familierHunter.getRegions();

        for (int i = regions.size() - 1; i >= 0; i--)
        {
            WebElement element = regions.get(i);
            element.click();
            ThreadHelper.Sleep(500);

            if (element.getAttribute("class").contains("selected"))
            {
                if (familierHunter.attack(getEnergyCurrent()))
                {
                    System.out.println("Hunt started");
                    break;
                }
            }
        }
        ThreadHelper.Sleep(500);
        WebElement element = driver.findElement(By.xpath("//li[@class=\"mapRegion tooltip selected\"]"));
        element.click();
        ThreadHelper.Sleep(300);
    }

    public void getReward()
    {
        goToFamiliar();
        try
        {
            ThreadHelper.Sleep(200);
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("treasurehunt-fold")));
            WebElement messageBarLevel = element.findElement(By.id("messageBarLevel"));

            if (messageBarLevel.isDisplayed() && (messageBarLevel.getAttribute("style") != null) && messageBarLevel.getAttribute("style").equals("width: 100%;"))
            {
                element.findElement(By.xpath("//*[@id=\"messageBar\"]/.//span")).click();
                //ThreadHelper.Sleep(200);

                element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("comeback")));
                element.click();
                System.out.println("Get reward done");

                Boolean result = (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("messageBar")));
            }
            else
            {
                System.out.println("No reward");
            }

        } catch (Exception e)
        {
            System.out.println("getReward: " + e.getMessage());
        }
    }

    public void goToFamiliar()
    {
        //driver.navigate().to("http://www.eldarya.com/#/pet/");
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("pet-headerPortrait-base")));
            element.click();
        }
        catch (Exception e)
        {
            System.out.println("goToFamiliar : " + e.getMessage());
        }
    }

    public int getEnergyCurrent()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#pet-info-energy span")));
            String text = element.getText();
            String regex = "\\((\\d+)/(\\d+)\\)";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            if (matcher.find())
            {
                String current = matcher.group(1);
                return Integer.parseInt(current);

            } else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            System.out.println("getEnergyCurrent : " + e.getMessage());
            return 0;
        }
    }

    public boolean familierIsBusy()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("treasurehunt-fold")));
            WebElement messageBarLevel = element.findElement(By.id("messageBarLevel"));
            if (messageBarLevel.isDisplayed())
            {
                return true;
            }
            else
                return false;
        }
        catch (Exception e)
        {
            System.out.println("familierIsBusy : " + e.getMessage());
            return false;
        }
    }
}
