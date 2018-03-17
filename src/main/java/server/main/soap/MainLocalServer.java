package server.main.soap;

import server.logic.soap.GameLobby;

import javax.xml.ws.Endpoint;
import java.rmi.RemoteException;

public final class MainLocalServer {

    public static void main(String[] args) throws RemoteException {
        System.out.println("Java SOAP local register created.");
        GameLobby lobby = new GameLobby();
        Endpoint.publish("http://[::]:9901/lobby", lobby);
        System.out.println("Java SOAP local GameLobby server started.");
    }

}