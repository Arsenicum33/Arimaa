package cz.cvut.fel.pjv.Controllers.PieceCreationControllers;

import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.utils.Constants;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.utils.IImageHandler;

import java.util.ArrayList;
import java.util.Random;

public class RandomPieceCreator extends BasePieceCreator
{
    public RandomPieceCreator(BoardModel board, IPublisher publisher, IImageHandler imageHandler)
    {
        super(board,publisher, imageHandler);
    }
    @Override
    public void createPieceSet(Color color)
    {
        int numberOfPieces = 2*Constants.BOARD_SIZE;
        int index;
        int indexOffset = color==Color.Gold ? Constants.BOARD_SQUARE-numberOfPieces : 0;
        ArrayList<Piece> createdPieces = new ArrayList<>();
        Random random = new Random();

        PieceType[] pieceTypes = color==Color.Gold? pieceTypesGold : pieceTypesSilver;
        ArrayList<Integer> randomIndexes = new ArrayList<>(numberOfPieces);
        for (var pieceType : pieceTypes)
        {
            do {
                index = random.nextInt(0,numberOfPieces);
            }
            while (randomIndexes.contains(index));
            randomIndexes.add(index);
            createdPieces.add(new Piece(pieceType, index+indexOffset, publisher, imageHandler));
        }
        board.setPieceSet(createdPieces);
    }



}
