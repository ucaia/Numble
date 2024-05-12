import java.util.List;
import java.util.Map;

public interface INumberleModel {
    int MAX_ATTEMPTS = 6;

    void initialize();

    boolean processInput(String input);

    boolean isGameOver();

    boolean isGameWon();

    String getTargetNumber();

    StringBuilder getCurrentGuess();

    int getRemainingAttempts();

    void startNewGame();

    int[] Result();

    void Settings(String entrance);

    void Settings(String entrance, boolean[] result);
}