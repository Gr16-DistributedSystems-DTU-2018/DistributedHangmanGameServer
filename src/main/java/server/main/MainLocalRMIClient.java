package server.main;

import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;
import server.util.Utils;

import java.rmi.Naming;

public final class MainLocalRMIClient {

    public static void main(String[] args) throws Exception {
        IGameLobby lobby = (IGameLobby) Naming.lookup(Utils.RMI_LOBBY_STUB_URL_LOCAL);
        lobby.registerGameLogic("s15s1641");

        IGameLogic logic = lobby.getGameLogicInstance("s15s1641");

        System.out.println(logic.guess('a'));
        System.out.println(logic.guess('b'));
        System.out.println(logic.guess('c'));
        System.out.println(logic.guess('d'));
        System.out.println(logic.guess('e'));

        System.out.println("Score: " + logic.getCurrentScore());
        System.out.println("Life: " + logic.getCurrentLife());


    }

}