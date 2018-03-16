package server.controller;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import server.util.Utils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class UserController implements IUserController {

    private Brugeradmin userAdmin;

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
                userAdmin = (Brugeradmin) Naming.lookup(Utils.RMI_STUB_URL_BRUGERAUTORISATION);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                throw new RuntimeException("Failed to initialize Brugerautorisation RMI STUB: " + e.getMessage());
            }
        }
    }

    public static synchronized IUserController getInstance() {
        return instance;
    }

    @Override
    public Bruger getUser(String username, String password) throws UserControllerException {
        try {
            return userAdmin.hentBruger(username, password);
        } catch (RemoteException e) {
            throw new UserControllerException("Invalid credentials!");
        }
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
            throw new UserControllerException("No user field found at key '" + userFieldKey + " for user " + username + "!");
        }
    }

    @Override
    public void sendUserEmail(String username, String password, String subject, String msg) throws UserControllerException {
        try {
            userAdmin.sendEmail(username, password, subject, msg);
        } catch (RemoteException e) {
            throw new UserControllerException("Failed to send email for user: " + username);
        }
    }

    @Override
    public void sendForgotPasswordEmail(String username, String msg) throws UserControllerException {
        try {
            userAdmin.sendGlemtAdgangskodeEmail(username, msg);
        } catch (RemoteException e) {
            throw new UserControllerException("Failed to send forgotPasswordEmail for user: " + username);
        }
    }

    @Override
    public void changeUserPassword(String username, String oldPassword, String newPassword) throws UserControllerException {
        try {
            userAdmin.ændrAdgangskode(username, oldPassword, newPassword);
        } catch (RemoteException e) {
            throw new UserControllerException("Failed to change password for user: " + username);
        }
    }

}