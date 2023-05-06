package cz.cvut.fel.pjv.Controllers.GameLogicControllers;

import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.utils.IImageHandler;
import cz.cvut.fel.pjv.utils.ImageHandler;
import cz.cvut.fel.pjv.utils.MockImageHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IMoveHandlerTest {

    /*private List<Piece> createPieces()
    {
        List<Piece> pieceSet = new ArrayList<>();
        pieceSet.add(new Piece())
    }*/
    @Test
    void tryMovePiece()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Piece p = new Piece(PieceType.ELEPHANT_G, 20,null, new MockImageHandler());
        board.setPiece(p);
        BoardField startField = board.getField(20);
        BoardField endField = board.getField(28);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        moveHandler.tryMovePiece(moveInfo, 4, Color.Gold);
        assertAll(
                ()->assertFalse(board.getField(20).hasPiece()),
                ()->assertEquals(board.getField(28).getPiece(),p)
        );
    }

    @Test
    void isLegalMoveNormal()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Piece p = new Piece(PieceType.ELEPHANT_G, 20,null, new MockImageHandler());
        board.setPiece(p);
        BoardField startField = board.getField(20);
        BoardField endField = board.getField(28);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertTrue(moveHandler.isLegalMove(moveInfo,4,Color.Gold));
    }

    @Test
    void isLegalMoveFrozen()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.ELEPHANT_G, 28,null,imageHandler);
        Piece p2 = new Piece(PieceType.DOG_S, 29,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        BoardField startField = board.getField(29);
        BoardField endField = board.getField(30);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Silver));
    }

    @Test
    void isLegalMoveRabbitBackwards()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Piece p1 = new Piece(PieceType.RABBIT_G, 28,null, new MockImageHandler());
        board.setPiece(p1);
        BoardField startField = board.getField(28);
        BoardField endField = board.getField(36);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Gold));
    }

    @Test
    void isLegalMoveOtherPlayer()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Piece p1 = new Piece(PieceType.HORSE_G, 28,null, new MockImageHandler());
        board.setPiece(p1);
        BoardField startField = board.getField(28);
        BoardField endField = board.getField(36);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Silver));
    }

    @Test
    void isLegalMoveDestinationHasPiece()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.CAT_S, 28,null,imageHandler);
        Piece p2 = new Piece(PieceType.CAT_G, 27,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        BoardField startField = board.getField(28);
        BoardField endField = board.getField(27);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Silver));
    }

    @Test
    void isLegalMoveNotStrongEnough()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.CAT_S, 28,null, imageHandler);
        Piece p2 = new Piece(PieceType.CAT_G, 27,null, imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        BoardField startField = board.getField(28);
        BoardField endField = board.getField(27);
        BoardField forceMoveField = board.getField(26);
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Silver));
    }

    @Test
    void isLegalMoveNotAdjacentEndField()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Piece p1 = new Piece(PieceType.CAT_S, 28,null, new MockImageHandler());
        board.setPiece(p1);
        BoardField startField = board.getField(28);
        BoardField endField = board.getField(35);
        BoardField forceMoveField = null;
        MoveInfo moveInfo = new MoveInfo(startField,endField,forceMoveField);
        assertFalse(moveHandler.isLegalMove(moveInfo,4,Color.Silver));
    }



    @Test
    void checkTraps()
    {
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        List<Piece> pieceSet = new ArrayList<>();
        IImageHandler imageHandler = new MockImageHandler();
        pieceSet.add(new Piece(PieceType.HORSE_G, 18,null, imageHandler));
        pieceSet.add(new Piece(PieceType.RABBIT_S, 19,null, imageHandler));
        pieceSet.add(new Piece(PieceType.RABBIT_G, 20,null, imageHandler));
        pieceSet.add(new Piece(PieceType.RABBIT_S, 21,null, imageHandler));
        board.setPieceSet(pieceSet);
        moveHandler.checkTraps();
        assertAll(
                ()->assertFalse(board.getField(18).hasPiece()),
                ()->assertTrue(board.getField(19).hasPiece()),
                ()->assertTrue(board.getField(20).hasPiece()),
                ()->assertFalse(board.getField(18).hasPiece())
        );
    }
}