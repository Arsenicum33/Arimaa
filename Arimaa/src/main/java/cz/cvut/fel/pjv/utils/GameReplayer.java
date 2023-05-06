package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.Controllers.MessageControllers.IPublisher;
import cz.cvut.fel.pjv.Models.*;

import java.io.*;
import java.util.*;

/**
 * Game replayer interacts directly with a file and read data from it to replay moves
 */
public class GameReplayer
{
    private final IImageHandler imageHandler;
    private final Map<Character, PieceType> notationToTypeCharTable = new HashMap<>(){{
        put('R',PieceType.RABBIT_G);
        put('r',PieceType.RABBIT_S);
        put('C',PieceType.CAT_G);
        put('c',PieceType.CAT_S);
        put('D',PieceType.DOG_G);
        put('d',PieceType.DOG_S);
        put('H',PieceType.HORSE_G);
        put('h',PieceType.HORSE_S);
        put('M',PieceType.CAMEL_G);
        put('m',PieceType.CAMEL_S);
        put('E',PieceType.ELEPHANT_G);
        put('e',PieceType.ELEPHANT_S);
    }};
    private final BufferedReader reader;
    public GameReplayer(String filename, IImageHandler imageHandler)
    {
        this.imageHandler = imageHandler;
        File file = new File(Constants.REPLAYS_PATH+filename);
        FileReader fr;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        reader = new BufferedReader(fr);


    }

    public BoardModel getStartingPosition(IPublisher publisher) throws IOException {
        BoardModel board = new BoardModel(publisher);
        List<Piece> pieces = new ArrayList<>();
        char[] buffer = new char[3];
        reader.skip(3); //skip "1g "
        for (int i=0;i<4*Constants.BOARD_SIZE;i++)
        {
            reader.read(buffer, 0, 3);
            reader.skip(1); //skip whitespaces
            PieceType type = notationToTypeCharTable.get(buffer[0]);
            int boardIndex = (buffer[1]-'a')+(Constants.BOARD_SIZE-(buffer[2]-'0'))*Constants.BOARD_SIZE;
            pieces.add(new Piece(type,boardIndex,publisher, imageHandler));
            if (i==2*Constants.BOARD_SIZE-1)
                reader.skip(3); //skip "1s "
        }
        board.setPieceSet(pieces);
        return board;
    }

    public ReplayMoveInfo getNextMove() throws IOException {
        //return new MoveInfo(null,null,null);
        int sizeOfMoveData = 4;
        final int maxReadValue =65535;
        char character;
        do {
            character=(char)reader.read();
            if (character==maxReadValue)
                return new ReplayMoveInfo(null,null,false);
        } while (character!=' ');

        char[] buffer = new char[sizeOfMoveData];
        reader.read(buffer,0,sizeOfMoveData);
        if (buffer[3]=='x')
            return new ReplayMoveInfo(null,null,true);
        Integer startFieldIndex = (buffer[1]-'a')+(Constants.BOARD_SIZE-(buffer[2]-'0'))*Constants.BOARD_SIZE;
        Integer endFieldIndex=null;
        switch (buffer[3])
        {
            case 'n'->endFieldIndex=startFieldIndex-Constants.BOARD_SIZE;
            case 'e'->endFieldIndex=startFieldIndex+1;
            case 's'->endFieldIndex=startFieldIndex+Constants.BOARD_SIZE;
            case 'w'->endFieldIndex=startFieldIndex-1;
        }
        return new ReplayMoveInfo(startFieldIndex, endFieldIndex, false);
    }

}
