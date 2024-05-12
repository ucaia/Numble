import java.util.Scanner;
public class CLIApp {
    private static INumberleModel model = new NumberleModel();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        do {
            StartNewGame();
            while (model.getRemainingAttempts() > 0) {
                System.out.print("input your guess:");
                model.processInput(scanner.next());
                if (model.isGameWon()) {
                    System.out.println("Won!!!!");
                    break;
                }
                int[] result = model.Result();
                for (int i = 0; i < result.length; i++) {
                    if (result[i] == 1) {
                        System.out.print(ColoredString(40, 4, String.valueOf(model.getCurrentGuess().charAt(i))));
                    } else if (result[i] == 2) {
                        System.out.print(ColoredString(41, 4, String.valueOf(model.getCurrentGuess().charAt(i))));
                    } else {
                        System.out.print(ColoredString(42, 4, String.valueOf(model.getCurrentGuess().charAt(i))));
                    }
                }
                System.out.println("\nThe number of tests you have left: " + model.getRemainingAttempts());
            }
            if (!model.isGameWon()) {
                System.out.println("Lose!!!");
            }
        } while (isContinue());
    }
    private static String ColoredString(int color, int fontType, String content) {
        return String.format("\033[%d;%dm%s\033[0m", color, fontType, content);
    }

    public static void StartNewGame() {
        model.Settings("CLI");
        model.startNewGame();
    }

    public static boolean isContinue() {
        System.out.print("enter n to continue: ");
        return "n".equals(scanner.next());
    }

}
