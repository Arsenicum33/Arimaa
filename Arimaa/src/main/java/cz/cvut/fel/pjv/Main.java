package cz.cvut.fel.pjv;


import cz.cvut.fel.pjv.Controllers.ViewManagers.MenuManager;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ConcretePublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IMessageBroker;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageBroker;
import cz.cvut.fel.pjv.utils.IImageHandler;
import cz.cvut.fel.pjv.utils.ImageHandler;


public class Main
{
    public static void main(String[] args)
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher publisher = new ConcretePublisher(broker);
        IImageHandler imageHandler = new ImageHandler();
        MenuManager gm = new MenuManager(broker, publisher, imageHandler);
        gm.LaunchArimaa();
    }
}