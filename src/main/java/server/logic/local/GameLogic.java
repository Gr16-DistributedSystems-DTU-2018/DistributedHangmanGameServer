package server.logic.local;

import brugerautorisation.data.Bruger;
import server.controller.IUserController;
import server.controller.UserController;
import server.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public GameLogic(List<String> wordList) {
        this.wordList = wordList;
        System.out.println(wordList);
        usedCharList = new ArrayList<>();

        try {
            resetScore();
            resetGame();
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    /************************************************
     *         GAME LOGIC CODE RESIDES HERE!        *
     ************************************************/
    @Override
    public boolean guess(char ch) throws GameLogicException {
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
    public void resetScore() throws GameLogicException {
        score = 0;
    }

    @Override
    public final void resetGame() throws GameLogicException {
        word = getRandomWord();
        hiddenWord = createHiddenWord();
        life = MAXIMUM_LIFE;
        usedCharList = new ArrayList<>();
    }

    @Override
    public final String getGuessedChars() throws GameLogicException {
        StringBuilder sb = new StringBuilder();
        for (Character c : usedCharList)
            sb.append(c);
        return sb.toString();
    }

    @Override
    public final String getWord() throws GameLogicException {
        return hiddenWord;
    }

    @Override
    public void setWord(String word) throws GameLogicException {
        this.word = word;
    }

    @Override
    public int getLife() throws GameLogicException {
        return life;
    }

    @Override
    public int getScore() throws GameLogicException {
        return score;
    }

    @Override
    public boolean isCharGuessed(char ch) throws GameLogicException {
        for (Character c : usedCharList)
            if (c == ch) return true;
        return false;
    }

    @Override
    public final boolean isGameWon() throws GameLogicException {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    @Override
    public final boolean isGameLost() throws GameLogicException {
        return life == 0;
    }

    @Override
    public boolean isHighScore(String username, String password) throws GameLogicException {
        Bruger user;
        try {
            user = userController.getUser(username, password);
        } catch (IUserController.UserControllerException e) {
            throw new GameLogicException(e.getMessage());
        }

        String scoreStr = null;
        int userScore = 0;

        try {
            scoreStr = userController.getUserField(username, password, Utils.HIGH_SCORE_FIELD_KEY);
            userScore = Integer.parseInt(scoreStr);
        } catch (IUserController.UserControllerException e) {
            e.printStackTrace();
            return false;
        }

        return score > userScore;
    }

    /************************************************
     *        PRIVATE METHODS RESIDES HERE!         *
     ************************************************/
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
        return wordList.get(new Random().nextInt(wordList.size()));
    }

}