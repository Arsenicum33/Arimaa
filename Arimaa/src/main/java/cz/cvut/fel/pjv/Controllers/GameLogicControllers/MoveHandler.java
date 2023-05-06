package cz.cvut.fel.pjv.Controllers.GameLogicControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import java.util.List;
/**
 * MoveHandler is responsible for the vast majority of in-game logic.
 * It verifies moves and applies changes, made by the move to the model.
 * Publishes information, when illegal move is made with a string description
 * */
public class MoveHandler implements IMoveHandler, IPublisher
{
    IPublisher publisher;
    BoardModel board;
    public MoveHandler(BoardModel board, IPublisher publisher)
    {
        this.board=board;
        this.publisher=publisher;
    }
    @Override
    public boolean tryMovePiece(MoveInfo moveInfo, int movesLeft, Color playerColor)
    {

        if (!isLegalMove(moveInfo, movesLeft, playerColor))
            return false;
        movePiece(moveInfo);
        return true;
    }

    @Override
    public boolean isLegalMove(MoveInfo moveInfo, int movesLeft, Color playerColor)
    {
        Piece piece = moveInfo.startField().getPiece();
        BoardField startField=moveInfo.startField();
        BoardField endField = moveInfo.endField();

        if (movesLeft <= 0)
        {
            handleIllegalMove(startField, "No moves left");
            return false;
        }
        if (piece.getColor() != playerColor) {
            handleIllegalMove(startField, "Other player's turn");
            return false;
        }
        if (moveInfo.endField().getIndex() < 0 || moveInfo.endField().getIndex() >= Constants.BOARD_SQUARE)
        {
            handleIllegalMove(startField, "Invalid field");
            return false;
        }

        if (checkFrozenState(startField))
        {
            handleIllegalMove(startField, "This piece is frozen");
            return false;
        }

        if (!checkPossibleMoves(startField,endField))
        {
            handleIllegalMove(startField, "Impossible move. Either end field is not adjacent to start field or rabbit is trying to move backwards");
            return false;
        }

        return checkForceMove(moveInfo, movesLeft);
    }

    boolean checkFrozenState(BoardField startField)
    {
        boolean isFrozen = false;
        Color pieceColor = startField.getPiece().getColor();
        List<BoardField> adjacentFields =  board.getAdjacentFields(startField);
        for (var adjField : adjacentFields)
        {
            if (!adjField.hasPiece())
                continue;
            if (adjField.getPiece().getColor().equals(pieceColor))
            {
                return false;
            }
            if (adjField.getPiece().getStrength()>startField.getPiece().getStrength())
                isFrozen = true;
        }
        return isFrozen;
    }

    boolean checkPossibleMoves(BoardField startField, BoardField endField)
    {
        List<BoardField> possibleEndFields = board.getAdjacentFields(startField);
        if (startField.getPiece().getType().equals(PieceType.RABBIT_G))
        {
            possibleEndFields.removeIf(field->field.getIndex()==startField.getIndex()+Constants.BOARD_SIZE);
        }
        if (startField.getPiece().getType().equals(PieceType.RABBIT_S))
        {
            possibleEndFields.removeIf(field->field.getIndex()==startField.getIndex()-Constants.BOARD_SIZE);
        }
        return possibleEndFields.contains(endField);
    }

    boolean checkForceMove(MoveInfo moveInfo, int movesLeft)
    {
        BoardField startField=moveInfo.startField();
        BoardField endField = moveInfo.endField();
        BoardField forceMoveField = moveInfo.forceMoveField();
        boolean forceMove = forceMoveField!=null;
        boolean pull = forceMove && forceMoveField.hasPiece();
        boolean push = forceMove && !forceMoveField.hasPiece();
        if (forceMove)
        {
            if (movesLeft == 1)
            {
                handleIllegalMove(startField, "Not enough moves to pull/push");
                return false;
            }
            if (push)
            {
                if (!endField.hasPiece())
                {
                    handleIllegalMove(startField, "You need a target to push");
                    return false;
                }

                if (!board.getAdjacentFields(endField).contains(forceMoveField))
                {
                    handleIllegalMove(startField, "You can push only to adjacent fields");
                    return false;
                }

                if (startField.getPiece().getColor()==endField.getPiece().getColor())
                {
                    handleIllegalMove(startField, "Can't push your own pieces");
                    return false;
                }

                if (startField.getPiece().getStrength() <= endField.getPiece().getStrength())
                {
                    handleIllegalMove(startField, "Piece is not strong enough to push");
                    return false;
                }

                if (forceMoveField.hasPiece())
                {
                    handleIllegalMove(startField, "Can't push to a field with a piece");
                    return false;
                }
            }
            if (pull)
            {
                if (startField.getPiece().getColor()==forceMoveField.getPiece().getColor())
                {
                    handleIllegalMove(startField, "Can't pull your own pieces");
                    return false;
                }
                if (startField.getPiece().getStrength() <= forceMoveField.getPiece().getStrength())
                {
                    handleIllegalMove(startField, "Piece is not strong enough to pull");
                    return false;
                }
                if (!board.getAdjacentFields(startField).contains(forceMoveField))
                {
                    handleIllegalMove(startField, "You need a target to pull");
                    return false;
                }
            }
        }
        if (!push && endField.hasPiece())
        {
            handleIllegalMove(startField, "End field is not empty, you need to push");
            return false;
        }
        return true;
    }

    private void handleIllegalMove(BoardField startField, String errorMessage)
    {
        if (publisher==null)
            return;
        Piece piece = startField.getPiece();
        piece.setBoardIndex(piece.getBoardIndex());
        publish(MessageType.GameStateNotification,"Illegal move! " + errorMessage + "\n");
    }

    private void movePiece(MoveInfo moveInfo)
    {
        BoardField startField=moveInfo.startField();
        BoardField endField = moveInfo.endField();
        BoardField forceMoveField = moveInfo.forceMoveField();
        boolean pull = forceMoveField!=null&&forceMoveField.hasPiece();
        boolean push = forceMoveField!=null&& !forceMoveField.hasPiece();
        if (push)
            movePiece(endField, forceMoveField);
        checkTraps();
        movePiece(startField, endField);

        if (pull)
            movePiece(forceMoveField, startField);

    }

    public void movePiece(BoardField startField, BoardField endField)
    {
        Piece piece = startField.getPiece();
        startField.setPiece(null);
        endField.setPiece(piece);
        piece.setBoardIndex(endField.getIndex());
        board.unchooseAllFields();
    }
    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }

    @Override
    public void checkTraps()
    {
        for (int trapIndex : Constants.TRAP_INDEXES)
        {
            BoardField trap = board.getField(trapIndex);
            if (trap.hasPiece())
            {
                boolean deletePiece = true;
                Color trappedPieceColor = trap.getPiece().getColor();
                List<BoardField> adjacentFields = board.getAdjacentFields(trap);
                for (var adjField : adjacentFields)
                {
                    if (adjField.hasPiece()&&adjField.getPiece().getColor()==trappedPieceColor)
                        deletePiece=false;
                }
                if (deletePiece)
                {
                    trap.getPiece().setBoardIndex(-1);
                    trap.setPiece(null);
                }
            }

        }
    }


}
