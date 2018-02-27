package server.controller;

import brugerautorisation.data.Bruger;

public interface IUserController {
    void logIn(String username, String password) throws UserControllerException;
    void logOut() throws UserControllerException;

    void setUserField(String username, String password, String userFieldKey, String value) throws UserControllerException;
    String getUserField(String username, String password, String userFieldKey) throws UserControllerException;

    Bruger getCurrentUser() throws UserControllerException;
    boolean isLoggedIn();

    class UserControllerException extends Exception {

        public UserControllerException(String msg) {
            super(msg);
        }

    }

}