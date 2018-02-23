package io.inabsentia.gameserver.logic.soap;

import brugerautorisation.data.Bruger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.Remote;
import java.rmi.RemoteException;

@WebService
public interface IGameLogic extends Remote {
    /* Game Logic */
    @WebMethod boolean guessCharacter(char character) throws RemoteException;
    @WebMethod void addGameScore(int score) throws RemoteException;

    @WebMethod void decreaseLife() throws RemoteException;
    @WebMethod void resetGame() throws RemoteException;

    @WebMethod void startGameTimer() throws RemoteException;
    @WebMethod void stopGameTimer() throws RemoteException;
    @WebMethod void resetGameTimer() throws RemoteException;
    @WebMethod int getGameTimeElapsed() throws RemoteException;

    @WebMethod String getUsedCharacters() throws RemoteException;

    @WebMethod String getHiddenWord() throws RemoteException;
    @WebMethod String getGameWord() throws RemoteException;

    @WebMethod int getGameLife() throws RemoteException;
    @WebMethod int getGameScore() throws RemoteException;
    @WebMethod int getWordScore() throws RemoteException;

    @WebMethod boolean isGameWon() throws RemoteException;
    @WebMethod boolean isGameLost() throws RemoteException;
    @WebMethod boolean isHighScore(int score) throws RemoteException;
    @WebMethod boolean isCharGuessed(char character) throws RemoteException;

    /* User Authorization */
    @WebMethod void logIn(String username, String password) throws RemoteException;
    @WebMethod void logOut() throws RemoteException;

    @WebMethod void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException;
    @WebMethod String getUserField(String username, String password, String userFieldKey) throws RemoteException;

    @WebMethod Bruger getCurrentUser() throws RemoteException;
    @WebMethod boolean isLoggedIn() throws RemoteException;
}