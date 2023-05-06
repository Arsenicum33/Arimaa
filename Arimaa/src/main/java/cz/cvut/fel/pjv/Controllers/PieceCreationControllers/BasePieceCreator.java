package cz.cvut.fel.pjv.Controllers.PieceCreationControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Controllers.PieceCreationControllers.IPieceCreator;
import cz.cvut.fel.pjv.Models.BoardField;
import cz.cvut.fel.pjv.Models.BoardModel;
import cz.cvut.fel.pjv.Models.PieceType;
import cz.cvut.fel.pjv.utils.IImageHandler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Base piece creator stores logic for checking, whether piece sets that are currently on the board are correct.
 * To do it, BasePieceCreator keeps a collection of all piece types that should be on the board at the beginning
 * and then iterates over the board, removing all pieces from stored collection. Publishes when sets are incorrect
 */
public abstract class BasePieceCreator implements IPieceCreator, IPublisher
{
    BoardModel board;
    IPublisher publisher;
    IImageHandler imageHandler;
    public BasePieceCreator(BoardModel board, IPublisher publisher, IImageHandler imageHandler)
    {
        this.board = board;
        this.publisher = publisher;
        this.imageHandler = imageHandler;
    }
    protected final PieceType[] pieceTypesGold =
            {PieceType.RABBIT_G, PieceType.RABBIT_G, PieceType.RABBIT_G, PieceType.RABBIT_G,
                    PieceType.RABBIT_G, PieceType.RABBIT_G, PieceType.RABBIT_G, PieceType.RABBIT_G,
                    PieceType.CAT_G, PieceType.CAT_G, PieceType.DOG_G, PieceType.DOG_G,
                    PieceType.HORSE_G, PieceType.HORSE_G, PieceType.CAMEL_G, PieceType.ELEPHANT_G};
    protected final PieceType[] pieceTypesSilver =
            {PieceType.RABBIT_S, PieceType.RABBIT_S, PieceType.RABBIT_S, PieceType.RABBIT_S,
                    PieceType.RABBIT_S, PieceType.RABBIT_S, PieceType.RABBIT_S, PieceType.RABBIT_S,
                    PieceType.CAT_S, PieceType.CAT_S, PieceType.DOG_S, PieceType.DOG_S,
                    PieceType.HORSE_S, PieceType.HORSE_S, PieceType.CAMEL_S, PieceType.ELEPHANT_S};

    @Override
    public boolean checkStartSets()
    {
        int numberOfPieces = 32;
        ArrayList<PieceType> allPieces = new ArrayList<>(numberOfPieces);
        Collections.addAll(allPieces, pieceTypesSilver);
        Collections.addAll(allPieces, pieceTypesGold);
        for (BoardField field : board.getData())
        {
            if (!field.hasPiece())
                continue;

            PieceType type = field.getPiece().getType();
            if (!allPieces.contains(type))
            {
                publish(MessageType.GameStateNotification, "Wrong piece set. You must have 8 rabbits, 2 cats, 2 dogs, 2 horses, 1 camel and 1 elephant.\n");
                publish(MessageType.DisableStartButton, false);
                return false;
            }
            allPieces.remove(type);
        }
        publish(MessageType.DisableStartButton, true);
        return true;
    }

    @Override
    public void publish(MessageType type, Object data)
    {
        if (publisher!=null)
            publisher.publish(type,data);
    }

}
