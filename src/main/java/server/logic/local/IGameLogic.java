package server.logic.local;

import brugerautorisation.data.Bruger;

public interface IGameLogic {
    /* Game Logic */
    boolean guess(char ch) throws GameException;

    void resetScore() throws GameException;
    void resetGame() throws GameException;

    String getGuessedChars() throws GameException;
    String getCurrentGuessedWord() throws GameException;

    int getCurrentLife() throws GameException;
    int getCurrentScore() throws GameException;

    boolean isCharGuessed(char ch) throws GameException;
    boolean isGameWon() throws GameException;
    boolean isGameLost() throws GameException;
    boolean isHighScore() throws GameException;

    /* User Authorization */
    void logIn(String username, String password) throws GameException;
    void logOut() throws GameException;

    void setUserField(String username, String password, String userFieldKey, String value) throws GameException;
    String getUserField(String username, String password, String userFieldKey) throws GameException;
    Bruger getCurrentUser() throws GameException;

    boolean isLoggedIn() throws GameException;

    class GameException extends Exception {

        public GameException(String msg) {
            super(msg);
        }
    }

}