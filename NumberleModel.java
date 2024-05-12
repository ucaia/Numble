import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NumberleModel extends Observable implements INumberleModel {
    private String targetNumber;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;

    private boolean TestModel;
    private boolean SetModel;
    private boolean ResultModel;
    private ArrayList<String> lines = new ArrayList<>();


    @Override
    public void initialize() {
        if (lines.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader("equations.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                return;
            }
        }
        if (!lines.isEmpty()) {
            Random random = new Random();
            targetNumber = lines.get(random.nextInt(lines.size()));
            /*System.out.println("Selected equation: " + targetNumber);*/
         /*   for (int remainingAttempts = 6; remainingAttempts > 0; remainingAttempts--) {
                System.out.println("Selected equation: " + targetNumber);
                System.out.println("remainingAttempts： " + remainingAttempts);
        }*/
        }
        if (SetModel) {
            targetNumber = lines.get(99);
        }
        currentGuess = new StringBuilder(" ");
        remainingAttempts = MAX_ATTEMPTS;
        if (ResultModel) {
            System.out.println("Target:" + targetNumber);
        }
        gameWon = false;
        setChanged();
        notifyObservers();
    }


    @Override
    public boolean processInput(String input) {

        if (!input.contains("=")) {
            System.out.println("No equal '=' sign");
            if (!TestModel) remainingAttempts--;
            return false;
        }
        if (!input.contains("+") && !input.contains("-") && !input.contains("*") && !input.contains("/")) {
            System.out.println("There must be at least one sign +-*/");
            if (!TestModel) remainingAttempts--;
            return false;
        }
        currentGuess = new StringBuilder(input);
        if (isEquation()) {
            remainingAttempts--;
            if (currentGuess.toString().equals(targetNumber)) {
                gameWon = true;
            }
            setChanged();
            notifyObservers();
            return true;
        }
        System.out.println("The left side is not equal to the right");
        return false;
    }


    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public String getTargetNumber() {
        return targetNumber;
    }

    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    @Override
    public void startNewGame() {
        initialize();
    }


    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private void processOperation(Stack<Integer> operandStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();
        int operand2 = operandStack.pop();
        int operand1 = operandStack.pop();

        int result = 0;
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                result = operand1 / operand2;
                break;
        }

        operandStack.push(result);
    }

    private int evaluateExpression(String expression) {
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') {
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (expression.charAt(i) >= '0' && expression.charAt(i) <= '9')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;

                operandStack.push(Integer.parseInt(sb.toString()));

            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operatorStack.isEmpty() && hasPrecedence(ch, operatorStack.peek())) {
                    processOperation(operandStack, operatorStack);
                }
                operatorStack.push(ch);
            }
        }

        while (!operatorStack.isEmpty()) {
            processOperation(operandStack, operatorStack);
        }
        return operandStack.pop();
    }

    private boolean isEquation() {
        return
                evaluateExpression(currentGuess.toString().split("=")[0])
                        ==
                        evaluateExpression(currentGuess.toString().split("=")[1]);
    }

    // model set
    private void setTestModel(boolean testpattern) {
        TestModel = testpattern;
    }

    private void setSetModel(boolean setpattern) {
        SetModel = setpattern;
    }

    private void setShowResultModel(boolean ResultModel) {
        ResultModel = ResultModel;
    }

    public void Settings(String entrance) {
        if ("CLI".equals(entrance)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Test model (y/n)?:");
            if ("y".equals(scanner.next())) {
                setTestModel(true);
            }else {
                setTestModel(false);
            }
            System.out.print("Set model (y/n)?:");
            if ("y".equals(scanner.next())) {
                setSetModel(true);
            }else {
                setSetModel(false);
            }
            System.out.print("show Result model (y/n)?:");
            if ("y".equals(scanner.next())) {
                setShowResultModel(true);
            }else {
                setShowResultModel(false);
            }
        }
    }

    public void Settings(String entrance, boolean[] result) {
        if ("GUI".equals(entrance)) {
            setTestModel(result[0]);
            setSetModel(result[1]);
            setShowResultModel(result[2]);
        }
    }



/*
    @Override
    public Map<Integer, Integer> getResult() {
        return null;
    }
*/

    @Override
    public int[] Result() {
        int[] result = new int[currentGuess.length()];
        for (int i = 0; i < currentGuess.length(); i++) {
            if (targetNumber.charAt(i) == (currentGuess.charAt(i))) {
                result[i] = 1; // correct position and character
                continue;
            }
            if (targetNumber.contains(String.valueOf(currentGuess.charAt(i)))) {
                result[i] = 2;//incorrect
            }
        }
        return result;
    }

/*    public Map<Integer, Integer> getResult() {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < currentGuess.length(); i++) {
            if (targetNumber.charAt(i) == currentGuess.charAt(i)) {
                result.put(i, 1); // 代表位置与字符正确
                continue;
            }
            if (targetNumber.contains(String.valueOf(currentGuess.charAt(i)))) {
                result.put(i, 2); // 代表字符存在但位置不对
            }
        }
        return result;
    }*/
}
