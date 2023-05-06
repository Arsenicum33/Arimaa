package cz.cvut.fel.pjv.Controllers.MessageControllers;

public interface IPublisher
{
    void publish(MessageType type, Object data);
}
