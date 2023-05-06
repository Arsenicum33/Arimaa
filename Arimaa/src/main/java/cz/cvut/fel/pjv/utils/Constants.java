package cz.cvut.fel.pjv.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants
{
    public static final int BOARD_SIZE=8;
    public static final int BOARD_SQUARE=64;
    public static final String imagesDirPath = "Arimaa/Images/";
    public static int SQUARE_SIZE=80;
    public static String REPLAYS_PATH = "Arimaa/Replays/";

    public static final Set<Integer> TRAP_INDEXES = new HashSet<>(Arrays.asList(18, 21, 42, 45));
    private Constants() {}
}
