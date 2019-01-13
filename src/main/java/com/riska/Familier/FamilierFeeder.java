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

public class FamilierFeeder
{
    WebDriver driver;

    public FamilierFeeder(WebDriver page)
    {
        driver = page;
    }

    public boolean buyFoodForFamiliar(String namefood)
    {
        try
        {
            //Заходим в бутик
            ThreadHelper.Sleep(500);
            //driver.navigate().to("http://www.eldarya.com/#/mall");
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("main-menu-mall")));
            element.click();
            //Заходим в магазин фамильяров
            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#mall-pets .button_purple_big")));
            element.click();
            //Выбираем отдел еды
            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#mall-categories [data-categorylabel=food] a")));
            element.click();
            //Находим нужную еду
            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(@class,\"mall-product\") and contains(.,\"" + namefood + "\")]")));
            element.click();

            //Покупаем и возращаем результат покупки
            return buyFood();
        }
        catch (Exception e)
        {
            System.out.println("buyFoodForFamiliar : " + e.getMessage());
            return false;
        }
    }

    protected boolean buyFood()
    {
        try
        {
            int maxUnit = 1, needUnit = 15;
            WebElement element = driver.findElement(By.cssSelector("#mall-productDetail-info-maana .button_blue_small"));
            element.click();
            //Считываем максимальное количество единиц, что мы можем купить
            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("quantityRange")));
            try
            {
                maxUnit = Integer.parseInt(element.getAttribute("max"));
            } catch (NumberFormatException e)
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
        } catch (Exception e)
        {
            System.out.println("buyFood : " + e.getMessage());
            return false;
        }
    }

    protected void acheter(int num)
    {
        try
        {
            //Двигаем ползунок на num
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("quantityRange")));
            for (int i = 1; i < num; i++)
            {
                element.sendKeys(Keys.ARROW_RIGHT);
            }
            element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("buyform-submit-btn")));
            element.click();
            System.out.println("Food bought");
            ThreadHelper.Sleep(4000);
            element = driver.findElement(By.id("pet-headerPortrait-base"));
            element.click();
        } catch (Exception e)
        {
            System.out.println("acheter : " + e.getMessage());
        }
    }

    public void giveFood()
    {
        try
        {
            //Двигаем ползунок на 1 единицу
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("foodQuantity")));
            element.sendKeys(Keys.ARROW_RIGHT);
            element = driver.findElement(By.cssSelector("#foodForm .button_blue_big"));
            element.click();
            System.out.println("Food given");
        } catch (Exception e)
        {
            System.out.println("giveFood : " + e.getMessage());
        }
    }

    public void recharger()
    {
        try
        {
            //ThreadHelper.Sleep(300);
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("pet-info-food-stock")));
            element.click();
        } catch (Exception e)
        {
            System.out.println("recharger : " + e.getMessage());
        }
    }

    public int maxFoodQuantity()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.id("foodQuantity")));
            return Integer.parseInt(element.getAttribute("max"));
        } catch (NumberFormatException e)
        {
            System.out.println("maxFoodQuantity: " + e.getMessage());
            return 0;
        }
    }

    public String nameFood()
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#pet-food-stock-popup .genericPopupClose")));
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
        } catch (Exception e)
        {
            System.out.println("nameFood : " + e.getMessage());
            return "";
        }
    }


}
