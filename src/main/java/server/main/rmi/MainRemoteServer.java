package server.main.rmi;

import server.logic.rmi.GameLobby;
import server.logic.rmi.IGameLobby;
import server.util.Utils;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public final class MainRemoteServer {

    public static void main(String[] args) throws Exception {

        try {
            LocateRegistry.createRegistry(Utils.LOCAL_PORT);
            System.setProperty("java.rmi.server.hostname", "ubuntu4.javabog.dk");
            System.out.println("Java RMI remote register created.");
        } catch (RemoteException e) {
            System.out.println("Java RMI remote register already created.");
        }

        IGameLobby lobby = new GameLobby();
        Naming.rebind(Utils.RMI_STUB_URL_REMOTE_LOBBY_JAVABOG, lobby);
        System.out.println("Java RMI remote GameLobby server started.");
    }

}