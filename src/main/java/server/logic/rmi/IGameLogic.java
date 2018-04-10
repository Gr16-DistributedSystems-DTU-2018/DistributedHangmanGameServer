package server.logic.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameLogic extends Remote {
    boolean guess(char ch) throws RemoteException;

    void resetScore() throws RemoteException;
    void resetGame() throws RemoteException;

    String getGuessedChars() throws RemoteException;
    String getWord() throws RemoteException;
    void setWord(String word) throws RemoteException;

    int getLife() throws RemoteException;
    int getScore() throws RemoteException;

    boolean isCharGuessed(char ch) throws RemoteException;
    boolean isGameWon() throws RemoteException;
    boolean isGameLost() throws RemoteException;
    boolean isHighScore(String username, String password) throws RemoteException;
}