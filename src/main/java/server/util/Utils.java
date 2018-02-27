package server.util;

public final class Utils {

    /* Fields */
    public final static String CMD_ARROW = "»";
    public final static String REMOTE_PORT = "21499";

    public final static String RMI_STUB_URL_USERS = "rmi://javabog.dk/brugeradmin";
    public final static String RMI_STUB_URL_LOCAL_LOGIC = "rmi://localhost:" + REMOTE_PORT + "/gamelogicservice";

    public final static String RMI_STUB_URL_REMOTE_LOGIC_JAVABOG = "rmi://ubuntu4.javabog.dk:" + REMOTE_PORT + "/gamelogicservice";
    public final static String RMI_STUB_URL_REMOTE_LOGIC = "rmi://localhost:" + REMOTE_PORT + "/gamelogicservice";
    public final static String HIGH_SCORE_FIELD_KEY = "s151641_highscore";

    public final static String SOAP_STUB_URL_REMOTE_LOGIC_JAVABOG = "http://[::]:22320/gamelogicservice";

    public static final int SINGLE_CHAR_SCORE = 10;

    public final static int MAXIMUM_LIFE = 6;

    /* Static Singleton instance */
    private static Utils instance;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new Utils();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton Utils instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private Utils() {

    }

    /*
     * Singleton instance getter.
     */
    public static synchronized Utils getInstance() {
        return instance;
    }

}