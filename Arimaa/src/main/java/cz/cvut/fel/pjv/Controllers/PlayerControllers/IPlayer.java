package cz.cvut.fel.pjv.Controllers.PlayerControllers;

import cz.cvut.fel.pjv.Models.Color;

public interface IPlayer
{
    Color getColor();
    int getMovesLeft();

    void startMove();

    void endMove();
}
