package server.main.rmi;


import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;
import server.util.Utils;

import java.rmi.Naming;
import java.util.Map;

public final class MainLocalClient {

    public static void main(String[] args) throws Exception {
        IGameLobby lobby = (IGameLobby) Naming.lookup(Utils.RMI_LOBBY_STUB_URL_LOCAL);

        /* Log in */
        lobby.logIn("jacno", "xxx");
        lobby.logIn("s151641", "godkode");
        lobby.logIn("s155005", "anusic");

        System.out.println(lobby.getLoggedInUser("s151641"));
        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());

        IGameLogic logic = lobby.getGameLogicInstance("s151641");

        Map<String, Integer> map = lobby.getAllUsersHighscore();
        int i = 0;
        for (String key : map.keySet()) {
            int value = map.get(key);
            System.out.println(i + ": " + value);
            i++;
        }

        /* Log out */
        lobby.logOut("s151641");
        lobby.logOut("s155005");
        lobby.logOut("jacno");

        System.out.println(lobby.getAllUsersHighscore());

        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());
    }

}