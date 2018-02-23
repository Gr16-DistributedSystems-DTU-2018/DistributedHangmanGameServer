package io.inabsentia.gameserver.main;

import io.inabsentia.gameserver.util.Utils;

import java.rmi.registry.LocateRegistry;

public final class Main {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(Integer.parseInt(String.valueOf(Utils.REMOTE_PORT)));
        System.setProperty("java.rmi.server.hostname", "ubuntu4.javabog.dk");

        //IGameLogic logic = new GameLogic();
        //Naming.rebind(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG, logic);

        System.out.println("GameLogic registered at: " + Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

}