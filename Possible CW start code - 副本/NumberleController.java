import java.awt.*;

// NumberleController.java
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    public void setView(NumberleView view) {
        this.view = view;
    }

    public void processInput(String input) {
        model.processInput(input);
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }


    public boolean isMean(String currentGuess){
        return model.isMean(currentGuess);
    }

    public boolean isGameWon() {
        return model.isGameWon();
    }

    public String getTargetWord() {
        return model.getTargetNumber();
    }

    public StringBuilder getCurrentGuess() {
        return model.getCurrentGuess();
    }

    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }
    public Color[] getColors(String targetNumber, String currentGuess) {
        return model.getColors(targetNumber,currentGuess);
    }
    public void startNewGame() {
        model.startNewGame();
    }
}