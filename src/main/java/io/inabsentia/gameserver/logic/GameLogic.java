package io.inabsentia.gameserver.logic;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import io.inabsentia.gameserver.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.*;

public final class GameLogic extends UnicastRemoteObject implements IGameLogic {

    private static final int MAXIMUM_LIFE = 6;

    private int life = MAXIMUM_LIFE;
    private int time = 0;
    private int score = 0;

    private String word;
    private String hiddenWord;

    private List<String> wordList;
    private List<Character> usedCharactersList;

    private Brugeradmin userController;
    private Bruger currentUser;

    public GameLogic() throws RemoteException {
        wordList = new ArrayList<>();
        usedCharactersList = new ArrayList<>();
        resetGame();

        if (userController == null) {
            try {
                userController = (Brugeradmin) Naming.lookup(Utils.RMI_STUB_URL_USERS);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                throw new RuntimeException("Failed initializing RMI stub: " + Utils.RMI_STUB_URL_USERS);
            }
        }

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
    public boolean guessCharacter(char character) throws RemoteException {
        useCharacter(character);

        if (word.contains(Character.toString(character))) {
            removeCharacter(character);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addGameScore(int score) throws RemoteException {
        this.score += score;
    }

    @Override
    public void decreaseLife() throws RemoteException {
        life--;
    }

    @Override
    public void resetGame() throws RemoteException {
        initWordList();
        word = getRandomWord();
        hiddenWord = createHiddenWord();
        stopGameTimer();
        resetGameTimer();
        life = MAXIMUM_LIFE;
        score = 0;
        usedCharactersList = new ArrayList<>();
    }

    @Override
    public void startGameTimer() throws RemoteException {

    }

    @Override
    public void stopGameTimer() throws RemoteException {

    }

    @Override
    public void resetGameTimer() throws RemoteException {
        time = 0;
    }

    @Override
    public int getGameTimeElapsed() throws RemoteException {
        return time;
    }

    @Override
    public String getUsedCharacters() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (Character c : usedCharactersList)
            sb.append(c);
        return sb.toString();
    }

    @Override
    public String getHiddenWord() throws RemoteException {
        return hiddenWord;
    }

    @Override
    public String getGameWord() throws RemoteException {
        return word;
    }

    @Override
    public int getWordScore() throws RemoteException {
        return hiddenWord.length();
    }

    @Override
    public boolean isGameWon() throws RemoteException {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    @Override
    public boolean isGameLost() throws RemoteException {
        return life == 0;
    }

    @Override
    public boolean isHighScore(int score) throws RemoteException {
        if (!isLoggedIn())
            throw new RemoteException("Not logged in!");

        String scoreStr = getUserField(currentUser.brugernavn, currentUser.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
        int userScore = Integer.parseInt(scoreStr);

        return score > userScore;
    }

    @Override
    public boolean isCharGuessed(char character) throws RemoteException {
        for (Character c : usedCharactersList)
            if (c == character) return true;
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

    private String getRandomWord() {
        if (wordList == null)
            initWordList();
        return wordList.get(new Random().nextInt(wordList.size()));
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

    private void useCharacter(char letter) {
        if (usedCharactersList.size() == 0) {
            usedCharactersList.add(letter);
            return;
        }

        for (Character c : usedCharactersList)
            if (c == letter)
                return;

        usedCharactersList.add(letter);
    }

    private void removeCharacter(char letter) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                char[] charArray = hiddenWord.toCharArray();
                charArray[i] = letter;
                hiddenWord = new String(charArray);
            }
        }
    }

    @Override
    public int getGameLife() throws RemoteException {
        return life;
    }

    @Override
    public int getGameScore() throws RemoteException {
        return score;
    }

    /************************************************
     *    USER AUTHORIZATION LOGIC RESIDES HERE!    *
     ************************************************/
    @Override
    public void logIn(String username, String password) throws RemoteException {
        try {
            currentUser = userController.hentBruger(username, password);
            System.out.println("[" + LocalDateTime.now() + "]: " + username + " has logged in.");
        } catch (Exception e) {
            throw new RemoteException("Log In failed!");
        }
    }

    @Override
    public void logOut() throws RemoteException {
        if (!isLoggedIn())
            throw new RemoteException("Not logged in!");
        System.out.println("[" + LocalDateTime.now() + "]: " + currentUser.brugernavn + " has logged out.");
        currentUser = null;
    }

    @Override
    public void setUserField(String username, String password, String userFieldKey, String value) throws RemoteException {
        try {
            userController.setEkstraFelt(username, password, userFieldKey, value);
        } catch (RemoteException e) {
            throw new RemoteException("Failed setting user field '" + value + "' at key '" + userFieldKey + "!");
        }
    }

    @Override
    public String getUserField(String username, String password, String userFieldKey) throws RemoteException {
        try {
            Object userField = userController.getEkstraFelt(username, password, userFieldKey);
            String userFieldString = (String) userField;

            if (userFieldKey.equals(Utils.HIGH_SCORE_FIELD_KEY))
                if (userFieldString == null || userFieldString.equals("null"))
                    userFieldString = "0";

            return userFieldString;
        } catch (RemoteException e) {
            throw new RemoteException("No user field found at key '" + userFieldKey + "!");
        }
    }

    @Override
    public Bruger getCurrentUser() throws RemoteException {
        if (!isLoggedIn())
            throw new RemoteException("Not logged in!");
        return currentUser;
    }

    @Override
    public boolean isLoggedIn() throws RemoteException {
        return currentUser != null;
    }

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

}