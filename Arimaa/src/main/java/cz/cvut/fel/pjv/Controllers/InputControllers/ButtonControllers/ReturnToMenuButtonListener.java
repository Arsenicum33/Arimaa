package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import java.awt.event.ActionEvent;

public class ReturnToMenuButtonListener extends AbstractButtonListener
{
    Object owner;
    public ReturnToMenuButtonListener(IPublisher publisher, Object owner)
    {
        super(publisher);
        this.owner = owner;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.ReturnToMenu, owner);
    }
}
