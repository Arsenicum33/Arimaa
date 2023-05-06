package cz.cvut.fel.pjv.Views;

import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.*;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.utils.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * replay window
 */
public class ReplayFrame extends JFrame
{
    private JPanel boardPanel;

    public ReplayFrame(BoardModel board, IPublisher publisher)
    {
        setTitle("Arimaa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int defaultSizeX = Constants.SQUARE_SIZE * (Constants.BOARD_SIZE + 2);
        int defaultSizeY = Constants.SQUARE_SIZE * Constants.BOARD_SIZE + 35;
        setSize(new Dimension(defaultSizeX, defaultSizeY));
        setLayout(new GridBagLayout());
        createBoard(board);
        createButtonsPanel(publisher);

        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void createBoard(BoardModel board)
    {
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardPanel = new BoardPanel(board);
        boardPanel.setMaximumSize(new Dimension(Constants.BOARD_SIZE*Constants.SQUARE_SIZE, Constants.BOARD_SIZE*Constants.SQUARE_SIZE));
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 0;
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 1;
        boardConstraints.fill = GridBagConstraints.BOTH;
        boardConstraints.weightx = 1.0;
        boardConstraints.weighty = 1.0;
        add(boardPanel,boardConstraints);
    }

    private void createButtonsPanel(IPublisher publisher)
    {
        JPanel buttonsPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS);
        buttonsPanel.setLayout(layout);
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.gridheight = 1;
        buttonPanelConstraints.gridwidth = 1;
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.weightx = 0.0;
        buttonPanelConstraints.weighty = 0.0;

        JButton nextMoveButton = new JButton("Next move");
        nextMoveButton.addActionListener(new NextMoveButtonListener(publisher));
        buttonsPanel.add(nextMoveButton);
        JButton backToMenu = new JButton("Return to menu");
        backToMenu.addActionListener(new ReturnToRecordsButtonListener(publisher));
        buttonsPanel.add(backToMenu);
        add(buttonsPanel, buttonPanelConstraints);
    }

    @Override
    public void repaint()
    {
        boardPanel.repaint();
    }

}
