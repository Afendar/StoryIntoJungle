package core;

import java.util.Locale;

/**
 * Defines class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Defines {
    public static final float   GRAVITY         = 0.2f;
    public static final float   MAX_SPEED       = 5;
    public static final float   MIN_SPEED       = 1;
    public static final int     TILE_SIZE       = 64;
    public static final int     SPEED           = 5;
    public static final int     LEVEL_MAX       = 7;
    public static final int     START_LEVEL     = 1;
    public static final int     DEFAULT_SCREEN_WIDTH    = 800;
    public static final int     DEFAULT_SCREEN_HEIGHT   = 600;
    public static final String  VERSION         = "3.0";
    public static final boolean DEV             = false;
    public static final Locale  langs[]         = {new Locale("en","EN"), new Locale("fr", "FR")};
    public static final String  DATA_DIRECTORY  = "/data";
    public static final String  SAVES_DIRECTORY = "/saves";
}
