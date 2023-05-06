package cz.cvut.fel.pjv.Models;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.utils.*;

import java.awt.*;
import java.util.Objects;

/**
 * represents a piece with its properties (position, type, image, etc.)
 */
public class Piece implements IPublisher
{
    private final IPublisher publisher;
    private PieceType type;

    private Image img;

    private int boardIndex;

    private int windowPosX, windowPosY;

    private IImageHandler imageHandler;

    public Piece(PieceType type, int boardIndex, IPublisher publisher, IImageHandler imageHandler)
    {
        this.type = type;
        this.publisher=publisher;
        this.imageHandler = imageHandler;
        img = imageHandler.getImage(type);
        setBoardIndex(boardIndex);

    }
    public void setPieceType(PieceType pieceType)
    {
        type=pieceType;
        img = imageHandler.getImage(type);
    }

    public void publish(MessageType type, Object data)
    {
        if (publisher != null)
            publisher.publish(type,data);
    }


    public Piece(Piece other)
    {
        this.type = other.type;
        this.img = other.img;
        this.boardIndex = other.boardIndex;
        this.windowPosX = other.windowPosX;
        this.windowPosY=other.windowPosY;
        this.publisher=other.publisher;
    }
    public PieceType getType()
    {
        return type;
    }
    public Color getColor()
    {
        return type.getColor();
    }

    public int getStrength()
    {
        return type.getStrength();
    }


    public Image getImg()
    {
        return img;
    }

    public int getWindowPosX()
    {
        return windowPosX;
    }

    public int getWindowPosY()
    {
        return windowPosY;
    }

    public void setWindowPos(int windowPosX, int windowPosY)
    {
        this.windowPosX = windowPosX;
        this.windowPosY=windowPosY;
        publish(MessageType.PieceDragged,null);
    }

    public int getBoardIndex()
    {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex)
    {
        RecorderMoveInfo rmi = new RecorderMoveInfo(getType(),this.boardIndex, boardIndex);
        this.boardIndex = boardIndex;
        windowPosX = boardIndex%Constants.BOARD_SIZE * Constants.SQUARE_SIZE;
        windowPosY = boardIndex/Constants.BOARD_SIZE * Constants.SQUARE_SIZE;
        publish(MessageType.PiecePosChanged, null);
        if (!Objects.equals(rmi.startBoardIndex(), rmi.endBoardIndex()))
            publish(MessageType.RecordMove, rmi);
    }

}
