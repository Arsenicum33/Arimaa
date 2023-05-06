package cz.cvut.fel.pjv.Controllers.MessageControllers;

/**
 * Interface of a class, which controls the message flow
 */
public interface IMessageBroker
{
    void addSubscriber(MessageType type, ISubscriber subscriber);

    void removeSubscriber(MessageType type, ISubscriber subscriber);

    void receiveMessage(MessageType type, Object data);

    void removeAllSubscribers();
}
