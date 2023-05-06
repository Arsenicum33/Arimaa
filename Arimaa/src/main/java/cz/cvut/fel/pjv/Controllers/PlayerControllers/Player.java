package cz.cvut.fel.pjv.Controllers.PlayerControllers;

import cz.cvut.fel.pjv.Controllers.GameLogicControllers.IMoveHandler;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.Color;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.MoveInfo;
import cz.cvut.fel.pjv.utils.ITimer;
/**
 * Player stores information related to player, like number of moves left and time left.
 * Manages timer and calls tryMovePiece of moveHandler. Publishes EndMove with number of moves left
 */
public class Player extends BasePlayer implements IPublisher
{
    IPublisher publisher;
    IMoveHandler moveHandler;
    ITimer timer;
    public Player(Color color, IMoveHandler moveHandler, IPublisher publisher, ITimer timer)
    {
        super(color);
        this.moveHandler=moveHandler;
        this.publisher = publisher;
        this.timer = timer;
    }

    public void move(MoveInfo moveInfo)
    {

        if (moveHandler.tryMovePiece(moveInfo, movesLeft, color))
        {
            int movesMade = moveInfo.forceMoveField() == null ? 1 : 2;
            movesLeft -= movesMade;
            publish(MessageType.EndMove, movesLeft);
        }

    }

    @Override
    public void startMove()
    {
        super.startMove();
        publish(MessageType.disableEndMoveButton, false);
        if (timer!=null)
            timer.start();
    }

    @Override
    public void endMove()
    {
        super.endMove();
        if (timer!=null)
            timer.stop();
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }
}
