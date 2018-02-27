package server.controller;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import server.util.Utils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public final class UserController implements IUserController {

    private Brugeradmin userAdmin;
    private Bruger currentUser;

    private static IUserController instance;

    static {
        try {
            instance = new UserController();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton UserController instance!");
        }
    }

    private UserController() {
        if (userAdmin == null) {
            try {
                userAdmin = (Brugeradmin) Naming.lookup(Utils.RMI_STUB_URL_USERS);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                throw new RuntimeException("Failed initializing Brugerautorisation STUB: " + e.getMessage());
            }
        }
    }

    public static synchronized IUserController getInstance() {
        return instance;
    }

    @Override
    public void logIn(String username, String password) throws UserControllerException {
        try {
            currentUser = userAdmin.hentBruger(username, password);
            System.out.println("[" + LocalDateTime.now() + "]: " + username + " has logged in.");
        } catch (Exception e) {
            throw new UserControllerException("Log In failed!");
        }
    }

    @Override
    public void logOut() throws UserControllerException {
        if (!isLoggedIn())
            throw new UserControllerException("Not logged in!");
        System.out.println("[" + LocalDateTime.now() + "]: " + currentUser.brugernavn + " has logged out.");
        currentUser = null;
    }

    @Override
    public void setUserField(String username, String password, String userFieldKey, String value) throws UserControllerException {
        try {
            userAdmin.setEkstraFelt(username, password, userFieldKey, value);
        } catch (RemoteException e) {
            throw new UserControllerException("Failed setting user field '" + value + "' at key '" + userFieldKey + "!");
        }
    }

    @Override
    public String getUserField(String username, String password, String userFieldKey) throws UserControllerException {
        try {
            Object userField = userAdmin.getEkstraFelt(username, password, userFieldKey);
            String userFieldString = (String) userField;

            if (userFieldKey.equals(Utils.HIGH_SCORE_FIELD_KEY))
                if (userFieldString == null || userFieldString.equals("null"))
                    userFieldString = "0";

            return userFieldString;
        } catch (RemoteException e) {
            throw new UserControllerException("No user field found at key '" + userFieldKey + "!");
        }
    }

    @Override
    public Bruger getCurrentUser() throws UserControllerException {
        if (!isLoggedIn())
            throw new UserControllerException("Not logged in!");
        return currentUser;
    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

}