package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;

import java.awt.event.ActionEvent;

public class ReplayButtonListener extends AbstractButtonListener
{
    String filename;
    public ReplayButtonListener(String filename,IPublisher publisher)
    {
        super(publisher);
        this.filename = filename;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.PlayRecord, filename);
    }
}
