package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.Controllers.MessageControllers.ISubscriber;
import cz.cvut.fel.pjv.Controllers.MessageControllers.MessageType;
import cz.cvut.fel.pjv.Models.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GameRecorder interacts directly with the file and writes there data representing start position and moves
 */
public class GameRecorder implements ISubscriber
{
    private final Map<PieceType, Character> typeToNotationCharTable = new HashMap<>(){{
        put(PieceType.RABBIT_G, 'R');
        put(PieceType.RABBIT_S, 'r');
        put(PieceType.CAT_G, 'C');
        put(PieceType.CAT_S, 'c');
        put(PieceType.DOG_G, 'D');
        put(PieceType.DOG_S, 'd');
        put(PieceType.HORSE_G, 'H');
        put(PieceType.HORSE_S, 'h');
        put(PieceType.CAMEL_G, 'M');
        put(PieceType.CAMEL_S, 'm');
        put(PieceType.ELEPHANT_G, 'E');
        put(PieceType.ELEPHANT_S, 'e');
    }};

    private StringBuilder currentString;
    private final BufferedWriter writer;
    private int moveNumber=2;
    private char colorNotationChar='g';

    public GameRecorder(BoardModel board)
    {
        long currentTime = System.currentTimeMillis();
        try
        {
            FileWriter fw = new FileWriter(Constants.REPLAYS_PATH+currentTime + ".txt");
            writer = new BufferedWriter(fw);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        recordInitialState(board);


    }

    private void recordInitialState(BoardModel board)
    {
        int numberOfPiecesOfColor=16;
        currentString=new StringBuilder().append("1g");
        var fields = board.getData();
        for (int i=Constants.BOARD_SQUARE-numberOfPiecesOfColor;i<Constants.BOARD_SQUARE;i++)
        {
            Piece piece = fields.get(i).getPiece();
            currentString.append(makeMoveRecord(new RecorderMoveInfo(piece.getType(),piece.getBoardIndex(),null)));
        }
        currentString.append("\n1s");
        for (int i=0;i<numberOfPiecesOfColor;i++)
        {
            Piece piece = fields.get(i).getPiece();
            currentString.append(makeMoveRecord(new RecorderMoveInfo(piece.getType(),piece.getBoardIndex(),null)));
        }
        try {
            writer.write(currentString.toString());
            writer.flush();
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentString = new StringBuilder().append(moveNumber).append(colorNotationChar);
    }
    @Override
    public void receiveMessage(MessageType type, Object data)
    {
        switch (type)
        {
            case RecordMove -> currentString.append(makeMoveRecord((RecorderMoveInfo) data));
            case EndMove -> {
                if (((int)data)==0)
                    endMove();
            }
            case EndGame -> endMove();
        }

    }

    private void endMove()
    {
        try {
            writer.write(currentString.toString());
            writer.flush();
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (colorNotationChar=='g')
        {
            colorNotationChar='s';
        }
        else
        {
            colorNotationChar='g';
            moveNumber++;
        }

        currentString = new StringBuilder().append(moveNumber).append(colorNotationChar);
    }

    private String makeMoveRecord(RecorderMoveInfo rmi)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(' ');
        sb.append(typeToNotationCharTable.get(rmi.type()));
        int startPosX = rmi.startBoardIndex()%Constants.BOARD_SIZE;
        int startPosY = rmi.startBoardIndex()/Constants.BOARD_SIZE;
        sb.append((char)('a'+startPosX));
        sb.append(Constants.BOARD_SIZE-startPosY);
        if (rmi.endBoardIndex()==null)
        {
            return sb.toString();
        }
        if (rmi.endBoardIndex()==-1)
        {
            sb.append("x");
            return sb.toString();
        }
        switch (rmi.endBoardIndex() - rmi.startBoardIndex()) {
            case 1 -> sb.append('e');
            case Constants.BOARD_SIZE -> sb.append('s');
            case -1 -> sb.append('w');
            case -Constants.BOARD_SIZE -> sb.append('n');
        }
        return sb.toString();


    }
}
