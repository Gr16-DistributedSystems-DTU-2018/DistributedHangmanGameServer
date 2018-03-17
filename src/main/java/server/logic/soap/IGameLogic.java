package server.logic.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.RemoteException;

@WebService
public interface IGameLogic {
    @WebMethod boolean guess(char ch) throws RemoteException;

    @WebMethod void resetScore() throws RemoteException;
    @WebMethod void resetGame() throws RemoteException;

    @WebMethod String getGuessedChars() throws RemoteException;
    @WebMethod String getWord() throws RemoteException;
    @WebMethod int getLife() throws RemoteException;
    @WebMethod int getScore() throws RemoteException;

    @WebMethod boolean isCharGuessed(char ch) throws RemoteException;
    @WebMethod boolean isGameWon() throws RemoteException;
    @WebMethod boolean isGameLost() throws RemoteException;
    @WebMethod boolean isHighScore(String username, String password) throws RemoteException;
}