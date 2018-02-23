package io.inabsentia.gameserver.logic.rmi;

import brugerautorisation.data.Bruger;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameLogic extends Remote {
    /* Game Logic */
    boolean guessCharacter(char character) throws RemoteException;
    void addGameScore(int score) throws RemoteException;

    void decreaseLife() throws RemoteException;
    void resetGame() throws RemoteException;

    void startGameTimer() throws RemoteException;
    void stopGameTimer() throws RemoteException;
    void resetGameTimer() throws RemoteException;
    int getGameTimeElapsed() throws RemoteException;

    String getUsedCharacters() throws RemoteException;

    String getHiddenWord() throws RemoteException;
    String getGameWord() throws RemoteException;

    int getGameLife() throws RemoteException;
    int getGameScore() throws RemoteException;
    int getWordScore() throws RemoteException;

    boolean isGameWon() throws RemoteException;
    boolean isGameLost() throws RemoteException;
    boolean isHighScore(int score) throws RemoteException;
    boolean isCharGuessed(char character) throws RemoteException;

    /* User Authorization */
    void logIn(String username, String password) throws RemoteException;
    void logOut() throws RemoteException;

    void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException;
    String getUserField(String username, String password, String userFieldKey) throws RemoteException;

    Bruger getCurrentUser() throws RemoteException;
    boolean isLoggedIn() throws RemoteException;
}