package cz.cvut.fel.pjv.Views;

import cz.cvut.fel.pjv.Models.BoardField;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.Piece;
import cz.cvut.fel.pjv.utils.Constants;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * BoardPanel represents the board during game or replays.
 */
public class BoardPanel extends JPanel
{
    BoardModel boardModel;
    public BoardPanel(BoardModel boardModel)
    {
        super();
        this.boardModel = boardModel;
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        paintBoard(g);
        paintPieces(g, boardModel.getData());
    }

    private void paintBoard(Graphics g)
    {
        for (int y = 0; y< Constants.BOARD_SIZE; y++)
        {
            for (int x=0;x<Constants.BOARD_SIZE;x++)
            {
                int index = y*Constants.BOARD_SIZE+x;
                BoardField field = boardModel.getField(index);
                g.setColor(field.getColor());
                g.fillRect(x*Constants.SQUARE_SIZE,y*Constants.SQUARE_SIZE,Constants.SQUARE_SIZE,Constants.SQUARE_SIZE);
            }
        }
    }

    private void paintPieces(Graphics g, List<BoardField> fields)
    {
        for (BoardField field : fields)
        {
            Piece piece = field.getPiece();
            if (piece!=null)
            {
                g.drawImage(piece.getImg(),field.getPiece().getWindowPosX(), field.getPiece().getWindowPosY(),new JFrame());
            }
        }
    }
}
