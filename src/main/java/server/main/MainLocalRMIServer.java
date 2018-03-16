package server.main;

import server.logic.rmi.GameLobby;
import server.logic.rmi.IGameLobby;
import server.util.Utils;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public final class MainLocalRMIServer {

    public static void main(String[] args) throws Exception {

        try {
            LocateRegistry.createRegistry(Utils.LOCAL_PORT);
            System.setProperty("java.rmi.server.hostname", "localhost");
            System.out.println("Java RMI local register created.");
        } catch (RemoteException e) {
            System.out.println("Java RMI local register already created.");
        }

        IGameLobby lobby = new GameLobby();
        Naming.rebind(Utils.RMI_LOBBY_STUB_URL_LOCAL, lobby);
        System.out.println("Java RMI local GameLobby server started.");
    }

}