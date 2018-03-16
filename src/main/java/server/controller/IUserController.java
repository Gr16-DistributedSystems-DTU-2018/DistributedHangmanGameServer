package server.controller;

import brugerautorisation.data.Bruger;

public interface IUserController {
    Bruger getUser(String username, String password) throws UserControllerException;
    void setUserField(String username, String password, String userFieldKey, String value) throws UserControllerException;
    String getUserField(String username, String password, String userFieldKey) throws UserControllerException;
    void sendUserEmail(String username, String password, String subject, String msg) throws UserControllerException;
    void sendForgotPasswordEmail(String username, String msg) throws UserControllerException;
    void changeUserPassword(String username, String oldPassword, String newPassword) throws UserControllerException;

    class UserControllerException extends Exception {
        public UserControllerException(String msg) {
            super(msg);
        }
    }

}