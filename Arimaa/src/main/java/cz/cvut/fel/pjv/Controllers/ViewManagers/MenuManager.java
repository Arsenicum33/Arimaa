package cz.cvut.fel.pjv.Controllers.ViewManagers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.*;
import cz.cvut.fel.pjv.Views.Menu;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.StartGameInfo;
import cz.cvut.fel.pjv.utils.IImageHandler;

import javax.swing.*;

/**
 * MenuManager is responsible for all user interactions with menu. Creates other classes on delegates
 * responsibilities of game creation and record menu creation to other classes
 */
public class MenuManager implements ISubscriber
{
    IMessageBroker broker;
    IPublisher publisher;
    GameCreator gameCreator;
    RecordingsMenuManager recordingsMenuManager;
    Menu menu;
    IImageHandler imageHandler;
    public MenuManager(IMessageBroker broker, IPublisher publisher, IImageHandler imageHandler)
    {
        this.broker = broker;
        this.publisher = publisher;
        this.imageHandler=imageHandler;
    }
    public void LaunchArimaa()
    {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menu = new Menu(publisher);
                menu.setVisible(true);
            }
        });
        broker.addSubscriber(MessageType.CreateGame, this);
        broker.addSubscriber(MessageType.OpenRecords, this);
        broker.addSubscriber(MessageType.ReturnToMenu, this);
    }


    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type)
        {
            case CreateGame -> handleCreateGameMessage(data);
            case ReturnToMenu -> handleReturnToMenuMessage(data);
            case OpenRecords -> handleOpenRecordsMessage(data);
        }
    }

    private void handleCreateGameMessage(Object data)
    {
        StartGameInfo startGameInfo = (StartGameInfo) data;
        gameCreator = new GameCreator(broker, publisher, imageHandler, startGameInfo);
        menu.setVisible(false);
        broker.addSubscriber(MessageType.ReturnToMenu, this);
    }

    private void handleReturnToMenuMessage(Object data)
    {
        broker.removeAllSubscribers();
        broker.addSubscriber(MessageType.CreateGame, this);
        broker.addSubscriber(MessageType.OpenRecords, this);
        broker.addSubscriber(MessageType.ReturnToMenu, this);
        menu.setVisible(true);
    }


    private void handleOpenRecordsMessage(Object data)
    {
        menu.setVisible(false);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                recordingsMenuManager = new RecordingsMenuManager(broker,publisher, imageHandler);
            }
        });

        broker.addSubscriber(MessageType.ReturnToMenu, this);
    }
}
