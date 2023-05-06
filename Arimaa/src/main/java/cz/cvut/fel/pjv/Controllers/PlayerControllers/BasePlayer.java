package cz.cvut.fel.pjv.Controllers.PlayerControllers;

import cz.cvut.fel.pjv.Models.Color;

public abstract class BasePlayer implements IPlayer
{
    final protected int MAX_MOVES = 4;
    protected int movesLeft;
    protected final Color color;
    public BasePlayer(Color color)
    {
        this.color = color;
        if (color==Color.Gold)
            movesLeft=MAX_MOVES;
    }

    @Override
    public int getMovesLeft()
    {
        return movesLeft;
    }

    @Override
    public Color getColor()
    {
        return color;
    }

    public int getMAX_MOVES()
    {return MAX_MOVES;}

    @Override
    public void startMove()
    {
        movesLeft=MAX_MOVES;
    }

    @Override
    public void endMove()
    {
        movesLeft=0;
    }
}
