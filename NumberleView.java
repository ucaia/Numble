import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;


public class NumberleView extends Component implements Observer {
    private final INumberleModel model;
    private final NumberleController controller;
    private static JFrame frame = new JFrame("Numberle");
    private JPanel[][] JLableJpanes = new JPanel[7][7];
    private JLabel[][] JLables = new JLabel[7][7];
    private StringBuilder inputMsg[] = new StringBuilder[8];
    private final String[] signalMsg = {"Back", "+", "-", "*", "/", "=", "Enter"};
    JLabel modelLabel;
    ArrayList<JButton> buttonList = new ArrayList<>();
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newItem;


    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        ((NumberleModel) this.model).addObserver(this);
        MainView();
        initializeFrame();
        this.controller.setView(this);
        /*createAndShowGUI();*/
        /*addButtonWithAction(frame);*/
        StartNewGame();
        update((NumberleModel) this.model, null);
    }

    public void initializeFrame() {
        inputMsg[controller.getRemainingAttempts()] = new StringBuilder();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        /*recursiveMethod(6,frame);*/
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.add(new JPanel());

        // display area
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 7));

        for (int i = 6; i > 0; i--) {
            for (int j = 0; j < 7; j++) {
                JLableJpanes[i][j] = new JPanel(new FlowLayout());
                JLableJpanes[i][j].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                JLables[i][j] = new JLabel();
                JLables[i][j].setOpaque(true);
                JLables[i][j].setBackground(Color.WHITE);
                JLables[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                JLables[i][j].setPreferredSize(new Dimension(70, 50));
                JLableJpanes[i][j].add(JLables[i][j]);
                inputPanel.add(JLableJpanes[i][j]);
            }
        }
        center.add(inputPanel);
        center.add(new JPanel());
        frame.add(center, BorderLayout.NORTH);

        JPanel modelPanel = new JPanel();
        modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.X_AXIS));
        modelPanel.add(new JPanel());
        JPanel Jmodel = new JPanel();
        Jmodel.setLayout(new GridLayout(1, 1));
        modelLabel = new JLabel("");
        Jmodel.add(modelLabel);
        modelPanel.add(Jmodel);
        frame.add(modelPanel, BorderLayout.CENTER);

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        keyboardPanel.add(new JPanel());
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(1, 10));
        keyboardPanel.add(numberPanel, BorderLayout.NORTH);


        // number button
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setEnabled(true);
            button.setBackground(Color.WHITE);
            button.addActionListener(e -> {
                if (inputMsg[controller.getRemainingAttempts()].length() < 7) {
                    JLables[controller.getRemainingAttempts()][inputMsg[controller.getRemainingAttempts()].length()].setText(button.getText());
                    inputMsg[controller.getRemainingAttempts()].append(button.getText());
                }
            });
            button.setPreferredSize(new Dimension(50, 20));
            numberPanel.add(button);
            buttonList.add(button);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 8));
        keyboardPanel.add(buttonPanel);

        /*JButton jtb = new JButton("Attempts remaining");
        jtb.setBounds(700, 500, 100, 50);
        AtomicInteger atomicInteger = new AtomicInteger(model.MAX_ATTEMPTS);//6
        jtb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRemainingAttempts(atomicInteger);
            }
        });
        frame.getContentPane().add(jtb);
        frame.setVisible(true);*/


        for (int i = 0; i < signalMsg.length; i++) {
            JButton button = new JButton(signalMsg[i]);
            button.setBackground(Color.WHITE);
            button.setEnabled(true);
            buttonList.add(button);
            // 监听按钮
            button.addActionListener(e -> {
                if (button.getText().equals("Back")) { // back
                    if (inputMsg[controller.getRemainingAttempts()].length() > 0) {
                        JLables[controller.getRemainingAttempts()][inputMsg[controller.getRemainingAttempts()].length() - 1].setText("");
                        inputMsg[controller.getRemainingAttempts()].deleteCharAt(inputMsg[controller.getRemainingAttempts()].length() - 1);
                    }
                } else if (button.getText().equals("ReStart")) {
                    int response = JOptionPane.showConfirmDialog(frame,
                            "Restart Game？",
                            "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        // restart game
                        StartNewGame();
                        JOptionPane.showMessageDialog(frame,
                                "Start New Game success!",
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (button.getText().equals("Enter")) {
                    // enter comfirm
                    if (inputMsg[controller.getRemainingAttempts()].length() >= 7) {
                        if (!inputMsg[controller.getRemainingAttempts()].toString().contains("=")) {
                            JOptionPane.showMessageDialog(null, "No equal '=' sign",
                                    "Prompt", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("No equal '=' sign");
                            return;
                        }
                        if (!inputMsg[controller.getRemainingAttempts()].toString().contains("+") && !inputMsg[controller.getRemainingAttempts()].toString().contains("-")
                                && !inputMsg[controller.getRemainingAttempts()].toString().contains("*") && !inputMsg[controller.getRemainingAttempts()].toString().contains("/")) {
                            JOptionPane.showMessageDialog(null,
                                    "There must be at least one sign +-",
                                    "Prompt", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("There must be at least one sign + - * /");
                            return;
                        }
                        if (!controller.processInput(inputMsg[controller.getRemainingAttempts()].toString())) {
                            JOptionPane.showMessageDialog(null,
                                    "The left side is not equal to the right",
                                    "Prompt", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        if (controller.isGameOver()) { // GameOver
                            if (model.isGameWon()) {
                                JOptionPane.showMessageDialog(null,
                                        "Won",
                                        "Won", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Lose",
                                        "Lose", JOptionPane.INFORMATION_MESSAGE);
                            }
                            StartNewGame();
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Attempts remaining：" + model.getRemainingAttempts());
                        inputMsg[controller.getRemainingAttempts()] = new StringBuilder(); // Initializes a new line

                    }
                } else {
                    // 正常按键
                    if (inputMsg[controller.getRemainingAttempts()].length() < 7) {
                        JLables[controller.getRemainingAttempts()][inputMsg[controller.getRemainingAttempts()].length()].setText(button.getText());
                        inputMsg[controller.getRemainingAttempts()].append(button.getText());
                    }
                }
            });

            button.setPreferredSize(new Dimension(50, 20));
            buttonPanel.add(button);
        }
        frame.add(keyboardPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    /*private static void createAndShowGUI() {
        remainingAttempts = 6;
        JPanel panel = new JPanel();
        textField = new JTextField(20);
        panel.add(textField);
        JLabel attemptsLabel = new JLabel("Remaining Attempts: " + remainingAttempts);
        panel.add(attemptsLabel);

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (remainingAttempts > 0) {
                        remainingAttempts--;
                        attemptsLabel.setText("Remaining Attempts: " + remainingAttempts);
                        showMessage();
                    } else {
                        JOptionPane.showMessageDialog(null, "No more attempts left!");
                    }
                }
            }
        });

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void showMessage() {
        if (remainingAttempts >= 0) {
            JOptionPane.showMessageDialog(null, "Remaining Attempts: " + remainingAttempts);
        }
    }*/
/*    private static void createAndShowGUI() {
        JPanel panel = new JPanel();
        textField = new JTextField(0);
        panel.add(textField);
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnter();
                updateAttemptsLabel();
            }
        });
        panel.add(enterButton);
        attemptsLabel = new JLabel("Remaining Attempts: " + remainingAttempts);
        panel.add(attemptsLabel);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void handleEnter() {
        if (remainingAttempts > 0) {
            remainingAttempts--;
            JOptionPane.showMessageDialog(null, "Remaining Attempts: " + remainingAttempts);
        } else {
            JOptionPane.showMessageDialog(null, "No more attempts left!");
        }
    }

    private static void updateAttemptsLabel() {
        attemptsLabel.setText("Remaining Attempts: " + remainingAttempts);
    }*/


    @Override
    public void update(java.util.Observable o, Object arg) {
        for (int n = 0; n < buttonList.size(); n++) {
            buttonList.get(n).setBackground(Color.WHITE);
        }
        if (controller.getRemainingAttempts() < 6) {
            int[] result = controller.verifiedResult();
            for (int j = 0; j < result.length; j++) {
                if (result[j] == 2) {
                    // exist but position incorrect
                    JLables[controller.getRemainingAttempts() + 1][j].setBackground(Color.ORANGE.darker());
                    for (int n = 0; n < buttonList.size(); n++) {
                        if (buttonList.get(n).getText() == JLables[controller.getRemainingAttempts() + 1][j].getText()) {
                            buttonList.get(n).setBackground(Color.ORANGE.darker());
                        }
                    }
                } else if (result[j] == 1) {
                    // represente correct position
                    JLables[controller.getRemainingAttempts() + 1][j].setBackground(Color.YELLOW);
                    for (int n = 0; n < buttonList.size(); n++) {
                        if (buttonList.get(n).getText() == JLables[controller.getRemainingAttempts() + 1][j].getText()) {
                            buttonList.get(n).setBackground(Color.YELLOW);
                        }
                    }
                } else {
                    //represente incorrect position
                    JLables[controller.getRemainingAttempts() + 1][j].setBackground(Color.GRAY);
                    for (int n = 0; n < buttonList.size(); n++) {
                        if (buttonList.get(n).getText() == JLables[controller.getRemainingAttempts() + 1][j].getText()) {
                            buttonList.get(n).setBackground(Color.GRAY.darker());
                        }
                    }
                }
            }
        }
    }

    private void StartNewGame() {
        boolean[] result = new boolean[3];
        StringBuilder sb = new StringBuilder("Model: ");
        for (int n = 0; n < buttonList.size(); n++) {
            buttonList.get(n).setBackground(Color.WHITE);
        }
    /*    for (int remainingAttempts = 6; remainingAttempts > 0; remainingAttempts--) {
            int response = JOptionPane.showConfirmDialog(frame,
                    "Attempts remaining"+ remainingAttempts ,
                    "Warning",
                    JOptionPane.YES_NO_OPTION);
        }*/


        int response = JOptionPane.showConfirmDialog(frame,
                "Enabled Set model？",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            result[1] = true;
            sb.append("Set model \t");
        } else {
            result[1] = false;
        }
        response = JOptionPane.showConfirmDialog(frame,
                "Enabled Test model？",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            result[0] = true;
            sb.append("Test model \t");
        } else {
            result[0] = false;
        }
        response = JOptionPane.showConfirmDialog(frame,
                "Enabled show Result model？",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            result[2] = true;
            sb.append("show Result model \t");
        } else {
            result[2] = false;
        }
        controller.Settings(result);
        controller.startNewGame();
        if (result[2]) {
            sb.append("\n result: " + controller.getTargetWord() + " \t");
        }
        modelLabel.setText(sb.toString());
        for (int i = 6; i > 0; i--) {
            for (int j = 0; j < 7; j++) {
                JLables[i][j].setText("");
                JLables[i][j].setBackground(Color.WHITE);
                inputMsg[controller.getRemainingAttempts()] = new StringBuilder();
            }
        }
    }

    /*
        private void addButtonWithAction(JFrame frame) {
            JButton button = new JButton("ENTER");

            int[] remainingAttempts = {6};
            StringBuilder[] inputMsg = new StringBuilder[6];

            // Initialize inputMsg arrays
            for (int i = 0; i < 6; i++) {
                inputMsg[i] = new StringBuilder();
            }

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button.getText().equals("ENTER")) {
                        // 确认
                        if (inputMsg[remainingAttempts[0]].length() <= 6) {
                            if (!inputMsg[remainingAttempts[0]].toString().contains("=")) {
                                JOptionPane.showMessageDialog(null, "No equal '=' sign",
                                        "Prompt", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("No equal '=' sign");
                                return;
                            }
                            if (!inputMsg[remainingAttempts[0]].toString().contains("+") && !inputMsg[remainingAttempts[0]].toString().contains("-")
                                    && !inputMsg[remainingAttempts[0]].toString().contains("*") && !inputMsg[remainingAttempts[0]].toString().contains("/")) {
                                JOptionPane.showMessageDialog(null,
                                        "There must be at least one sign + - * /",
                                        "Prompt", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("There must be at least one sign + - * /");
                                return;
                            }
                            // Process input
                            if (!controller.processInput(inputMsg[remainingAttempts[0]].toString())) {
                                JOptionPane.showMessageDialog(null,
                                        "The left side is not equal to the right",
                                        "Prompt", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }

                            // Check if game is over
                            if (controller.isGameOver()) {
                                if (model.isGameWon()) {
                                    JOptionPane.showMessageDialog(null,
                                            "Won",
                                            "Won", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Lose",
                                            "Lose", JOptionPane.INFORMATION_MESSAGE);
                                }
                                StartNewGame();
                                return;
                            }
                            inputMsg[remainingAttempts[0]] = new StringBuilder(); // Initialize new row
                        }
                    } else {
                        // 正常按键
                        if (inputMsg[remainingAttempts[0]].length() < 7) {
                            inputMsg[remainingAttempts[0]].append(button.getText());
                        }
                    }
                }
            });

            // 添加按钮到 JFrame 的内容面板中
            frame.getContentPane().add(button);
        }*/
    /*public void MainView() {
        // 初始化菜单栏
        menuBar = new JMenuBar();

        // 初始化选项菜单
        optionsMenu = new JMenu("Options");

        // 初始化菜单项并添加事件监听器
        setModeMenuItem = new JMenuItem("Set Mode");
        setModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableSetMode();
            }
        });
        optionsMenu.add(setModeMenuItem);

        testModeMenuItem = new JMenuItem("Test Mode");
        testModeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableTestMode();
            }
        });
        optionsMenu.add(testModeMenuItem);

        showResultMenuItem = new JMenuItem("Show Result");
        showResultMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableShowResultMode();
            }
        });
        optionsMenu.add(showResultMenuItem);

        // 将选项菜单添加到菜单栏
        menuBar.add(optionsMenu);

        // 将菜单栏设置到主窗口
        frame.setJMenuBar(menuBar);

        // 设置其他界面元素...
    }

    private void enableSetMode() {
        int response = JOptionPane.showConfirmDialog(this,
                "Enable Set mode?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // 设置模式的逻辑...
        }
    }

    private void enableTestMode() {
        int response = JOptionPane.showConfirmDialog(this,
                "Enable Test mode?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // 测试模式的逻辑...
        }
    }

    private void enableShowResultMode() {
        int response = JOptionPane.showConfirmDialog(this,
                "Enable Show Result mode?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // 显示结果模式的逻辑...
        }
    }

    // 其他方法...*/
    public void MainView() {
        StringBuilder sb = new StringBuilder("Model: ");
        // Set  basic properties of  window
        frame.setTitle("Numberle");
        setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize menu bar
        menuBar = new JMenuBar();

        // 初始化文件菜单
        fileMenu = new JMenu("Options");
        // 初Initialize fileMenu
        newItem = new JMenuItem("Restart");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(frame,
                        "Restart Game？",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // restart
                    StartNewGame(); //  StartNewGame() method
                    JOptionPane.showMessageDialog(frame,
                            "Start New Game success!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        fileMenu.add(restartItem);

/*        JMenuItem testItem = new JMenuItem("Test");
        testItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Enable Test mode?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // 执行测试模式的逻辑，例如：
                    // testModeEnabled = true;
                    // 更新界面或执行其他操作
                    JOptionPane.showMessageDialog(null,
                            "Test mode enabled!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    targetNumber = "2+3+1=6";
                    sb.append(targetNumber);
                }
            }
        });
        fileMenu.add(testItem);
        testItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Enable Test mode?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // 更新 StringBuilder 的内容
                    sb.setLength(0); // 清空StringBuilder
                    sb.append(targetNumber);

                    // 显示 StringBuilder 的内容
                    JOptionPane.showMessageDialog(null,
                            sb.toString(),
                            "Test mode enabled!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 将 JMenuItem 添加到菜单中
        fileMenu.add(testItem);*/


        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    //  exit and close the application
                    System.exit(0);
                }
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        // Set the menu bar to the main window
        frame.setJMenuBar(menuBar);
    }


    /*public static void getRemainingAttempts(AtomicInteger count) {
        int newcount = count.get();
        if (newcount == 0) {
            System.out.println("No attempts");
            return;
        } else {
            count.decrementAndGet();//count--
            System.out.println("Attempts remaining：" + count);
            JOptionPane.showMessageDialog(null, "Attempts remaining：" + count);
        }
    }
}*/
}









