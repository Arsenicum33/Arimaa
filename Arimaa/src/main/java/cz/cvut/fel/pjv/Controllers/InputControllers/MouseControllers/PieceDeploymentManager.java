package cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.BoardField;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.Piece;
import cz.cvut.fel.pjv.Models.PieceType;

import java.security.InvalidParameterException;

/**
 * PieceDeploymentManager is responsible for deploying pieces on the board at the beginning of the game
 * When a piece is clicked, it receives this message and changes the piece for the next in a row
 */
public class PieceDeploymentManager implements ISubscriber
{
    BoardModel board;
    public PieceDeploymentManager(BoardModel board)
    {
        this.board=board;
    }

    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        if (type!=MessageType.ChangePiece)
            return;
        if (!(data instanceof BoardField fieldToChange))
            throw new InvalidParameterException();
        if (!fieldToChange.hasPiece())
            return;
        Piece currentPiece = fieldToChange.getPiece();
        currentPiece.setPieceType(PieceType.getStrongerAnimal(currentPiece.getType()));


    }
}
