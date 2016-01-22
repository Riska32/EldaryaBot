package com.riska.Familier;

import com.riska.Utils.ThreadHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SteaveP on 22.01.2016.
 */
public class FamilierFeeder
{
    WebDriver driver;

    public FamilierFeeder(WebDriver page)
    {
        driver = page;
    }

    public boolean buyFoodForFamiliar(String namefood)
    {
        ThreadHelper.Sleep(400);
        //Заходим в бутик
        WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("main-menu-mall")));
        element.click();
        //Заходим в магазин фамильяров
        element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mall-pets .button_purple_big")));
        element.click();
        //Выбираем отдел еды
        element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mall-categories [data-categorylabel=food] a")));
        element.click();
        //Находим нужную еду
        namefood = org.apache.commons.lang3.StringEscapeUtils.escapeJava(namefood);
        Pattern p = Pattern.compile("(\\\\u.{4})", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(namefood);

        String namefood2 = namefood;
        while (m.find())
        {
            namefood2 = namefood2.replace(m.group(1), m.group(1).toLowerCase());
        }
        namefood2 = namefood2.replace("\\", "\\\\");
        element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mall-products-list [data-product*=\"\\\"name\\\":\\\"" + namefood2 + "\\\"\"]")));
        element.click();

        //Покупаем и возращаем результат покупки
        return buyFood();
    }

    protected boolean buyFood()
    {
        int maxUnit = 1, needUnit = 5;
        WebElement element = driver.findElement(By.cssSelector("#mall-productDetail-info-maana .button_blue_small"));
        element.click();
        //Считываем максимальное количество единиц, что мы можем купить
        element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("quantityRange")));
        try
        {
            maxUnit = Integer.parseInt(element.getAttribute("max"));
        }
        catch (NumberFormatException e)
        {
            System.out.println("maxFoodQuantity: " + e.getMessage());
        }

        //Покупаем по потребностям ...
        if (needUnit <= maxUnit)
        {
            acheter(needUnit);
            return true;
        }
        //или по возможностям
        else if ((maxUnit < needUnit) && (maxUnit > 0))
        {
            acheter(maxUnit);
            return true;
        }
        //Вариант для нищебродов
        else
        {
            return false;
        }
    }

    protected void acheter(int num)
    {
        //Двигаем ползунок на num
        WebElement element = driver.findElement(By.id("quantityRange"));
        for (int i=1; i<num; i++)
        {
            element.sendKeys(Keys.ARROW_RIGHT);
        }
        element = driver.findElement(By.id("buyform-submit-btn"));
        element.click();
    }

    public void giveFood()
    {
        //Двигаем ползунок на 1 единицу
        WebElement element = driver.findElement(By.id("foodQuantity"));
        element.sendKeys(Keys.ARROW_RIGHT);
        element = driver.findElement(By.cssSelector("#foodForm .button_blue_big"));
        element.click();
    }

    public void recharger()
    {
        WebElement element = driver.findElement(By.id("pet-info-food-stock"));
        element.click();
    }

    public int maxFoodQuantity()
    {
        WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("foodQuantity")));
        try
        {
            return Integer.parseInt(element.getAttribute("max"));
        } catch (NumberFormatException e)
        {
            System.out.println("maxFoodQuantity: " + e.getMessage());
            return 0;
        }
    }

    public String nameFood()
    {
        WebElement element = driver.findElement(By.cssSelector("#pet-food-stock-popup .genericPopupClose"));
        element.click();
        element = driver.findElement(By.id("pet-info-food"));
        String name = element.getText();

        String regex = "^(.*)\n";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        if (matcher.find())
        {
            name = matcher.group(1);
        }
        return name;
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
