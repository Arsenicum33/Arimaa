package cz.cvut.fel.pjv.Controllers.GameLogicControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.BasePlayer;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.IPlayer;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;

import java.util.List;
/**
 * Move ender does actions, which must occur at the end of the move.
 * It checks win condition, traps and changes current player when needed.
 * Receives EndMove with number of moves left and publishes when someone wins.
 * */
public class MoveEnder implements ISubscriber, IPublisher
{
    private final BoardModel board;
    private final BasePlayer player1;
    private final BasePlayer player2;
    private IPlayer currentPlayer;
    private final IMoveHandler moveHandler;
    private final IPublisher publisher;
    public IPlayer getCurrentPlayer() { return currentPlayer; }
    public MoveEnder(BoardModel board, BasePlayer player1, BasePlayer player2,
                     IPlayer currentPlayer, IMoveHandler moveHandler, IPublisher publisher)
    {
        this.board = board;
        this.player1=player1;
        this.player2=player2;
        this.currentPlayer=currentPlayer;
        this.moveHandler=moveHandler;
        this.publisher=publisher;
    }

    private void onMoveEnd(Boolean forcedEnd)
    {
        if (currentPlayer.getMovesLeft()==4)
        {
            publish(MessageType.GameStateNotification, "You must make at least 1 move!\n");
            return;
        }
        moveHandler.checkTraps();
        if ((forcedEnd||currentPlayer.getMovesLeft()==0))
        {
            currentPlayer.endMove();
            currentPlayer = currentPlayer==player1 ? player2 : player1;
            currentPlayer.startMove();
        }
        Color winnerColor = checkWinConditions((BasePlayer)currentPlayer, moveHandler);
        if (winnerColor!=null)
        {
            publish(MessageType.EndGame, "Winner is " + winnerColor + " player\n");
        }


    }

    private Color checkWinConditions(BasePlayer nextPlayer, IMoveHandler moveHandler) //Returns color of a player, who won, or null otherwise
    {
        Color winnersColor;
        winnersColor = checkRabbitsExistence();
        if (winnersColor!=null)
            return winnersColor;
        winnersColor = checkRabbitsOnLastRow();
        if (winnersColor!=null)
            return winnersColor;
        if (!checkPossibleMoves(nextPlayer,moveHandler))
        {
            winnersColor = nextPlayer.getColor()==Color.Gold ? Color.Silver : Color.Gold;
            return winnersColor;
        }
        return null;
    }

    private Color checkRabbitsExistence()
    {
        boolean goldenRabbitsExist = false;
        boolean silverRabbitsExist = false;
        for (var field:board.getData())
        {
            if (field.hasPiece()&&field.getPiece().getType().equals(PieceType.RABBIT_S))
                silverRabbitsExist=true;
            if (field.hasPiece()&&field.getPiece().getType().equals(PieceType.RABBIT_G))
                goldenRabbitsExist=true;
        }
        if (!goldenRabbitsExist)
            return Color.Silver;
        if (!silverRabbitsExist)
            return Color.Gold;
        return null;
    }
    private Color checkRabbitsOnLastRow()
    {
        for (int i=0; i< Constants.BOARD_SIZE;i++)
        {
            Piece p = board.getField(i).getPiece();
            if (p!=null&&p.getType().equals(PieceType.RABBIT_G))
            {
                return Color.Gold;
            }
        }
        for (int i=Constants.BOARD_SQUARE-1;i>= Constants.BOARD_SQUARE-Constants.BOARD_SIZE;i--)
        {
            Piece p = board.getField(i).getPiece();
            if (p!=null&&p.getType().equals(PieceType.RABBIT_S))
            {
                return Color.Silver;
            }
        }
        return null;
    }

    private boolean checkPossibleMoves(BasePlayer nextPlayer, IMoveHandler moveHandler)
    {
        if (nextPlayer.getMovesLeft()!=4)
            return true;
        Color nextPlayerColor = nextPlayer.getColor();
        for (var field : board.getData())
        {
            if (field.hasPiece() && field.getPiece().getColor()==nextPlayerColor)
            {
                List<BoardField> adjacentFields = board.getAdjacentFields(field);
                for (var endField : adjacentFields)
                {
                    MoveInfo potentialSimpleMove = new MoveInfo(field, endField, null);
                    if (moveHandler.isLegalMove(potentialSimpleMove, nextPlayer.getMAX_MOVES(), nextPlayerColor))
                        return true;
                    List<BoardField> endFieldAdjacentFields = board.getAdjacentFields(endField);
                    for (var forceMoveField : endFieldAdjacentFields)
                    {
                        MoveInfo potentialPushMove = new MoveInfo(field, endField, forceMoveField);
                        if (moveHandler.isLegalMove(potentialPushMove, nextPlayer.getMAX_MOVES(), nextPlayerColor))
                            return true;
                    }
                }
            }

        }
        return false;
    }




    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        if(type!=MessageType.EndMove)
            return;
        boolean forcedMoveEnd=false;
        if (data instanceof Integer)
            forcedMoveEnd=((Integer)data==0);
        onMoveEnd(forcedMoveEnd);
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }
}
