package server.main;


import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;
import server.util.Utils;

import java.rmi.Naming;

public final class MainLocalRMIClient {

    public static void main(String[] args) throws Exception {
        IGameLobby lobby = (IGameLobby) Naming.lookup(Utils.RMI_LOBBY_STUB_URL_LOCAL);

        /* Log in */
        lobby.logIn("s151641", "godkode");
        lobby.logIn("s155005", "anusic");
        lobby.logIn("jacno", "xxx");

        System.out.println(lobby.getLoggedInUser("s151641"));
        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());


        IGameLogic logic = lobby.getGameLogicInstance("s151641");

        System.out.println("Life: " + logic.getLife());
        System.out.println("Score: " + logic.getScore());
        System.out.println("Word: " + logic.getWord());
        System.out.println("Guessed Chars: " + logic.getGuessedChars());

        System.out.println("Life: " + logic.getLife());
        System.out.println("Score: " + logic.getScore());
        System.out.println("Word: " + logic.getWord());
        System.out.println("Guessed Chars: " + logic.getGuessedChars());

        /* Log out */
        lobby.logOut("s151641");
        lobby.logOut("s155005");
        lobby.logOut("jacno");

        System.out.println("Users logged in: " + lobby.getUserAmount());
        System.out.println("All users logged in: " + lobby.getAllCurrentUserNames());
    }

}