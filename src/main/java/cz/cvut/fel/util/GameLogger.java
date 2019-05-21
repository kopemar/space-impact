package cz.cvut.fel.util;

import java.util.logging.Logger;

public class GameLogger extends Logger  {
    private static final Logger LOGGER = Logger.getLogger(GameLogger.class.getName());

    public GameLogger(String name, String resourceBundleName)
    {
        super(name, resourceBundleName);
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }
}
