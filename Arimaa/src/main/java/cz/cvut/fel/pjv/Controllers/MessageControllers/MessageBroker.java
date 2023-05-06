package cz.cvut.fel.pjv.Controllers.MessageControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MessageBroker keeps a Map of message types and lists of subscribers for each message type
 * When MessageBroker receives a message, it redirects it to all subscribers, who are interested
 * in this particular message type
 */
public class MessageBroker implements IMessageBroker
{
    private Map<MessageType, List<ISubscriber>> subscribers = new HashMap<>();

    @Override
    public void addSubscriber(MessageType type, ISubscriber subscriber)
    {
        if (!subscribers.containsKey(type))
        {
            subscribers.put(type, new ArrayList<>());
        }
        subscribers.get(type).add(subscriber);
    }

    @Override
    public void removeSubscriber(MessageType type, ISubscriber subscriber)
    {
        if (!subscribers.containsKey(type))
            return;
        subscribers.get(type).remove(subscriber);
    }

    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        if (!subscribers.containsKey(type))
            return;
        List<ISubscriber> listeners = subscribers.get(type);
        for (var listener:listeners)
        {
            listener.receiveMessage(type, data);
        }
    }

    @Override
    public void removeAllSubscribers()
    {
        subscribers=new HashMap<>();
    }
}
