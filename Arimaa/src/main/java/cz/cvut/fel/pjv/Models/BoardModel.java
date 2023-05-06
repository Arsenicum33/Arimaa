package cz.cvut.fel.pjv.Models;

import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import java.awt.Color;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * represents the board as an array [64] BoardFields
 */
public class BoardModel
{
    private final List<BoardField> data = new ArrayList<>(Constants.BOARD_SQUARE);

    public BoardModel(IPublisher publisher)
    {
        data.addAll(createBoard(publisher));
    }
    private List<BoardField> createBoard(IPublisher publisher)
    {
        List<BoardField> fields = new ArrayList<>();
        for (int y=0;y<Constants.BOARD_SIZE;y++)
        {
            for (int x=0;x<Constants.BOARD_SIZE;x++)
            {
                int index = y*Constants.BOARD_SIZE+x;
                boolean isTrap=false;
                java.awt.Color fieldColor;
                if (Constants.TRAP_INDEXES.contains(index))
                    isTrap=true;
                if (isTrap)
                    fieldColor= Color.yellow;
                else
                    fieldColor = (index+index/Constants.BOARD_SIZE)%2==0? Color.white : Color.black;

                fields.add(new BoardField(index,isTrap, fieldColor,publisher));
            }

        }
        return fields;
    }
    public List<BoardField> getData() { return data; }
    public BoardField getField(int index)
    {
        if (index<0 || index>=Constants.BOARD_SQUARE)
            throw new InvalidParameterException();
        return data.get(index);
    }

    public void setPieceSet(List<Piece> pieceSet)
    {
        for (var piece : pieceSet)
        {
            int index = piece.getBoardIndex();
            data.get(index).setPiece(piece);
        }
    }

    public void setPiece(Piece p)
    {
        int index = p.getBoardIndex();
        data.get(index).setPiece(p);
    }

    public void unchooseAllFields()
    {
        for(var field: data)
        {
            field.unchoose();
        }
    }

    public List<BoardField> getAdjacentFields(BoardField field)
    {
        List<BoardField> adjacentFields = new ArrayList<>();
        int fieldIndex = field.getIndex();
        int fieldXPos = fieldIndex%Constants.BOARD_SIZE;
        int fieldYPos = fieldIndex/Constants.BOARD_SIZE;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int i=0;i<4;i++)
        {
            int adjX = fieldXPos + dx[i];
            int adjY = fieldYPos + dy[i];
            if (adjX>=0 && adjY>=0 && adjX<Constants.BOARD_SIZE && adjY<Constants.BOARD_SIZE)
                adjacentFields.add(getFieldFromCoordinates(adjX,adjY));
        }
        return adjacentFields;
    }
    private BoardField getFieldFromCoordinates(int x, int y) { return data.get(x+y*Constants.BOARD_SIZE);}
}
