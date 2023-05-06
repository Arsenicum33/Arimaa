package cz.cvut.fel.pjv.Controllers.ViewManagers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers.MouseListenerAll;
import cz.cvut.fel.pjv.Models.TimerInfo;
import cz.cvut.fel.pjv.Views.GameFrame;

/**
 * GameViewManager is responsible for handling messages and perform actions on the game window.
 */
public class GameViewManager implements ISubscriber
{
    private final GameFrame frame;
    private MouseListenerAll mouseListener;

    public GameViewManager(BoardModel boardModel, MouseListenerAll mouseListener, IPublisher publisher)
    {
        frame = new GameFrame(boardModel, publisher);
        this.mouseListener=mouseListener;
        frame.addMouseListener(mouseListener);
        frame.addMouseMotionListener(mouseListener);
    }

    private void repaint()
    {
        frame.repaint();
    }

    public void closeWindow()
    {
        frame.dispose();
    }


    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type) {
            case TimerFired -> {
                TimerInfo ti = (TimerInfo) data;
                frame.setTime(ti.color(), ti.time());
                repaint();
            }
            case PiecePosChanged, PieceDragged, FieldColorChanged, ChangePiece -> repaint();
            case GameStateNotification -> frame.postMessage(data.toString());
            case EndGame -> {
                frame.postMessage(data.toString());
                endGame();
            }
            case EndMove -> frame.setMovesLeft((Integer) data);
            case disableEndMoveButton -> frame.setEndMoveButtonEnabled(!(boolean)data);
            case DisableStartButton -> frame.setStartGameButtonEnabled(!(boolean)data);

        }
    }

    public void setMouseListener(MouseListenerAll newMouseListener)
    {
        frame.removeMouseListener(mouseListener);
        frame.removeMouseMotionListener(mouseListener);
        this.mouseListener = newMouseListener;
        frame.addMouseListener(mouseListener);
        frame.addMouseMotionListener(mouseListener);
    }

    public void endGame()
    {
        frame.removeMouseListener(mouseListener);
        frame.removeMouseMotionListener(mouseListener);
        frame.onEndGame();
    }
}
