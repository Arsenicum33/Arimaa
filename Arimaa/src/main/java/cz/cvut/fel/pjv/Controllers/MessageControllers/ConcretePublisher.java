package cz.cvut.fel.pjv.Controllers.MessageControllers;

/**
 * ConcretePublisher encapsulates logic for sending messages, so other classes, which
 * want tp publish can utilize ConcretePublisher's implementation without knowing how it works,
 * providing loose-coupling
 */
public class ConcretePublisher implements IPublisher
{
    IMessageBroker messageBroker;
    public ConcretePublisher(IMessageBroker messageBroker)
    {
        this.messageBroker= messageBroker;
    }
    @Override
    public void publish(MessageType type, Object data)
    {
        if (messageBroker!=null)
            messageBroker.receiveMessage(type, data);
    }
}
