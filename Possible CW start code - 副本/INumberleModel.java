import java.awt.*;
import java.util.List;

public interface INumberleModel {
    int MAX_ATTEMPTS = 6;
    int Length=7;
    int Height =6;

    void initialize();
    boolean processInput(String input);
    boolean isGameOver();
    boolean isGameWon();
    String getTargetNumber();
    StringBuilder getCurrentGuess();
    int getRemainingAttempts();
    void startNewGame();
    boolean isMean(String currentGuess);

    Color[] getColors(String targetNumber, String currentGuess);


//    string getColors();
}