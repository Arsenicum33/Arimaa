package cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.BoardField;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import java.awt.event.MouseEvent;

/**
 * PieceDeploymentMouseListener handles mouse events at the beginning of the game while deploying.
 * Publishes if a board field with a piece is clicked
 */
public class PieceDeploymentMouseListener implements MouseListenerAll, IPublisher
{
    BoardModel board;
    IPublisher publisher;

    public PieceDeploymentMouseListener(BoardModel boardModel, IPublisher publisher)
    {
        this.board = boardModel;
        this.publisher = publisher;
    }
    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        int mousePosX = e.getX();
        int mousePosY = e.getY();
        int fieldIndex = getFieldIndex(mousePosX, mousePosY);
        BoardField clickedField = board.getField(fieldIndex);
        if (clickedField.hasPiece())
            publish(MessageType.ChangePiece, clickedField);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

    private int getFieldIndex(int posX, int posY)
    {
        posX/= Constants.SQUARE_SIZE;
        posY/=Constants.SQUARE_SIZE;
        return posX+posY*Constants.BOARD_SIZE;
    }
}
