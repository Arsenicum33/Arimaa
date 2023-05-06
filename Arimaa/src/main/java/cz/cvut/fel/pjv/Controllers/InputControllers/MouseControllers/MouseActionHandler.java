package cz.cvut.fel.pjv.Controllers.InputControllers.MouseControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.GameLogicControllers.MoveEnder;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.IPlayer;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.Player;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.MoveInfo;

/**
 * MouseActionHandler handles user's moves, noticed by BoardMouseListener
 */
public class MouseActionHandler implements ISubscriber
{
    BoardModel board;
    MoveEnder moveEnder;
    public MouseActionHandler(BoardModel board, MoveEnder moveEnder)
    {
        this.board=board;
        this.moveEnder=moveEnder;
    }
    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        if (type!=MessageType.PieceMoved)
            return;
         MoveInfo moveInfo = (MoveInfo) data;
         IPlayer currentPlayer = moveEnder.getCurrentPlayer();
         if (!(currentPlayer instanceof Player))
             return;
         ((Player)currentPlayer).move(moveInfo);

    }
}
