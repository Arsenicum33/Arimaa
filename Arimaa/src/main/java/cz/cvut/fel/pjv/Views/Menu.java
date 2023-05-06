package cz.cvut.fel.pjv.Views;

import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.ButtonActionListener;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.GameRecordsButtonListener;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.InputControllers.ButtonControllers.StartGameButtonListener;

import javax.swing.*;
import java.awt.*;

/**
 * menu window
 */
public class Menu
{
    final int preferredSizeX =250, preferredSizeY=300;
    private final JFrame menuFrame;
    private JPanel mainPanel;
    private JButton aiPlayButton, hotsitButton, gameRecordsButton;
    private JLabel arimaaTimerBase, arimaaTimerAddition;
    private JTextField arimaaTimerBaseText, arimaaTimerAdditionText;
    public Menu(IPublisher publisher)
    {
        menuFrame = new JFrame("Arimaa");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(new Dimension(preferredSizeX,preferredSizeY));
        createComponents(publisher);
        setAlignmentCenterAllComponents();
        addComponentsToMainPanel();
        menuFrame.setLocationRelativeTo(null);
    }

    private void setAlignmentCenterAllComponents()
    {
        arimaaTimerAdditionText.setHorizontalAlignment(JTextField.CENTER);
        aiPlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hotsitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameRecordsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        arimaaTimerBase.setAlignmentX(Component.CENTER_ALIGNMENT);
        arimaaTimerBaseText.setAlignmentX(Component.CENTER_ALIGNMENT);
        arimaaTimerAddition.setAlignmentX(Component.CENTER_ALIGNMENT);
        arimaaTimerAdditionText.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void createComponents(IPublisher publisher)
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        aiPlayButton = new JButton("Play with AI");
        hotsitButton = new JButton("Play together");
        gameRecordsButton = new JButton("Game records");
        arimaaTimerBase = new JLabel("Base time in sec");
        arimaaTimerBaseText = new JTextField();
        arimaaTimerBaseText.setHorizontalAlignment(JTextField.CENTER);
        arimaaTimerAddition = new JLabel("Addition in sec");
        arimaaTimerAdditionText = new JTextField();
        ButtonActionListener startAIGameListener = new StartGameButtonListener(publisher,arimaaTimerBaseText, arimaaTimerAdditionText, true);
        ButtonActionListener startHotsitGameListener = new StartGameButtonListener(publisher,arimaaTimerBaseText, arimaaTimerAdditionText, false);
        ButtonActionListener gameRecordsButtonListener = new GameRecordsButtonListener(publisher);
        aiPlayButton.addActionListener(startAIGameListener);
        hotsitButton.addActionListener(startHotsitGameListener);
        gameRecordsButton.addActionListener(gameRecordsButtonListener);
    }
    private void addComponentsToMainPanel()
    {
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(aiPlayButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(hotsitButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(arimaaTimerBase);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(arimaaTimerBaseText);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(arimaaTimerAddition);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(arimaaTimerAdditionText);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(gameRecordsButton);
        menuFrame.add(mainPanel);
    }
    public void setVisible(boolean value)
    {
        menuFrame.setVisible(value);
    }

}
