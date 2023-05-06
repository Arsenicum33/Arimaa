package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.event.ActionEvent;

public class BeginGameButtonListener extends AbstractButtonListener
{
    public BeginGameButtonListener(IPublisher publisher)
    {
        super(publisher);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.BeginGame, null);
    }
}
