package cz.cvut.fel.pjv.Views;

import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.ReturnToMenuButtonListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.BeginGameButtonListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.EndMoveButtonListener;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.Time;
import cz.cvut.fel.pjv.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Window used during the game
 */
public class GameFrame extends JFrame
{
    private final int defaultSizeX= Constants.SQUARE_SIZE*(Constants.BOARD_SIZE+2), defaultSizeY = Constants.SQUARE_SIZE*(Constants.BOARD_SIZE+2);
    private BoardPanel boardPanel;
    private JTextArea notificationsBox;
    private JPanel buttonsPanel;
    private JPanel gameInfoPanel;
    private JButton endMoveButton, startGameButton,backToMenuButton;
    private JLabel timeGold, timeSilver, movesLeft;
    public GameFrame(BoardModel board, IPublisher publisher)
    {
        setTitle("Arimaa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(defaultSizeX,defaultSizeY);
        GridBagLayout mainLayout = new GridBagLayout();
        setLayout(mainLayout);

        createBoardPanel(board);

        createNotificationsPanel();

        createButtonsPanel(publisher);

        createGameInfoPanel();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createBoardPanel(BoardModel board)
    {
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardPanel = new BoardPanel(board);
        boardPanel.setPreferredSize(new Dimension(Constants.BOARD_SIZE*Constants.SQUARE_SIZE, Constants.BOARD_SIZE*Constants.SQUARE_SIZE));
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 0;
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 1;
        boardConstraints.fill = GridBagConstraints.BOTH;
        boardConstraints.weightx = 0.0;
        boardConstraints.weighty = 0.0;
        add(boardPanel,boardConstraints);
    }

    private void createNotificationsPanel()
    {
        GridBagConstraints notificationConstraints = new GridBagConstraints();
        notificationsBox = new JTextArea();
        notificationsBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        notificationsBox.setRows(5);
        notificationsBox.setColumns(20);
        notificationsBox.setEditable(false);
        JScrollPane notificationBoxScroller = new JScrollPane(notificationsBox);
        notificationBoxScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        notificationConstraints.gridx = 0;
        notificationConstraints.gridy = 1;
        notificationConstraints.gridheight = 1;
        notificationConstraints.gridwidth = 1;
        notificationConstraints.fill = GridBagConstraints.BOTH;
        notificationConstraints.weightx = 0.0;
        notificationConstraints.weighty = 1.0;
        add(notificationBoxScroller, notificationConstraints);
    }

    private void createButtonsPanel(IPublisher publisher)
    {
        buttonsPanel = new JPanel(new GridLayout(3,1));
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridheight = 1;
        buttonPanelConstraints.gridwidth = 1;
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.weightx = 1.0;
        buttonPanelConstraints.weighty = 0.0;
        add(buttonsPanel, buttonPanelConstraints);
        ActionListener endMoveButtonListener = new EndMoveButtonListener(publisher);
        endMoveButton = new JButton("End move");
        endMoveButton.addActionListener(endMoveButtonListener);
        startGameButton = new JButton("Start game");
        ActionListener startGameActionListener = new BeginGameButtonListener(publisher);
        startGameButton.addActionListener(startGameActionListener);
        backToMenuButton = new JButton("Return to menu");
        ActionListener backToMenuListener = new ReturnToMenuButtonListener(publisher, this);
        backToMenuButton.addActionListener(backToMenuListener);
        buttonsPanel.add(endMoveButton);
        buttonsPanel.add(startGameButton);
        buttonsPanel.add(backToMenuButton);
    }

    private void createGameInfoPanel()
    {
        gameInfoPanel = new JPanel(new GridLayout(3,1));
        GridBagConstraints timersPanelConstraints = new GridBagConstraints();
        timersPanelConstraints.gridx = 1;
        timersPanelConstraints.gridy = 1;
        timersPanelConstraints.gridheight = 1;
        timersPanelConstraints.gridwidth = 1;
        timersPanelConstraints.fill = GridBagConstraints.BOTH;
        timersPanelConstraints.weightx = 1.0;
        timersPanelConstraints.weighty = 0.0;
        add(gameInfoPanel, timersPanelConstraints);

        movesLeft = new JLabel("Moves left: 4");
        timeGold=new JLabel();
        timeSilver=new JLabel();
        gameInfoPanel.add(timeSilver);
        gameInfoPanel.add(timeGold);
        gameInfoPanel.add(movesLeft);
    }

    @Override
    public void repaint()
    {
        boardPanel.repaint();
    }

    @Override
    public void addMouseListener(MouseListener ml)
    {
        boardPanel.addMouseListener(ml);
    }

    @Override
    public void removeMouseListener(MouseListener ml)
    {
        boardPanel.removeMouseListener(ml);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener mml)
    {
        boardPanel.addMouseMotionListener(mml);
    }

    @Override
    public void removeMouseMotionListener(MouseMotionListener mml)
    {
        boardPanel.removeMouseMotionListener(mml);
    }

    public void postMessage(String message)
    {
        notificationsBox.append(message);
    }

    public void setTime(cz.cvut.fel.pjv.Models.Color color, Time time)
    {
        if (color== cz.cvut.fel.pjv.Models.Color.Gold)
            timeGold.setText("Golden player time: " + time.toString());
        else
            timeSilver.setText("Silver player time: " + time.toString());
    }

    public void setMovesLeft(int value)
    {
        if (value==0)
            value=4;
        movesLeft.setText("Moves left: " + value);
    }

    public void onEndGame()
    {
        endMoveButton.setEnabled(false);
    }

    public void setEndMoveButtonEnabled(boolean value)
    {
        endMoveButton.setEnabled(value);
    }
    public void setStartGameButtonEnabled(boolean value) { startGameButton.setEnabled(value);}
}
