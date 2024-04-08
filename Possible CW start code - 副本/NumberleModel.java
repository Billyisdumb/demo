// NumberleModel.java
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Random;
import java.util.Observable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class NumberleModel extends Observable implements INumberleModel {

    private String targetNumber;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;
    private List<String> equationList;



    public NumberleModel() {
        loadEquationsFromFile("equations.txt"); // 从文件加载方程列表
    }

    private void loadEquationsFromFile(String filename) {
        equationList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                equationList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(equationList.size());
        targetNumber = equationList.get(randomIndex);
        currentGuess = new StringBuilder("");
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        setChanged();
        notifyObservers();
        System.out.println(targetNumber);
    }

    public String getTargetNumber() {
        return targetNumber;
    }
    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    @Override
    public boolean processInput(String input) {
        isMean(String.valueOf(currentGuess));
        checkLength(String.valueOf(currentGuess));
        setChanged();
        notifyObservers();
        isGameOver();
        isGameWon();
        return true;
    }
    public boolean checkLength(String currentGuess) {
        if (currentGuess.length() == 7) {
            System.out.println("isMean" + remainingAttempts);
            remainingAttempts--;
        }
        return true;
    }
    public boolean isMean(String currentGuess) {
        if (currentGuess.length() < 7) {
            throw new IllegalArgumentException("Worry length");
        }
        if (!currentGuess.contains("+") && !currentGuess.contains("-") && !currentGuess.contains("*") && !currentGuess.contains("/") && !currentGuess.contains("=")) {
            throw new IllegalArgumentException("Not an equation");
        }
        // 猜测有效
        return true;
    }

    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0;
    }
    public Color[] getColors(String targetNumber, String currentGuess) {
        Color[] colors = new Color[targetNumber.length()];
        for (int i = 0; i < targetNumber.length(); i++) {
            char targetChar = targetNumber.charAt(i);
            char guessChar = (i < currentGuess.length()) ? currentGuess.charAt(i) : ' ';
            if (guessChar == ' ') {
                colors[i] = Color.gray;
            } else if (guessChar == targetChar) {
                colors[i] = Color.green;
            } else if (targetNumber.contains(Character.toString(guessChar))) {
                colors[i] = Color.yellow;
            } else {
                colors[i] = Color.gray;
            }
        }
        return colors;
    }


    @Override
    public boolean isGameWon() {
        return currentGuess.toString().equals(targetNumber);
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }
    @Override
    public void startNewGame() {
        initialize();
    }

}
