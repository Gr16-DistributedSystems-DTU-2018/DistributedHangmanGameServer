package server.main.rmi;

import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;
import server.util.Utils;

import java.rmi.Naming;

public class MainRemoteClient {

    public static void main(String[] args) throws Exception {
        IGameLobby lobby = (IGameLobby) Naming.lookup(Utils.RMI_STUB_URL_REMOTE_LOBBY_JAVABOG);

        /* Log in */
        lobby.logIn("s151641", "godkode");
        lobby.logIn("s155005", "anusic");
        lobby.logIn("jacno", "xxx");

        System.out.println(lobby.getLoggedInUser("s151641"));
        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());

        IGameLogic logic = lobby.getGameLogicInstance("s151641");
        System.out.println("Highscore: " + lobby.getUserHighscore("s151641"));

        System.out.println("## All high scores ##");

        for (String user : lobby.getAllUsersScore().keySet()) {
            int highscore = lobby.getAllUsersHighscore().get(user);
            System.out.println(user + ": " + highscore);
        }

        /* Log out */
        lobby.logOut("s151641");
        lobby.logOut("s155005");
        lobby.logOut("jacno");

        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());
    }

}
