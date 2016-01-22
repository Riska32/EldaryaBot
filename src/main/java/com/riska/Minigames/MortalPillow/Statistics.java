package com.riska.Minigames.MortalPillow;

import com.sun.jna.platform.win32.WinDef;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Created by Anastasia on 09.01.2016.
 */

public class Statistics
{
    public class Step
    {
        public String Attack;
        public String Defense;

        public Step(String attack, String defense)
        {
            Attack = attack;
            Defense = defense;
        }
    }

    public String Name = "";
    public String AverageAttack = "";
    public String AverageDefense = "";
    public List<Step> steps = new Vector<Step>();

    private int[][] enemyStatistic = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    private int[][] allStatistic = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};


    public Statistics()
    {
    }

    public void Read() throws FileNotFoundException, IOException
    {
        RandomAccessFile randomAccessFile = new RandomAccessFile("MPStatistics.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        FileLock lock = channel.lock();
        try
        {
            //byte[] buffer = new byte[(int) file.length()];
            //randomAccessFile.readFully(buffer, 0, buffer.length);
            String line = "";

            while ((line = randomAccessFile.readLine()) != null)
            {

                String newName = "";
                for (int i = 0; (line.charAt(i) != ' ') && (line.charAt(i) != '\n'); i++)
                {
                    newName += line.charAt(i);
                }
                EnterStep(line, newName);
            }
        } finally
        {
            lock.release();
        }

        channel.close();

        if (steps.size() > 0)
        {
            if ((steps.size() > 1)
                    && (Objects.equals(steps.get(steps.size() - 1).Attack, steps.get(steps.size() - 2).Attack))
                    && (Objects.equals(steps.get(steps.size() - 1).Defense, steps.get(steps.size() - 2).Defense)))
            {
                AverageAttack = steps.get(steps.size() - 1).Attack;
                AverageDefense = "";
                for (int i = 0; i < 5; i++)
                {
                    for (int j = 1; j<4; j++)
                    {
                        if (steps.get(steps.size() - 1).Defense.charAt(i) != Character.forDigit(j, 10))
                        {
                            AverageDefense += Character.forDigit(j, 10);
                            break;
                        }
                    }
                }
            } else
            {
                EnterStatisticToAverage(enemyStatistic);
            }
        } else
        {
            EnterStatisticToAverage(allStatistic);
        }
        enemyStatistic = new int[][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        allStatistic = new int[][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    }

    public void Write(String line)
    {
        try(FileWriter writer = new FileWriter("MPStatistics.txt", true))
        {
            writer.write(Name + " " + line + "\n");
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private void EnterStep(String line, String newName)
    {
        Boolean bool = false;
        if (Name.equals(newName))
        {
            bool = true;
        }
        int nameLength = newName.length() + 1;

        if (bool)
        {
            Step step = new Step("", "");

            for (int i = 0; i < 5; i++)
            {
                step.Attack += line.charAt(i + nameLength);
                incrementStatistic(line.charAt(i + nameLength), i, bool);
            }
            for (int i = 5; i < 10; i++)
            {
                step.Defense += line.charAt(i + nameLength);
                incrementStatistic(line.charAt(i + nameLength), i, bool);
            }

            steps.add(step);
        } else
        {
            for (int i = 0; i < 10; i++)
            {
                incrementStatistic(line.charAt(i + nameLength), i, bool);
            }
        }
    }

    private void incrementStatistic(char c, int i, boolean bool)
    {
        switch (c)
        {
            case '1':
                if (bool) enemyStatistic[0][i]++;
                allStatistic[0][i]++;
                break;
            case '2':
                if (bool) enemyStatistic[1][i]++;
                allStatistic[1][i]++;
                break;
            case '3':
                if (bool) enemyStatistic[2][i]++;
                allStatistic[2][i]++;
                break;
            default:
                break;
        }
    }

    private void EnterStatisticToAverage(int[][] stat)
    {
        AverageAttack = "";
        AverageDefense = "";

        for (int i = 0; i < 5; i++)
        {
            if ((stat[0][i] >= stat[1][i]) && (stat[0][i] >= stat[2][i]))
            {

                AverageAttack += "1";
            }
            else if ((stat[1][i] >= stat[0][i]) && (stat[1][i] >= stat[2][i]))
            {
                AverageAttack += "2";
            }
            else
            {
                AverageAttack += "3";
            }
        }

        for (int i = 5; i < 10; i++)
        {
            if ((stat[0][i] <= stat[1][i]) && (stat[0][i] <= stat[2][i]))
            {
                AverageDefense += "1";
            }
            else if ((stat[1][i] <= stat[0][i]) && (stat[1][i] <= stat[2][i]))
            {
                AverageDefense += "2";
            }
            else
            {
                AverageDefense += "3";
            }
        }
    }
}
