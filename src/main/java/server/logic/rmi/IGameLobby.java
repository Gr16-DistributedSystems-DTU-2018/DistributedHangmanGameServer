package server.logic.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGameLobby extends Remote {
    IGameLogic getGameLogicInstance(String studentId) throws RemoteException;
    List<String> getAllGameLogicInstances() throws RemoteException;
    void registerGameLogic(String studentId) throws RemoteException;
    void unregisterGameLogic(String studentId) throws RemoteException;
}