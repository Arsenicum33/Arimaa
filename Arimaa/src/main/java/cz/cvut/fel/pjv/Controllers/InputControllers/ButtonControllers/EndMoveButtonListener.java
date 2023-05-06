package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import java.awt.event.ActionEvent;

public class EndMoveButtonListener extends AbstractButtonListener
{
    public EndMoveButtonListener(IPublisher publisher)
    {
        super(publisher);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.EndMove, 0);
    }
}
