package server.logic.rmi;

import brugerautorisation.data.Bruger;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameLogic extends Remote {
    /* Game Logic */
    boolean guess(char ch) throws RemoteException;

    void resetGame() throws RemoteException;

    void startGameTimer() throws RemoteException;
    void stopGameTimer() throws RemoteException;
    int getGameTimeElapsed() throws RemoteException;

    String getGuessedCharacters() throws RemoteException;

    String getCurrentGuessedWord() throws RemoteException;

    int getCurrentLife() throws RemoteException;
    int getCurrentScore() throws RemoteException;

    boolean isGameWon() throws RemoteException;
    boolean isGameLost() throws RemoteException;
    boolean isHighScore() throws RemoteException;

    /* User Authorization */
    void logIn(String username, String password) throws RemoteException;
    void logOut() throws RemoteException;

    void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException;
    String getUserField(String username, String password, String userFieldKey) throws RemoteException;

    Bruger getCurrentUser() throws RemoteException;
    boolean isLoggedIn() throws RemoteException;
}