package cz.cvut.fel.pjv.Models;

/**
 * data needed by replayer to replay a move
 */
public record ReplayMoveInfo(Integer startFieldIndex, Integer endFieldIndex, boolean checkTraps) {
}
