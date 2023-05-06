package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.StartGameInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StartGameButtonListener extends AbstractButtonListener
{
    private final JTextField baseTime;
    private final JTextField timeAddition;
    private final boolean againstAI;

    public StartGameButtonListener(IPublisher publisher, JTextField baseTime, JTextField timeAddition, boolean againstAI)
    {
        super(publisher);
        this.baseTime=baseTime;
        this.timeAddition=timeAddition;
        this.againstAI=againstAI;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int baseTimeInt, timeAdditionInt;
        try {
            String baseTimeStr = baseTime.getText();
            baseTimeInt = baseTimeStr.length()==0?0:Integer.parseInt(baseTimeStr);
            String timeAdditionStr = timeAddition.getText();
            timeAdditionInt = timeAdditionStr.length()==0?0:Integer.parseInt(timeAdditionStr);
        }
        catch (NumberFormatException exception)
        {
            return;
        }
        publish(MessageType.CreateGame, new StartGameInfo(againstAI, baseTimeInt, timeAdditionInt));
    }
}
