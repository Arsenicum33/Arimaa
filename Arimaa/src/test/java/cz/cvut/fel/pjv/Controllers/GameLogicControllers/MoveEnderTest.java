package cz.cvut.fel.pjv.Controllers.GameLogicControllers;

import cz.cvut.fel.pjv.Controllers.MessageControllers.*;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.BasePlayer;
import cz.cvut.fel.pjv.Controllers.PlayerControllers.Player;
import cz.cvut.fel.pjv.Models.*;
import cz.cvut.fel.pjv.utils.IImageHandler;
import cz.cvut.fel.pjv.utils.MockImageHandler;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class MoveEnderTest {

    @Test
    void rabbitsDoNotExist()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher moveEnderPublisher = new ConcretePublisher(broker);
        final String[] message = {""};
        ISubscriber sub = (type, data) -> message[0] = (String)data;
        broker.addSubscriber(MessageType.EndGame, sub);
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Player player1 = new Player(Color.Gold, moveHandler, null, null);
        BasePlayer player2 = new Player(Color.Silver, moveHandler, null, null);
        MoveEnder moveEnder = new MoveEnder(board, player1, player2, player1, moveHandler,moveEnderPublisher);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.ELEPHANT_G, 20,null,imageHandler);
        Piece p2 = new Piece(PieceType.CAT_G, 21,null,imageHandler);
        Piece p3 = new Piece(PieceType.RABBIT_G, 41,null,imageHandler);
        Piece p4 = new Piece(PieceType.RABBIT_S, 36,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        board.setPiece(p3);
        board.setPiece(p4);
        MoveInfo mi = new MoveInfo(board.getField(41),board.getField(42),null);
        player1.move(mi);
        moveEnder.receiveMessage(MessageType.EndMove, 3);
        assertTrue(message[0].contains("Silver"));
    }

    @Test
    void rabbitsExist()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher moveEnderPublisher = new ConcretePublisher(broker);
        final boolean[] endGame = new boolean[1];
        ISubscriber sub = (type, data) -> endGame[0] = true;
        broker.addSubscriber(MessageType.EndGame, sub);
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Player player1 = new Player(Color.Gold, moveHandler, null, null);
        BasePlayer player2 = new Player(Color.Silver, moveHandler, null, null);
        MoveEnder moveEnder = new MoveEnder(board, player1, player2, player1, moveHandler,moveEnderPublisher);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.RABBIT_G, 20,null,imageHandler);
        Piece p2 = new Piece(PieceType.CAT_G, 21,null,imageHandler);
        Piece p3 = new Piece(PieceType.RABBIT_S, 36,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        board.setPiece(p3);
        MoveInfo mi = new MoveInfo(board.getField(21),board.getField(29),null);
        player1.move(mi);
        moveEnder.receiveMessage(MessageType.EndMove, 3);
        assertFalse(endGame[0]);
    }

    @Test
    void noPossibleMoves()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher moveEnderPublisher = new ConcretePublisher(broker);
        final String[] message = {""};
        ISubscriber sub = (type, data) -> message[0] = (String)data;
        broker.addSubscriber(MessageType.EndGame, sub);
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Player player1 = new Player(Color.Gold, moveHandler, null, null);
        Player player2 = new Player(Color.Silver, moveHandler, null, null);
        MoveEnder moveEnder = new MoveEnder(board, player1, player2, player1, moveHandler,moveEnderPublisher);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.RABBIT_S, 27,null,imageHandler);
        Piece p2 = new Piece(PieceType.RABBIT_G, 25,null,imageHandler);
        Piece p3 = new Piece(PieceType.RABBIT_G, 35,null,imageHandler);
        Piece p4 = new Piece(PieceType.RABBIT_G, 28,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        board.setPiece(p3);
        board.setPiece(p4);
        MoveInfo mi = new MoveInfo(board.getField(25),board.getField(26),null);
        player1.move(mi);

        moveEnder.receiveMessage(MessageType.EndMove, 0);
        assertTrue(message[0].contains("Gold"));
    }

    @Test
    void rabbitOnTheLastRow()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher moveEnderPublisher = new ConcretePublisher(broker);
        final String[] message = {""};
        ISubscriber sub = (type, data) -> message[0] = (String)data;
        broker.addSubscriber(MessageType.EndGame, sub);
        BoardModel board = new BoardModel(null);
        MoveHandler moveHandler = new MoveHandler(board, null);
        Player player1 = new Player(Color.Gold, moveHandler, null, null);
        BasePlayer player2 = new Player(Color.Silver, moveHandler, null, null);
        MoveEnder moveEnder = new MoveEnder(board, player1, player2, player1, moveHandler,moveEnderPublisher);
        IImageHandler imageHandler = new MockImageHandler();
        Piece p1 = new Piece(PieceType.RABBIT_G, 9,null,imageHandler);
        Piece p2 = new Piece(PieceType.CAT_G, 21,null,imageHandler);
        Piece p3 = new Piece(PieceType.RABBIT_S, 36,null,imageHandler);
        board.setPiece(p1);
        board.setPiece(p2);
        board.setPiece(p3);
        MoveInfo mi = new MoveInfo(board.getField(9),board.getField(1),null);
        player1.move(mi);
        moveEnder.receiveMessage(MessageType.EndMove, 3);
        assertTrue(message[0].contains("Gold"));
    }
}