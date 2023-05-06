package cz.cvut.fel.pjv.Models;

/**
 * describes a move with 3 board fields
 * @param startField start
 * @param endField destination
 * @param forceMoveField Board field, from which player is pulling or to which player is pushing. Might be null
 */
public record MoveInfo(BoardField startField, BoardField endField, BoardField forceMoveField) {}
