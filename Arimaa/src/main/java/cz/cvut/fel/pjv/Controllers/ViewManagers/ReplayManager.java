package cz.cvut.fel.pjv.Controllers.ViewManagers;

import cz.cvut.fel.pjv.Controllers.GameLogicControllers.MoveHandler;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.Views.ReplayFrame;
import cz.cvut.fel.pjv.utils.GameReplayer;
import cz.cvut.fel.pjv.utils.IImageHandler;

import java.io.IOException;

/**
 * Manages window when replaying. Handles events like Next move button click and Return button clicked
 */
public class ReplayManager implements ISubscriber
{

    private GameReplayer replayer;
    private ReplayFrame frame;
    private BoardModel board;
    private final IPublisher publisher;
    private MoveHandler moveHandler;
    private IImageHandler imageHandler;
    public ReplayManager(IPublisher publisher, IImageHandler imageHandler)
    {
        this.publisher = publisher;
        this.imageHandler=imageHandler;
    }

    public void playRecord(String filename)
    {
        replayer = new GameReplayer(filename, imageHandler);
        try {
            board = replayer.getStartingPosition(publisher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        frame = new ReplayFrame(board, publisher);
        moveHandler = new MoveHandler(board, publisher);
    }

    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type)
        {
            case NextMoveOfRecord -> {
                try {
                    ReplayMoveInfo moveInfo = replayer.getNextMove();
                    if (moveInfo.checkTraps())
                        moveHandler.checkTraps();
                    else
                    {
                        Integer startFieldIndex= moveInfo.startFieldIndex();
                        Integer endFieldsIndex= moveInfo.endFieldIndex();
                        if (startFieldIndex==null || endFieldsIndex==null)
                            return;
                        BoardField startField = board.getField(startFieldIndex);
                        BoardField endField = board.getField(endFieldsIndex);
                        moveHandler.movePiece(startField, endField);
                    }
                    frame.repaint();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            }
            case ReturnToRecords -> frame.dispose();
        }
    }
}
