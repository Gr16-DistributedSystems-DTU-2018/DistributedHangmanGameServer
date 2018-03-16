package server.logic.rmi;

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
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void resetScore() throws RemoteException {
        try {
            logic.resetScore();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void resetGame() throws RemoteException {
        try {
            logic.resetGame();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getGuessedChars() throws RemoteException {
        try {
            return logic.getGuessedChars();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getWord() throws RemoteException {
        try {
            return logic.getWord();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public int getLife() throws RemoteException {
        try {
            return logic.getLife();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public int getScore() throws RemoteException {
        try {
            return logic.getScore();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isCharGuessed(char ch) throws RemoteException {
        try {
            return logic.isCharGuessed(ch);
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isGameWon() throws RemoteException {
        try {
            return logic.isGameWon();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isGameLost() throws RemoteException {
        try {
            return logic.isGameLost();
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean isHighScore(String username, String password) throws RemoteException {
        try {
            return logic.isHighScore(username, password);
        } catch (server.logic.local.IGameLogic.GameLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

}