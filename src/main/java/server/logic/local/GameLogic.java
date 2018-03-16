package server.logic.local;

import brugerautorisation.data.Bruger;
import server.controller.IUserController;
import server.controller.UserController;
import server.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

public final class GameLogic implements IGameLogic, Serializable {

    public static final int MAXIMUM_LIFE = 6;
    public static final int SINGLE_CHAR_GUESS_SCORE = 10;
    public static final int LIFE_DECREMENT = 1;

    private int life = MAXIMUM_LIFE;

    private int score = 0;

    private String word;
    private String hiddenWord;

    private List<String> wordList;
    private List<Character> usedCharList;

    private final IUserController userController = UserController.getInstance();

    public GameLogic() {
        wordList = new ArrayList<>();
        usedCharList = new ArrayList<>();

        try {
            resetScore();
            resetGame();
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    /************************************************
     *         GAME LOGIC CODE RESIDES HERE!        *
     ************************************************/
    @Override
    public boolean guess(char ch) throws GameException {
        useChar(ch);

        if (word.contains(Character.toString(ch))) {
            removeChar(ch);
            addGameScore(SINGLE_CHAR_GUESS_SCORE);

            if (isGameWon())
                addGameScore(getWordScore());

            return true;
        } else {
            if (life > 0)
                life -= LIFE_DECREMENT;
            return false;
        }
    }

    @Override
    public void resetScore() throws GameException {
        score = 0;
    }

    @Override
    public final void resetGame() throws GameException {
        initWordList();
        word = getRandomWord();
        hiddenWord = createHiddenWord();
        life = MAXIMUM_LIFE;
        usedCharList = new ArrayList<>();
    }

    @Override
    public final String getGuessedChars() throws GameException {
        StringBuilder sb = new StringBuilder();
        for (Character c : usedCharList)
            sb.append(c);
        return sb.toString();
    }

    @Override
    public final String getCurrentGuessedWord() throws GameException {
        return hiddenWord;
    }

    @Override
    public int getCurrentLife() throws GameException {
        return life;
    }

    @Override
    public int getCurrentScore() throws GameException {
        return score;
    }

    @Override
    public boolean isCharGuessed(char ch) throws GameException {
        for (Character c : usedCharList)
            if (c == ch) return true;
        return false;
    }

    @Override
    public final boolean isGameWon() throws GameException {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    @Override
    public final boolean isGameLost() throws GameException {
        return life == 0;
    }

    @Override
    public boolean isHighScore() throws GameException {
        if (!isLoggedIn())
            throw new GameException("Not logged in!");

        Bruger user;
        try {
            user = userController.getCurrentUser();
        } catch (IUserController.UserControllerException e) {
            throw new GameException("Could not find logged in user!");
        }

        String scoreStr = getUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
        int userScore = Integer.parseInt(scoreStr);

        return score > userScore;
    }

    /************************************************
     *    USER AUTHORIZATION LOGIC RESIDES HERE!    *
     ************************************************/
    @Override
    public final void logIn(String username, String password) throws GameException {
        try {
            userController.logIn(username, password);
        } catch (IUserController.UserControllerException e) {
            throw new GameException(e.getMessage());
        }
    }

    @Override
    public final void logOut() throws GameException {
        try {
            userController.logOut();
        } catch (IUserController.UserControllerException e) {
            throw new GameException(e.getMessage());
        }
    }

    @Override
    public final void setUserField(String username, String password, String userFieldKey, String value) throws GameException {
        try {
            userController.setUserField(username, password, userFieldKey, value);
        } catch (IUserController.UserControllerException e) {
            throw new GameException(e.getMessage());
        }
    }

    @Override
    public final String getUserField(String username, String password, String userFieldKey) throws GameException {
        try {
            return userController.getUserField(username, password, userFieldKey);
        } catch (IUserController.UserControllerException e) {
            throw new GameException(e.getMessage());
        }
    }

    @Override
    public final Bruger getCurrentUser() throws GameException {
        try {
            return userController.getCurrentUser();
        } catch (IUserController.UserControllerException e) {
            throw new GameException(e.getMessage());
        }
    }

    @Override
    public final boolean isLoggedIn() throws GameException {
        return userController.isLoggedIn();
    }

    /************************************************
     *        PRIVATE METHODS RESIDES HERE!         *
     ************************************************/
    private static String fetchURL(String url) throws IOException {
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

    private void addGameScore(int amount) {
        score += amount;
    }

    private int getWordScore() {
        return hiddenWord.length();
    }

    private String getRandomWord() {
        if (wordList == null)
            initWordList();
        return wordList.get(new Random().nextInt(wordList.size()));
    }

}