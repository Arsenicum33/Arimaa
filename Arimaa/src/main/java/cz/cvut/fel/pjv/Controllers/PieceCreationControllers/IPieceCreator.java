package cz.cvut.fel.pjv.Controllers.PieceCreationControllers;

import cz.cvut.fel.pjv.Models.Color;

/**
 * interface responsible for creating pieces at the of the game and checking whether current
 * piece sets are correct
 */
public interface IPieceCreator
{
    void createPieceSet(Color color);

    boolean checkStartSets();
}
