package server.logic.soap;

import brugerautorisation.data.Bruger;
import server.controller.IUserController;
import server.controller.UserController;
import server.util.Utils;

import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

@WebService(endpointInterface = "server.logic.soap.IGameLogic")
public final class GameLogic extends UnicastRemoteObject implements IGameLogic {

    private final int MAXIMUM_LIFE = 6;

    private int life = MAXIMUM_LIFE;

    private int time = 0;
    private int score = 0;

    private String word;
    private String hiddenWord;

    private List<String> wordList;
    private List<Character> usedCharList;

    private final IUserController userController = UserController.getInstance();


    public GameLogic() throws RemoteException {
        wordList = new ArrayList<>();
        usedCharList = new ArrayList<>();
        resetGame();

        try {
            System.out.println("GameLogic registered: " + RemoteServer.getClientHost());
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }

    /************************************************
     *         GAME LOGIC CODE RESIDES HERE!        *
     ************************************************/
    @Override
    public boolean guess(char ch) throws RemoteException {
        useChar(ch);

        if (word.contains(Character.toString(ch))) {
            removeChar(ch);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void resetGame() throws RemoteException {
        initWordList();
        word = getRandomWord();
        hiddenWord = createHiddenWord();
        stopGameTimer();
        life = MAXIMUM_LIFE;
        score = 0;
        usedCharList = new ArrayList<>();
    }


    @Override
    public final void startGameTimer() throws RemoteException {

    }

    @Override
    public final void stopGameTimer() throws RemoteException {

    }

    @Override
    public final int getGameTimeElapsed() throws RemoteException {
        return time;
    }

    @Override
    public final String getGuessedChars() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (Character c : usedCharList)
            sb.append(c);
        return sb.toString();
    }

    @Override
    public final String getCurrentGuessedWord() throws RemoteException {
        return hiddenWord;
    }

    @Override
    public int getCurrentLife() throws RemoteException {
        return life;
    }

    @Override
    public int getCurrentScore() throws RemoteException {
        return score;
    }

    @Override
    public final boolean isGameWon() throws RemoteException {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    @Override
    public final boolean isGameLost() throws RemoteException {
        return life == 0;
    }

    @Override
    public boolean isHighScore() throws RemoteException {
        if (!isLoggedIn())
            throw new RemoteException("Not logged in!");

        Bruger user;
        try {
            user = userController.getCurrentUser();
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException("Could not find logged in user!");
        }

        String scoreStr = getUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
        int userScore = Integer.parseInt(scoreStr);

        return score > userScore;
    }

    /************************************************
     *    USER AUTHORIZATION LOGIC RESIDES HERE!    *
     ************************************************/
    @Override
    public final void logIn(String username, String password) throws RemoteException {
        try {
            userController.logIn(username, password);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public final void logOut() throws RemoteException {
        try {
            userController.logOut();
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public final void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException {
        try {
            userController.setUserField(username, password, userFieldKey, value);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public final String getUserField(String username, String password, String userFieldKey) throws RemoteException {
        try {
            return userController.getUserField(username, password, userFieldKey);
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public final Bruger getCurrentUser() throws RemoteException {
        try {
            return userController.getCurrentUser();
        } catch (IUserController.UserControllerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public final boolean isLoggedIn() throws RemoteException {
        return userController.isLoggedIn();
    }

    /************************************************
     *        PRIVATE METHODS RESIDES HERE!         *
     ************************************************/
    private static String fetchURL(String url) throws IOException {
        System.out.println("Fetching data from: " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje).append("\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

    private void getWordsOnline() throws Exception {
        String data = fetchURL("https://dr.dk");

        data = data.substring(data.indexOf("<body")). // fjern headere
                replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
                replaceAll("&#198;", "æ"). // erstat HTML-tegn
                replaceAll("&#230;", "æ"). // erstat HTML-tegn
                replaceAll("&#216;", "ø"). // erstat HTML-tegn
                replaceAll("&#248;", "ø"). // erstat HTML-tegn
                replaceAll("&oslash;", "ø"). // erstat HTML-tegn
                replaceAll("&#229;", "å"). // erstat HTML-tegn
                replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] ", " "); // fjern 2-bogstavsord

        wordList.clear();
        wordList.addAll(new HashSet<>(Arrays.asList(data.split(" "))));
    }

    private boolean isCharGuessed(char ch) throws RemoteException {
        for (Character c : usedCharList)
            if (c == ch) return true;
        return false;
    }

    private void initWordList() {
        if (!wordList.isEmpty())
            return;

        try {
            getWordsOnline();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createHiddenWord() {
        if (word == null)
            word = getRandomWord();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++)
            sb.append("•");
        hiddenWord = sb.toString();

        return hiddenWord;
    }

    private void useChar(char ch) {
        if (usedCharList.size() == 0) {
            usedCharList.add(ch);
            return;
        }

        for (Character c : usedCharList)
            if (c == ch)
                return;

        usedCharList.add(ch);
    }

    private void removeChar(char ch) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ch) {
                char[] charArray = hiddenWord.toCharArray();
                charArray[i] = ch;
                hiddenWord = new String(charArray);
            }
        }
    }

    private void addGameScore(int amount) throws RemoteException {
        score += amount;
    }

    private int getWordScore() throws RemoteException {
        return hiddenWord.length();
    }

    private String getRandomWord() {
        if (wordList == null)
            initWordList();
        return wordList.get(new Random().nextInt(wordList.size()));
    }

}