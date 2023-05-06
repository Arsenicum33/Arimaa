package cz.cvut.fel.pjv.Controllers.ViewManagers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IMessageBroker;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Views.GameRecordings;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.utils.IImageHandler;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecordingsMenuManager implements ISubscriber
{
    private IMessageBroker broker;
    private IPublisher publisher;
    private GameRecordings gameRecordings;
    private ReplayManager replayManager;
    private IImageHandler imageHandler;
    public RecordingsMenuManager(IMessageBroker broker, IPublisher publisher, IImageHandler imageHandler)
    {
        this.broker = broker;
        this.publisher = publisher;
        this.imageHandler = imageHandler;
        createReplayMenu();
    }

    private void createReplayMenu()
    {
        List<Long> replayDatesInMillis = getReplayDates();
        gameRecordings = new GameRecordings(publisher, replayDatesInMillis);
        broker.addSubscriber(MessageType.PlayRecord, this);
        broker.addSubscriber(MessageType.ReturnToMenu, this);
        broker.addSubscriber(MessageType.ReturnToRecords, this);
    }


    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type)
        {
            case ReturnToMenu -> gameRecordings.closeWindow();
            case ReturnToRecords -> gameRecordings.setFrameVisible(true);
            case PlayRecord -> {
                gameRecordings.setFrameVisible(false);
                String filename = (String)data;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        replayManager = new ReplayManager(publisher,imageHandler);
                        replayManager.playRecord(filename);
                        broker.addSubscriber(MessageType.NextMoveOfRecord, replayManager);
                        broker.addSubscriber(MessageType.ReturnToRecords, replayManager);
                    }
                });
            }
        }


    }

    private List<Long> getReplayDates()
    {
        File folder = new File(Constants.REPLAYS_PATH);
        File[] replays = folder.listFiles();
        if (replays==null)
            return null;
        List<Long> replayDatesInMillis = new ArrayList<>();
        for (var replay : replays)
        {
            String filename = replay.getName();
            String filenameWithoutExtension = filename.split("\\.")[0];
            long time=0;
            try
            {
                time = Long.parseLong(filenameWithoutExtension);
            }
            catch (NumberFormatException e)
            {
                continue;
            }
            replayDatesInMillis.add(time);
        }
        return replayDatesInMillis;
    }
}
