package cz.cvut.fel.pjv.Models;

/**
 * DTO, used to send timer data to the view (whose timer is it and how much time left)
 */
public record TimerInfo(Time time,Color color) { }
