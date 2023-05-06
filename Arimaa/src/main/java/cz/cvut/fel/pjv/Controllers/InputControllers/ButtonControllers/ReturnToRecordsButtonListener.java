package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;

import java.awt.event.ActionEvent;

public class ReturnToRecordsButtonListener extends AbstractButtonListener
{


    public ReturnToRecordsButtonListener(IPublisher publisher)
    {
        super(publisher);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        publish(MessageType.ReturnToRecords, null);
    }
}
