package server.util;

public final class Utils {

    public final static int LOCAL_PORT = 21499;
    public final static int REMOTE_PORT = 21499;

    public final static String RMI_LOBBY_STUB_URL_LOCAL = "rmi://localhost:" + REMOTE_PORT + "/lobby";

    public final static String RMI_STUB_URL_BRUGERAUTORISATION = "rmi://javabog.dk/brugeradmin";

    public final static String RMI_STUB_URL_REMOTE_LOBBY_JAVABOG = "rmi://ubuntu4.javabog.dk:" + REMOTE_PORT + "/lobby";

    public final static String HIGH_SCORE_FIELD_KEY = "s151641_highscore";

    private static Utils instance;

    static {
        try {
            instance = new Utils();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton Utils instance!");
        }
    }

    private Utils() {

    }

    public static synchronized Utils getInstance() {
        return instance;
    }

}