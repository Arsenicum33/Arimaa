package cz.cvut.fel.pjv.Views;

import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.ReturnToMenuButtonListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.ButtonActionListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.ReplayButtonListener;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;

/**
 * window with list of available replays
 */
public class GameRecordings
{
    private final int preferredSizeX=250;
    private final int preferredSizeY=400;
    private final JFrame recordingsFrame;
    private final JPanel mainPanel;

    private final IPublisher publisher;

    private final JButton returnToMenu;
    private final JScrollPane replaysScroller;
    private final JPanel replaysPanel;
    public GameRecordings(IPublisher publisher, List<Long> replayDatesInMillis)
    {
        this.publisher = publisher;
        recordingsFrame = new JFrame();
        recordingsFrame.setTitle("Arimaa");
        recordingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recordingsFrame.setSize(new Dimension(preferredSizeX,preferredSizeY));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        replaysPanel=new JPanel();
        replaysPanel.setLayout(new BoxLayout(replaysPanel, BoxLayout.PAGE_AXIS));
        replaysScroller = new JScrollPane(replaysPanel);
        replaysScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        createButtons(this.publisher, replayDatesInMillis);
        returnToMenu = new JButton("Return to menu");
        returnToMenu.addActionListener(new ReturnToMenuButtonListener(this.publisher, this));
        returnToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(replaysScroller);
        mainPanel.add(returnToMenu);
        recordingsFrame.add(mainPanel);
        recordingsFrame.setVisible(true);
        recordingsFrame.setLocationRelativeTo(null);
    }

    private void createButtons(IPublisher publisher, List<Long> replayDatesInMillis)
    {
        for (Long replayDate : replayDatesInMillis)
        {
            Date date = new Date(replayDate);
            JButton replayButton = new JButton(date.toString());
            ButtonActionListener actionListener = new ReplayButtonListener(replayDate.toString()+".txt", publisher);
            replayButton.addActionListener(actionListener);
            replayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            replaysPanel.add(replayButton);
        }

    }

    public void closeWindow()
    {
        recordingsFrame.dispose();
    }

    public void setFrameVisible(boolean value)
    {
        recordingsFrame.setVisible(value);
    }

}
