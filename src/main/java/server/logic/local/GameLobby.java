package server.logic.local;

import brugerautorisation.data.Bruger;
import server.controller.IUserController;
import server.controller.UserController;
import server.logic.rmi.GameLogic;
import server.logic.rmi.IGameLogic;
import server.util.Utils;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class GameLobby implements IGameLobby {

    private final Map<String, server.logic.rmi.IGameLogic> loggedInMap = new HashMap<>();
    private final List<Bruger> loggedInUserObjectList = new ArrayList<>();

    private final Map<String, Integer> highScoreMap = new TreeMap<>();
    private final Map<String, Integer> bootMap = new HashMap<>();

    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final IUserController userController = UserController.getInstance();

    public GameLobby() throws GameLobbyException {
        startBootingTimer();
    }

    @Override
    public void logIn(String username, String password) throws GameLobbyException {
        Bruger user;
        try {
            user = userController.getUser(username, password);
            logMessage("User logged in: " + username + "!");
        } catch (IUserController.UserControllerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        logMessage("Attempting to register GameLogic instance for user: " + username + "!");

        if (loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is already registered with a GameLogic instance!");

        if (isLoggedIn(username))
            throw new IllegalArgumentException(username + " is already a registered object user!");

        try {
            loggedInMap.put(username, new GameLogic());
            loggedInUserObjectList.add(user);
            logMessage("Registered a GameLogic instance for user and object: " + username + "!");

            /* Get their current high score and add to list */
            int highscore = Integer.valueOf(getUserHighscore(username));
            logMessage("Saved high score: " + username + " : " + highscore);
            highScoreMap.put(username, highscore);
        } catch (RemoteException e) {
            throw new GameLobbyException("Failed to register GameLogic instance for user: " + username + "!");
        }
    }

    @Override
    public void logOut(String username) throws GameLobbyException {
        logMessage("Attempting to unregister: " + username + "!");

        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        logMessage("Unregistered: " + username + "!");
        loggedInMap.remove(username);
        removeUserObject(username);
    }

    @Override
    public IGameLogic getGameLogicInstance(String username) throws GameLobbyException {
        logMessage("Searching for GameLogic instance for user: " + username + "!");

        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        return loggedInMap.get(username);
    }

    @Override
    public List<String> getAllCurrentUserNames() throws GameLobbyException {
        logMessage("Getting all registered user names!");
        return new ArrayList<>(loggedInMap.keySet());
    }

    @Override
    public int getUserAmount() throws GameLobbyException {
        logMessage("Getting current user amount!");
        return loggedInMap.size();
    }


    @Override
    public Bruger getLoggedInUser(String username) throws GameLobbyException {
        for (Bruger user : loggedInUserObjectList)
            if (user.brugernavn.equals(username))
                return user;
        throw new GameLobbyException("Unable to find logged in user: " + username + "!");
    }

    @Override
    public boolean isLoggedIn(String username) throws GameLobbyException {
        for (Bruger user : loggedInUserObjectList)
            if (user.brugernavn.equals(username))
                return true;
        return false;
    }

    @Override
    public Bruger getUserWithHighestHighscore() throws GameLobbyException {
        List<Integer> highScoreList = new ArrayList<>();
        List<Bruger> userList = new ArrayList<>();

        for (Bruger user : loggedInUserObjectList) {
            try {
                String highscore = userController.getUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                highScoreList.add(Integer.parseInt(highscore));
                userList.add(user);
            } catch (IUserController.UserControllerException e) {
                throw new GameLobbyException(e.getMessage());
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
    public void setUserHighscore(String username, String highscore) throws GameLobbyException {
        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        Bruger user = getLoggedInUser(username);

        try {
            userController.setUserField(username, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY, highscore);
            highScoreMap.put(username, Integer.valueOf(highscore));
        } catch (IUserController.UserControllerException e) {
            throw new GameLobbyException(e.getMessage());
        }
    }

    @Override
    public String getUserHighscore(String username) throws GameLobbyException {
        if (!loggedInMap.containsKey(username))
            throw new IllegalArgumentException(username + " is not registered with a GameLogic instance!");

        if (!isLoggedIn(username))
            throw new IllegalArgumentException(username + " is not a registered object user!");

        Bruger user = getLoggedInUser(username);

        try {
            return userController.getUserField(username, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
        } catch (IUserController.UserControllerException e) {
            throw new GameLobbyException(e.getMessage());
        }
    }

    @Override
    public Map<String, Integer> getAllLoggedInUsersScore() throws GameLobbyException {
        Map<String, Integer> userScoreMap = new HashMap<>();

        for (Bruger user : loggedInUserObjectList) {
            int score;
            try {
                score = getGameLogicInstance(user.brugernavn).getScore();
            } catch (RemoteException e) {
                throw new GameLobbyException(e.getMessage());
            }
            String username = user.brugernavn;
            userScoreMap.put(username, score);
        }

        return userScoreMap;
    }

    @Override
    public Map<String, Integer> getAllUsersHighscore() throws GameLobbyException {


        return highScoreMap;
    }

    @Override
    public void sendUserEmail(String username, String password, String subject, String msg) throws GameLobbyException {
        try {
            userController.sendUserEmail(username, password, subject, msg);
        } catch (IUserController.UserControllerException e) {
            throw new GameLobbyException(e.getMessage());
        }
    }

    @Override
    public void sendForgotPasswordEmail(String username, String msg) throws GameLobbyException {
        try {
            userController.sendForgotPasswordEmail(username, msg);
        } catch (IUserController.UserControllerException e) {
            throw new GameLobbyException(e.getMessage());
        }
    }

    @Override
    public void changeUserPassword(String username, String oldPassword, String newPassword) throws GameLobbyException {
        try {
            userController.changeUserPassword(username, oldPassword, newPassword);
        } catch (IUserController.UserControllerException e) {
            throw new GameLobbyException(e.getMessage());
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

    private void startBootingTimer() throws GameLobbyException {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logMessage("Boot tick starting...");

                try {
                    logMessage("Size before: " + getUserAmount());
                } catch (GameLobbyException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < loggedInUserObjectList.size(); i++) {
                    String username = loggedInUserObjectList.get(i).brugernavn;
                    if (!bootMap.containsKey(username)) {
                        try {
                            logMessage("New boot entry: " + username);
                            try {
                                bootMap.put(username, getGameLogicInstance(username).getScore());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } catch (GameLobbyException e) {
                            e.printStackTrace();
                        }
                    }
                }

                List<String> toBeRemoved = new ArrayList<>();

                for (String username : bootMap.keySet()) {
                    try {
                        if (getGameLogicInstance(username).getScore() == bootMap.get(username))
                            toBeRemoved.add(username);
                    } catch (GameLobbyException | RemoteException e) {
                        e.printStackTrace();
                    }
                }

                for (String username : toBeRemoved) {
                    logMessage("Booted: " + username);
                    try {
                        logOut(username);
                        bootMap.remove(username);
                    } catch (GameLobbyException e) {
                        e.printStackTrace();
                    }
                }

                // This shall happen at every tick
                // 1. Fill up the bootList, only with new members that is not already inside.
                // 2. Check to see if their current scores are equal to the saved scores. If yes, boot them.
                // 3. If not, save thier new score to their place.
                //

                try {
                    logMessage("Size after: " + getUserAmount());
                } catch (GameLobbyException e) {
                    e.printStackTrace();
                }

                logMessage("Boot tick end.");
            }
        }, 0, 1200000); // 20 minutes
    }

    private void logMessage(String msg) {
        String text = "[Server: " + dateFormat.format(new Date()) + "]: " + msg;
        System.out.println(text);
    }

}