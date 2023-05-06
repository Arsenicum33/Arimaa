package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.Models.PieceType;

import java.awt.*;

public interface IImageHandler
{
    Image getImage(PieceType pt);
}
