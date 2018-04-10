package server.logic.local;

import brugerautorisation.data.Bruger;
import server.logic.rmi.IGameLogic;

import java.util.List;
import java.util.Map;

public interface IGameLobby {
    void logIn(String username, String password) throws GameLobbyException;
    void logOut(String username) throws GameLobbyException;

    IGameLogic getGameLogicInstance(String username) throws GameLobbyException;
    List<String> getAllCurrentUserNames() throws GameLobbyException;

    int getUserAmount() throws GameLobbyException;
    Bruger getLoggedInUser(String username) throws GameLobbyException;
    boolean isLoggedIn(String username) throws GameLobbyException;
    Bruger getUserWithHighestHighscore() throws GameLobbyException;

    void setUserHighscore(String username, String highscore) throws GameLobbyException;
    String getUserHighscore(String username) throws GameLobbyException;
    Map<String, Integer> getAllLoggedInUsersScore() throws GameLobbyException;
    Map<String, Integer> getAllUsersHighscore() throws GameLobbyException;
    List<String> getAllWords() throws GameLobbyException;

    void sendUserEmail(String username, String password, String subject, String msg) throws GameLobbyException;
    void sendForgotPasswordEmail(String username, String msg) throws GameLobbyException;
    void changeUserPassword(String username, String oldPassword, String newPassword) throws GameLobbyException;

    class GameLobbyException extends Exception {
        public GameLobbyException(String msg) {
            super(msg);
        }
    }

}