package cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;

public abstract class AbstractButtonListener implements ButtonActionListener
{
    IPublisher publisher;
    public AbstractButtonListener(IPublisher publisher)
    {
        this.publisher=publisher;
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }


}
