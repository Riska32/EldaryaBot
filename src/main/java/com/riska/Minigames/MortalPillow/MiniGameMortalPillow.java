package com.riska.Minigames.MortalPillow;

import com.riska.Exceptions.MinigameException;
import com.riska.Minigames.IMinigame;
import com.riska.Minigames.InitParams;
import com.riska.Utils.ThreadHelper;
import com.riska.logger.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

/**
 * Created by Anastasia on 09.01.2016.
 */
public class MiniGameMortalPillow implements IMinigame
{
    public enum Arrow
    {
        Top,
        Middle,
        Down,
    }

    Statistics enemyStatistics = new Statistics();

    protected InitParams initParams;
    protected Random random = new Random();

    public void Init(InitParams _initParams)
    {
        initParams = _initParams;

        // TODO
    }

    public void Start() throws MinigameException
    {
        enterGame();

        while(true)
        {
            try
            {
                WebElement acceptButton = null;
                try
                {
                    acceptButton = initParams.page.findElement(By.className("accept_challenge"));
                } catch (Exception e)
                {
                }

                if (acceptButton != null)
                {
                    acceptChallenge(acceptButton);
                    //Распознаём врага и заносим данные в статистику
                    gartherStatistics();
                    initParams.page.findElement(By.className("auto-button-green")).click();
                    ThreadHelper.Sleep(500);
                }

                acceptButton = initParams.page.findElement(By.className("auto-button-pink"));
                acceptChallenge(acceptButton);
                ThreadHelper.Sleep(28000 + random.nextInt(3000));
            } catch (Exception e)
            {
                initParams.page.navigate().refresh();
            }
        }
        //exitToLobby();
    }

    protected void enterGame()
    {
        initParams.startButton.click();
    }

    protected void exitToLobby()
    {
        initParams.page.navigate().back();

        // TODO check if is not in lobby, go to it
    }

    protected void acceptChallenge(WebElement startButton)
    {
        startButton.click();
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException ex)
        {
        }
        String enemyName = "";
        boolean haveEnemy = false;

        try
        {
            enemyName = initParams.page.findElement(By.cssSelector("div#bet-area h3")).getText().toLowerCase();
            haveEnemy = true;
        } catch (Exception ex)
        {
            haveEnemy = false;
        }

        if (!haveEnemy)
        {
            List<Arrow> attack = getArrows();
            List<Arrow> defense = getArrows();
            applyCombination(attack, defense);
        } else
        {
            enemyStatistics = new Statistics();
            enemyStatistics.Name = enemyName;
            try
            {
                enemyStatistics.Read();
            } catch (Exception e)
            {

            }
            List<Arrow> attack = getArrows("attack");
            List<Arrow> defense = getArrows("defense");
            applyCombination(attack, defense);
        }
        initParams.page.findElement(By.id("run-battle")).click();
    }

    protected void applyCombination(List<Arrow> attack, List<Arrow> defense)
    {
        try
        {
            Map<Arrow, WebElement> attackArrowButtons = new HashMap<Arrow, WebElement>();
            attackArrowButtons.put(Arrow.Top, initParams.page.findElement(By.cssSelector(".attack .arrow-top")));
            attackArrowButtons.put(Arrow.Middle, initParams.page.findElement(By.cssSelector(".attack .arrow-middle")));
            attackArrowButtons.put(Arrow.Down, initParams.page.findElement(By.cssSelector(".attack .arrow-down")));

            for (int i = 0; i < attack.size(); i++)
            {
                attackArrowButtons.get(attack.get(i)).click();
                ThreadHelper.Sleep(100);
            }

            Map<Arrow, WebElement> defenseArrowButtons = new HashMap<Arrow, WebElement>();
            defenseArrowButtons.put(Arrow.Top, initParams.page.findElement(By.cssSelector(".defense .arrow-top")));
            defenseArrowButtons.put(Arrow.Middle, initParams.page.findElement(By.cssSelector(".defense .arrow-middle")));
            defenseArrowButtons.put(Arrow.Down, initParams.page.findElement(By.cssSelector(".defense .arrow-down")));

            for (int i = 0; i < defense.size(); i++)
            {
                defenseArrowButtons.get(defense.get(i)).click();
                ThreadHelper.Sleep(100);
            }

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    protected void gartherStatistics()
    {
        try
        {
            String enemyAttack = (new WebDriverWait(initParams.page, 2))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("p1ayerattack"))).getAttribute("value");
            String enemyDefense = (new WebDriverWait(initParams.page, 1))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("p1ayerdefenseinit"))).getAttribute("value");
            enemyStatistics.Name = (new WebDriverWait(initParams.page, 1))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".player_1 .left span"))).getText().toLowerCase();

            boolean sizeGt2 = enemyStatistics.steps.size() > 1;
            boolean prevActionsEquals = sizeGt2
                && enemyStatistics.steps.get(enemyStatistics.steps.size() - 1).Attack.equals(enemyAttack)
                && enemyStatistics.steps.get(enemyStatistics.steps.size() - 1).Defense.equals(enemyDefense);

            boolean prevPrevActionsEquals = sizeGt2
                    && (enemyStatistics.steps.get(enemyStatistics.steps.size() - 1).Attack.equals(enemyStatistics.steps.get(enemyStatistics.steps.size() - 2).Attack))
                    && (enemyStatistics.steps.get(enemyStatistics.steps.size() - 1).Defense.equals(enemyStatistics.steps.get(enemyStatistics.steps.size() - 2).Defense));
            if (prevActionsEquals && prevPrevActionsEquals)
            {
                //Do nothig
            } else
            {
                enemyStatistics.Write(enemyAttack + enemyDefense);
            }
        } catch (Exception ex)
        {
            Logger.inst().Log("gartherStatistics exception: " + ex.getMessage());
        }
    }

    protected List<Arrow> getArrows()
    {
        List<Arrow> randomArrows = new Vector<Arrow>();

        for (int i = 0; i < 5; i++)
        {
            int r = random.nextInt(3);
            randomArrows.add(Arrow.values()[r]);
        }

        return randomArrows;
    }

    protected List<Arrow> getArrows(String action)
    {
        List<Arrow> arrows = new Vector<Arrow>();

        if (action.equals("attack"))
        {
            for (int i = 0; i < 5; i++)
            {
                arrows.add(Arrow.values()[Character.getNumericValue(enemyStatistics.AverageDefense.charAt(i)) - 1]);
            }

        } else
        {
            for (int i = 0; i < 5; i++)
            {
                arrows.add(Arrow.values()[Character.getNumericValue(enemyStatistics.AverageAttack.charAt(i)) - 1]);
            }
        }

        return arrows;
    }
}
