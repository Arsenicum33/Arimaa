package cz.cvut.fel.pjv.Controllers.MessageControllers;

public interface ISubscriber
{
    void receiveMessage(MessageType type, Object data);
}
