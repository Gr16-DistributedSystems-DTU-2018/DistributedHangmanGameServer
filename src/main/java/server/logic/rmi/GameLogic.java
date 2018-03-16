package server.logic.rmi;

import brugerautorisation.data.Bruger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class GameLogic extends UnicastRemoteObject implements IGameLogic {

    private server.logic.local.IGameLogic logic;

    public GameLogic() throws RemoteException {
        logic = new server.logic.local.GameLogic();
    }

    @Override
    public boolean guess(char ch) throws RemoteException {
        try {
            return logic.guess(ch);
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void resetScore() throws RemoteException {
        try {
            logic.resetScore();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void resetGame() throws RemoteException {
        try {
            logic.resetGame();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getGuessedChars() throws RemoteException {
        try {
            return logic.getGuessedChars();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getCurrentGuessedWord() throws RemoteException {
        try {
            return logic.getCurrentGuessedWord();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public int getCurrentLife() throws RemoteException {
        try {
            return logic.getCurrentLife();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public int getCurrentScore() throws RemoteException {
        try {
            return logic.getCurrentScore();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isCharGuessed(char ch) throws RemoteException {
        try {
            return logic.isCharGuessed(ch);
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isGameWon() throws RemoteException {
        try {
            return logic.isGameWon();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isGameLost() throws RemoteException {
        try {
            return logic.isGameLost();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isHighScore() throws RemoteException {
        try {
            return logic.isHighScore();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void logIn(String username, String password) throws RemoteException {
        try {
            logic.logIn(username, password);
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void logOut() throws RemoteException {
        try {
            logic.logOut();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException {
        try {
            logic.setUserField(username, password, userFieldKey, value);
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getUserField(String username, String password, String userFieldKey) throws RemoteException {
        try {
            return logic.getUserField(username, password, userFieldKey);
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Bruger getCurrentUser() throws RemoteException {
        try {
            return logic.getCurrentUser();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isLoggedIn() throws RemoteException {
        try {
            return logic.isLoggedIn();
        } catch (server.logic.local.IGameLogic.GameException e) {
            throw new RemoteException(e.getMessage());
        }
    }

}