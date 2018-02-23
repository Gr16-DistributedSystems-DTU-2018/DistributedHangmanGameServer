package io.inabsentia.gameserver.main;

import io.inabsentia.gameserver.logic.rmi.GameLogic;
import io.inabsentia.gameserver.logic.rmi.IGameLogic;
import io.inabsentia.gameserver.util.Utils;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public final class MainRMI {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(Integer.parseInt(String.valueOf(Utils.REMOTE_PORT)));
        System.setProperty("java.rmi.server.hostname", "ubuntu4.javabog.dk");

        IGameLogic logic = new GameLogic();
        Naming.rebind(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG, logic);

        System.out.println("GameLogic (RMI) registered at: " + Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

}