package server.logic.soap;

import brugerautorisation.data.Bruger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

@WebService
public interface IGameLobby {
    @WebMethod void logIn(String username, String password) throws RemoteException;
    @WebMethod void logOut(String username) throws RemoteException;

    @WebMethod GameLogic getGameLogicInstance(String username) throws RemoteException;
    @WebMethod List<String> getAllCurrentUserNames() throws RemoteException;
    @WebMethod int getUserAmount() throws RemoteException;
    @WebMethod  Bruger getLoggedInUser(String username) throws RemoteException;

    @WebMethod boolean isLoggedIn(String username) throws RemoteException;
    @WebMethod Bruger getUserWithHighestHighscore() throws RemoteException;

    @WebMethod void setUserHighscore(String username, String highscore) throws RemoteException;
    @WebMethod String getUserHighscore(String username) throws RemoteException;
    @WebMethod Map<String, Integer> getAllUsersScore() throws RemoteException;
    @WebMethod Map<String, Integer> getAllUsersHighscore() throws RemoteException;

    @WebMethod void sendUserEmail(String username, String password, String subject, String msg) throws RemoteException;
    @WebMethod void sendForgotPasswordEmail(String username, String msg) throws RemoteException;
    @WebMethod void changeUserPassword(String username, String oldPassword, String newPassword) throws RemoteException;
}