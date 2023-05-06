package cz.cvut.fel.pjv.Models;

/**
 * data needed to create a game
 */
public record StartGameInfo(boolean againstAI, int baseTime, int timeAddition) { }
