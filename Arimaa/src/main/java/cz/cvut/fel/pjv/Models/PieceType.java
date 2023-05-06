package cz.cvut.fel.pjv.Models;

/**
 * specifies type of piece and it's properties (strength and color)
 */
public enum PieceType
{
    RABBIT_G(0, Color.Gold),
    RABBIT_S(0, Color.Silver),
    CAT_G(1, Color.Gold),
    CAT_S(1, Color.Silver),
    DOG_G(2, Color.Gold),
    DOG_S(2, Color.Silver),
    HORSE_G(3, Color.Gold),
    HORSE_S(3, Color.Silver),
    CAMEL_G(4, Color.Gold),
    CAMEL_S(4, Color.Silver),
    ELEPHANT_G(5, Color.Gold),
    ELEPHANT_S(5, Color.Silver);
    private final int strength;
    private final Color color;
    PieceType(int strength, Color color)
    {
        this.strength=strength;
        this.color = color;
    }

    public static PieceType getStrongerAnimal(PieceType initial)
    {
        final int numberDifferentAnimals =6;
        for (var pt : PieceType.values())
        {
            if ((pt.strength==((initial.strength+1)%numberDifferentAnimals))&&pt.color==initial.color)
                return pt;
        }
        return null;
    }
    public Color getColor()
    {
        return color;
    }

    public int getStrength()
    {
        return strength;
    }
}
