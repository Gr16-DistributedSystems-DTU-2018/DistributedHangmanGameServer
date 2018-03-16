package server.logic.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameLobby extends UnicastRemoteObject implements IGameLobby {

    private final Map<String, IGameLogic> clientMap = new HashMap<>();

    public GameLobby() throws RemoteException {

    }

    @Override
    public IGameLogic getGameLogicInstance(String studentId) throws RemoteException {
        System.out.println(getClass().getSimpleName() + ": Searching for GameLogic instance for " + studentId + ".");

        if (!clientMap.containsKey(studentId))
            throw new IllegalArgumentException(studentId + " not registered!");

        return clientMap.get(studentId);
    }

    @Override
    public List<String> getAllGameLogicInstances() throws RemoteException {
        System.out.println(getClass().getSimpleName() + ": Getting all GameLogic instances.");
        return new ArrayList<>(clientMap.keySet());
    }

    @Override
    public void registerGameLogic(String studentId) throws RemoteException {
        System.out.println(getClass().getSimpleName() + ": Attempting to register " + studentId + ".");

        if (clientMap.containsKey(studentId))
            throw new IllegalArgumentException(studentId + " already registered!");

        System.out.println(getClass().getSimpleName() + ": Registered " + studentId + ".");
        clientMap.put(studentId, new GameLogic());
    }

    @Override
    public void unregisterGameLogic(String studentId) throws RemoteException {
        System.out.println(getClass().getSimpleName() + ": Attempting to unregister " + studentId + ".");

        if (!clientMap.containsKey(studentId))
            throw new IllegalArgumentException(studentId + " not registered!");

        System.out.println(getClass().getSimpleName() + ": Unregistered " + studentId + ".");
        clientMap.remove(studentId);
    }

}