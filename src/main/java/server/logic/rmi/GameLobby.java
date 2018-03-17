package server.logic.rmi;

import brugerautorisation.data.Bruger;
import server.controller.IUserController;
import server.controller.UserController;
import server.util.Utils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public final class GameLobby extends UnicastRemoteObject implements IGameLobby {

    private final Map<String, IGameLogic> loggedInMap = new HashMap<>();
    private final List<Bruger> loggedInUserObjectList = new ArrayList<>();

    private final Map<String, Integer> bootMap = new HashMap<>();

    private final IUserController userController = UserController.getInstance();

    public GameLobby() throws RemoteException {
        startBootingTimer();
    }

    @Override
    public void logIn(String username, String password) throws RemoteException {
        Bruger user;
        try {
            user = userController.getUser(username, password);
            System.out.println("User logged in: " + username + "!");
        } catch (IUserController.UserControllerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        System.out.println("Attempting to register GameLogic instance for user: " + username + "!");

        if (loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is already registered with a GameLogic instance!");

        if (isLoggedIn(username))
            throw new IllegalArgumentException(username + " is already a registered object user!");

        try {
            loggedInMap.put(username, new GameLogic());
            loggedInUserObjectList.add(user);
            System.out.println("Registered a GameLogic instance for user and object: " + username + "!");
        } catch (RemoteException e) {
            throw new RemoteException("Failed to register GameLogic instance for user: " + username + "!");
        }
    }

    @Override
    public void logOut(String username) throws RemoteException {
        System.out.println("Attempting to unregister: " + username + "!");

        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        System.out.println("Unregistered: " + username + "!");
        loggedInMap.remove(username);
        removeUserObject(username);
    }

    @Override
    public IGameLogic getGameLogicInstance(String username) throws RemoteException {
        System.out.println("Searching for GameLogic instance for user: " + username + "!");

        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        return loggedInMap.get(username);
    }

    @Override
    public List<String> getAllCurrentUserNames() throws RemoteException {
        System.out.println("Getting all registered user names!");
        return new ArrayList<>(loggedInMap.keySet());
    }

    @Override
    public int getUserAmount() throws RemoteException {
        System.out.println("Getting current user amount!");
        return loggedInMap.size();
    }


    @Override
    public Bruger getLoggedInUser(String username) throws RemoteException {
        for (Bruger user : loggedInUserObjectList)
            if (user.brugernavn.equals(username))
                return user;
        throw new RemoteException("Unable to find logged in user: " + username + "!");
    }

    @Override
    public boolean isLoggedIn(String username) throws RemoteException {
        for (Bruger user : loggedInUserObjectList)
            if (user.brugernavn.equals(username))
                return true;
        return false;
    }

    @Override
    public Bruger getUserWithHighestHighscore() throws RemoteException {
        List<Integer> highScoreList = new ArrayList<>();
        List<Bruger> userList = new ArrayList<>();

        for (Bruger user : loggedInUserObjectList) {
            try {
                String highscore = userController.getUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                highScoreList.add(Integer.parseInt(highscore));
                userList.add(user);
            } catch (IUserController.UserControllerException e) {
                throw new RemoteException(e.getMessage());
            }
        }

        int limit = highScoreList.size();
        int max = Integer.MIN_VALUE;
        int maxPos = -1;

        for (int i = 0; i < limit; i++) {
            int value = highScoreList.get(i);
            if (value > max) {
                max = value;
                maxPos = i;
            }
        }

        return userList.get(maxPos);
    }

    @Override
    public void setUserHighscore(String username, String highscore) throws RemoteException {
        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        Bruger user = getLoggedInUser(username);

        try {
            userController.setUserField(username, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY, highscore);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public String getUserHighscore(String username) throws RemoteException {
        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        Bruger user = getLoggedInUser(username);

        try {
            return userController.getUserField(username, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public Map<String, Integer> getAllUsersScore() throws RemoteException {
        Map<String, Integer> userScoreMap = new HashMap<>();

        for (Bruger user : loggedInUserObjectList) {
            int score = getGameLogicInstance(user.brugernavn).getScore();
            String username = user.brugernavn;
            userScoreMap.put(username, score);
        }

        return userScoreMap;
    }

    @Override
    public Map<String, Integer> getAllUsersHighscore() throws RemoteException {
        Map<String, Integer> userHighscoreMap = new HashMap<>();

        for (Bruger user : loggedInUserObjectList) {
            int highscore = Integer.parseInt(getUserHighscore(user.brugernavn));
            String username = user.brugernavn;
            userHighscoreMap.put(username, highscore);
        }

        return userHighscoreMap;
    }

    @Override
    public void sendUserEmail(String username, String password, String subject, String msg) throws RemoteException {
        try {
            userController.sendUserEmail(username, password, subject, msg);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void sendForgotPasswordEmail(String username, String msg) throws RemoteException {
        try {
            userController.sendForgotPasswordEmail(username, msg);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void changeUserPassword(String username, String oldPassword, String newPassword) throws RemoteException {
        try {
            userController.changeUserPassword(username, oldPassword, newPassword);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    private void removeUserObject(String username) {
        for (int i = 0; i < loggedInUserObjectList.size(); i++) {
            if (loggedInUserObjectList.get(i).brugernavn.equals(username)) {
                loggedInUserObjectList.remove(i);
                break;
            }
        }
    }

    private void startBootingTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Boot tick starting...");

                try {
                    System.out.println("Size before: " + getUserAmount());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < loggedInUserObjectList.size(); i++) {
                    String username = loggedInUserObjectList.get(i).brugernavn;
                    if (!bootMap.containsKey(username)) {
                        try {
                            System.out.println("New boot entry: " + username);
                            bootMap.put(username, getGameLogicInstance(username).getScore());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                List<String> toBeRemoved = new ArrayList<>();

                for (String username : bootMap.keySet()) {
                    try {
                        if (getGameLogicInstance(username).getScore() == bootMap.get(username))
                            toBeRemoved.add(username);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                for (String username : toBeRemoved) {
                    System.out.println("Booted: " + username);
                    try {
                        logOut(username);
                        bootMap.remove(username);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                // This shall happen at every tick
                // 1. Fill up the bootList, only with new members that is not already inside.
                // 2. Check to see if their current scores are equal to the saved scores. If yes, boot them.
                // 3. If not, save thier new score to their place.
                //

                try {
                    System.out.println("Size after: " + getUserAmount());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                System.out.println("Boot tick end.");
            }
        }, 0, 3600000);

    }

}