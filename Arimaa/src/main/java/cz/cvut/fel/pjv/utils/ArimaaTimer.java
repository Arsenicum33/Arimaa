package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.Color;
import cz.cvut.fel.pjv.Models.Time;
import cz.cvut.fel.pjv.Models.TimerInfo;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing chess-like clocks. Publishes when a player runs out of time
 */
public class ArimaaTimer implements IPublisher, ITimer
{
    private final Timer timer=new Timer();
    private final int baseTimeInMillis;
    private int timeElapsedInMillis=0;
    private final int additionInMillis;
    private final int firingPeriod = 100;
    private boolean active = false;
    private final Color color;
    private  TimerTask task;

    IPublisher publisher;
    public ArimaaTimer(int baseTimeInSec, int additionInSec, Color playerColor, IPublisher publisher)
    {
        if (baseTimeInSec==0)
            throw new IllegalArgumentException();
        this.baseTimeInMillis=baseTimeInSec*1000;
        this.additionInMillis=additionInSec*1000;
        this.publisher = publisher;
        this.color = playerColor;
    }

    @Override
    public void start()
    {
        if (active)
            return;
        active = true;
        timeElapsedInMillis-=additionInMillis;
        task = new TimerTask()
        {
            @Override
            public void run()
            {
                timeElapsedInMillis+=firingPeriod;
                int timeLeft = baseTimeInMillis-timeElapsedInMillis;
                if (timeLeft<=0)
                {
                    timeLeft=0;
                    Color winnerColor = color==Color.Gold?Color.Silver:Color.Gold;
                    publish(MessageType.EndGame, "Winner is " + winnerColor + " player");
                    timer.cancel();
                }
                Time t = new Time(timeLeft);
                TimerInfo ti = new TimerInfo(t,color);
                publish(MessageType.TimerFired, ti);
            }
        };
        timer.scheduleAtFixedRate(task, firingPeriod, firingPeriod);
    }

    @Override
    public void stop()
    {
        if (!active)
            return;
        active = false;
        task.cancel();
    }

    public void dispose()
    {
        timer.cancel();
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }

}
