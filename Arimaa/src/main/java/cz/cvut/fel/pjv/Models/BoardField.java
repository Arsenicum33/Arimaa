package cz.cvut.fel.pjv.Models;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;


import java.awt.Color;

/**
 * represents a field on the board with properties like color, Piece on this field, etc.
 */
public class BoardField implements IPublisher
{
    private final IPublisher publisher;
    private final int index;
    private Piece piece;
    private final boolean isTrap;

    private java.awt.Color color;

    public BoardField(int index, boolean isTrap, java.awt.Color color, IPublisher publisher)
    {
        this.index=index;
        this.isTrap = isTrap;
        this.color = color;
        this.publisher = publisher;
    }

    public boolean hasPiece() {return piece!=null;}

    public Piece getPiece()
    {
        return piece;
    }

    public void setPiece(Piece piece)
    {
        this.piece = piece;
    }

    public void choose()
    {
        color = java.awt.Color.red;
        publish(MessageType.FieldColorChanged, null);
    }

    public void unchoose()
    {
        if (isTrap)
            color= Color.yellow;
        else
            color = (index+index/Constants.BOARD_SIZE)%2==0? Color.white : Color.black;
        publish(MessageType.FieldColorChanged, null);
    }


    public java.awt.Color getColor() { return color; }



    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }


    public int getIndex() { return index;}
}
