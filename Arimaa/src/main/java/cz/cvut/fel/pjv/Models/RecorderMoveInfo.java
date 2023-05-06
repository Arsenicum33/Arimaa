package cz.cvut.fel.pjv.Models;

/**
 * Data which recorder needs to record a move
 */
public record RecorderMoveInfo(PieceType type, Integer startBoardIndex, Integer endBoardIndex) {}
