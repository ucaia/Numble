import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberleModelTest {

    private NumberleModel model;

    @Before
    public void setUp() {
        model = new NumberleModel();
        model.initialize();
    }

    @Test
    public void testInitialize() {
        assertNotNull("Target number should not be null after initialization", model.getTargetNumber());
        assertEquals("Should have 6 attempts after initialization", 6, model.getRemainingAttempts());
        assertFalse("Game should not be won at initialization", model.isGameWon());
    }



    @Test
    public void testProcessIncorrectInput() {
        String incorrectInput = "1 + 1 = 3"; // incorrect input
        assertFalse("Process input should return false for an incorrect guess", model.processInput(incorrectInput));
        assertEquals("Remaining attempts should decrease after an incorrect guess", 5, model.getRemainingAttempts());
    }

    @Test
    public void testGameOver() {
        for (int i = 0; i < 6; i++) {
            model.processInput("1 + 1 = 3"); // Assume this is always wrong
        }
        assertTrue("Game should be over after 6 incorrect attempts", model.isGameOver());
    }

    @Test
    public void testGetTargetNumber() {

        assertNotNull("Target number should not be null", model.getTargetNumber());
    }

    @Test
    public void testGetRemainingAttempts() {
        assertEquals("Should start with 6 attempts", 6, model.getRemainingAttempts());
        model.processInput("1 + 1 = 1"); // An incorrect input
        assertNotEquals("Remaining attempts should decrease after an incorrect input", 6, model.getRemainingAttempts());
    }
    @Test
    public void testStartNewGame() {
        model.processInput("1 + 1 = 1"); // An incorrect input
        model.startNewGame();
        assertEquals("Should reset attempts to 6 after a new game", 6, model.getRemainingAttempts());
        assertFalse("Game should not be won after starting a new game", model.isGameWon());
    }
}