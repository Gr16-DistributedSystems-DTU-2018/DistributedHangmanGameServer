package server.logic.soap;

import brugerautorisation.data.Bruger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.Remote;
import java.rmi.RemoteException;

@WebService
public interface IGameLogic extends Remote {
    /* Game Logic */
    @WebMethod boolean guess(char ch) throws RemoteException;

    @WebMethod void resetScore() throws RemoteException;
    @WebMethod void resetGame() throws RemoteException;

    @WebMethod void startGameTimer() throws RemoteException;
    @WebMethod void stopGameTimer() throws RemoteException;
    @WebMethod int getGameTimeElapsed() throws RemoteException;

    @WebMethod String getGuessedChars() throws RemoteException;

    @WebMethod String getCurrentGuessedWord() throws RemoteException;

    @WebMethod int getCurrentLife() throws RemoteException;
    @WebMethod int getCurrentScore() throws RemoteException;

    @WebMethod boolean isCharGuessed(char ch) throws RemoteException;
    @WebMethod boolean isGameWon() throws RemoteException;
    @WebMethod boolean isGameLost() throws RemoteException;
    @WebMethod boolean isHighScore() throws RemoteException;

    /* User Authorization */
    @WebMethod void logIn(String username, String password) throws RemoteException;
    @WebMethod void logOut() throws RemoteException;

    @WebMethod void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException;
    @WebMethod String getUserField(String username, String password, String userFieldKey) throws RemoteException;

    @WebMethod Bruger getCurrentUser() throws RemoteException;
    @WebMethod boolean isLoggedIn() throws RemoteException;
}