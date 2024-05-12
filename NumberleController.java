public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    public void setView(NumberleView view) {
        this.view = view;
    }

    public boolean processInput(String input) {
        return model.processInput(input);
    }

    public boolean isGameOver() {
        return model.isGameOver();
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

    public void startNewGame() {
        model.startNewGame();
    }

    public int[] verifiedResult() {
        return model.Result();
    }

    public void Settings(boolean[] result) {
        model.Settings("GUI", result);
    }

/*    public Map<JButton, Color> getVerifiedResult() {
        Map<JButton, Color> result = new HashMap<>();
        result.put(new JButton(), Color.ORANGE);
        result.put(new JButton(), Color.YELLOW);
        result.put(new JButton(), Color.GRAY);
        return result;
    }*/
}
