package cz.cvut.fel.pjv.Controllers.PlayerControllers;

import cz.cvut.fel.pjv.Controllers.GameLogicControllers.IMoveHandler;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.BoardField;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.Color;

import java.util.*;
import java.util.Timer;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.MoveInfo;

/**
 * AIPlayer with random moves. It gets all legal moves, including the ones, which require pulling and pushing.
 * Then it chooses one at random. AIPlayer uses a Timer to make a pause between moves. During the pause it disables
 * EndMove button.Also publishes at the end of each move
 */
public class AIPlayer extends BasePlayer implements IPublisher
{

    private TimerTask task;
    private final Timer timer;
    private final int timerFiringRate=1000;
    private final BoardModel board;
    private final IMoveHandler moveHandler;
    private final IPublisher publisher;
    public AIPlayer(Color color, BoardModel board, IMoveHandler moveHandler, IPublisher publisher, Timer timer)
    {
        super(color);
        this.board = board;
        this.moveHandler = moveHandler;
        this.publisher=publisher;
        this.timer = timer;
    }
    public void move()
    {

        task = new TimerTask() {
            @Override
            public void run()
            {
                makeMove();
            }
        };
        timer.scheduleAtFixedRate(task, 0, timerFiringRate);
    }

    private void makeMove()
    {
        List<MoveInfo> possibleMoves = new ArrayList<>();
        for (var field : board.getData())
        {

            if (field.hasPiece()&&field.getPiece().getColor().equals(this.color))
            {
                List<BoardField> startAdjacentFields = board.getAdjacentFields(field);
                for (var startAdjField : startAdjacentFields)
                {
                    MoveInfo simpleMove = new MoveInfo(field, startAdjField, null);
                    if (moveHandler.isLegalMove(simpleMove, movesLeft, color))
                        possibleMoves.add(simpleMove);
                    for (var forceMoveField : startAdjacentFields)
                    {
                        MoveInfo pullMove = new MoveInfo(field, startAdjField, forceMoveField);
                        if (moveHandler.isLegalMove(pullMove, movesLeft, color)&& !possibleMoves.contains(pullMove))
                            possibleMoves.add(pullMove);
                    }
                    for (var forceMoveField : board.getAdjacentFields(startAdjField))
                    {
                        MoveInfo pushMove = new MoveInfo(field, startAdjField, forceMoveField);
                        if (moveHandler.isLegalMove(pushMove, movesLeft, color) && !possibleMoves.contains(pushMove))
                            possibleMoves.add(pushMove);
                    }
                }
            }
        }
        int size = possibleMoves.size();
        if (size==0)
        {
            movesLeft=0;
            task.cancel();
            publish(MessageType.EndMove, movesLeft);
            return;
        }
        Random random = new Random();
        MoveInfo chosenMove = possibleMoves.get(random.nextInt(size));
        moveHandler.tryMovePiece(chosenMove,movesLeft,color);
        int movesMade = chosenMove.forceMoveField() == null ? 1 : 2;
        movesLeft -= movesMade;
        if (movesLeft==0)
            task.cancel();
        publish(MessageType.EndMove, movesLeft);
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }

    @Override
    public void startMove()
    {
        super.startMove();
        publish(MessageType.disableEndMoveButton, true);
        move();
    }
}
