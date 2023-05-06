package cz.cvut.fel.pjv.Models;

import cz.cvut.fel.pjv.Controllers.MessageControllers.ConcretePublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IMessageBroker;
import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageBroker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardModelTest
{

    @Test
    @DisplayName("Get adjacent fields of a field not on the edge")
    void getAdjacentFieldsCenter()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher publisher = new ConcretePublisher(broker);
        BoardModel board = new BoardModel(publisher);
        BoardField testedField = board.getField(20);
        var adjacentFields = board.getAdjacentFields(testedField);
        assertAll(()->assertTrue(adjacentFields.contains(board.getField(21))),
                ()->assertTrue(adjacentFields.contains(board.getField(19))),
                ()->assertTrue(adjacentFields.contains(board.getField(12))),
                ()->assertTrue(adjacentFields.contains(board.getField(28))),
                ()->assertTrue(adjacentFields.size()==4));
    }

    @Test
    @DisplayName("Get adjacent fields of a field on the edge")
    void getAdjacentFieldsEdge()
    {
        IMessageBroker broker = new MessageBroker();
        IPublisher publisher = new ConcretePublisher(broker);
        BoardModel board = new BoardModel(publisher);
        BoardField testedField = board.getField(15);
        var adjacentFields = board.getAdjacentFields(testedField);
        assertAll(()->assertTrue(adjacentFields.contains(board.getField(14))),
                ()->assertTrue(adjacentFields.contains(board.getField(7))),
                ()->assertTrue(adjacentFields.contains(board.getField(23))),
                ()->assertEquals(adjacentFields.size(),3));
    }
}