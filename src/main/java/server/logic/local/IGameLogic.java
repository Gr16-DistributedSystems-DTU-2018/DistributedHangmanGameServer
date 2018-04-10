package server.logic.local;

public interface IGameLogic {
    boolean guess(char ch) throws GameLogicException;

    void resetScore() throws GameLogicException;
    void resetGame() throws GameLogicException;

    String getGuessedChars() throws GameLogicException;
    String getWord() throws GameLogicException;
    void setWord(String word) throws GameLogicException;

    int getLife() throws GameLogicException;
    int getScore() throws GameLogicException;

    boolean isCharGuessed(char ch) throws GameLogicException;
    boolean isGameWon() throws GameLogicException;
    boolean isGameLost() throws GameLogicException;
    boolean isHighScore(String username, String password) throws GameLogicException;

    class GameLogicException extends Exception {
        public GameLogicException(String msg) {
            super(msg);
        }
    }

}