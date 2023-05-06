package cz.cvut.fel.pjv.Controllers.GameLogicControllers;

import cz.cvut.fel.pjv.Models.Color;
import cz.cvut.fel.pjv.Models.MoveInfo;

public interface IMoveHandler
{
    boolean tryMovePiece(MoveInfo moveInfo, int movesLeft, Color playerColor);

    boolean isLegalMove(MoveInfo moveInfo,int movesLeft, Color playerColor);
    void checkTraps();
}
