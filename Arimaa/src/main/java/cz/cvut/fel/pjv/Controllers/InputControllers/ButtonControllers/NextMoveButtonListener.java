package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;

import java.awt.event.ActionEvent;

public class NextMoveButtonListener extends AbstractButtonListener
{
    public NextMoveButtonListener(IPublisher publisher)
    {
        super(publisher);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.NextMoveOfRecord, null);
    }
}
