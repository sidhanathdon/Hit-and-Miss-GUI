import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameGUI {
    private Game game;
    private JFrame frame;
    private JPanel gridPanel;
    private JLabel statusLabel;
    private JButton[][] buttons;
    private JButton restartButton;
    private static final int BUTTON_FONT_SIZE = 20; // Larger font size for button text
    private static final int STATUS_FONT_SIZE = 24; // Larger font size for status text
    private static final int RESTART_BUTTON_WIDTH = 150; // Width of restart button
    private static final int RESTART_BUTTON_HEIGHT = 50; // Height of restart button
    private static final int DELAY_MS = 2000; // Delay in milliseconds

    public GameGUI() {
        initUI();
        startNewGame();
    }

    private void initUI() {
        frame = new JFrame("Target Game");
        frame.setSize(500, 500); // Increase frame size for better visibility
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        int size = Game.SIZE;
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(size, size));
        buttons = new JButton[size][size];

        Font buttonFont = new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE);
        Font statusFont = new Font("Arial", Font.BOLD, STATUS_FONT_SIZE);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(80, 80)); // Increase button size
                buttons[i][j].setFont(buttonFont); // Set the font for the button
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                gridPanel.add(buttons[i][j]);
            }
        }

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(statusFont); // Set font for status label
        frame.add(statusLabel, BorderLayout.NORTH);

        restartButton = new JButton("Restart");
        restartButton.setPreferredSize(new Dimension(RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT)); // Increase size of restart button
        restartButton.setEnabled(false); // Initially disabled
        restartButton.addActionListener(e -> startNewGame());
        frame.add(restartButton, BorderLayout.SOUTH);

        frame.add(gridPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void startNewGame() {
        game = new Game(); // Initialize a new game
        updateButtons(); // Reset button display
        statusLabel.setText(game.getGameStatus()); // Update status label
        restartButton.setEnabled(false); // Disable restart button until game ends
    }

    private void updateButtons() {
        int size = Game.SIZE;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setBackground(null); // Reset background color
                buttons[i][j].setText(""); // Clear button text
            }
        }
    }

    private void updateButton(int x, int y, boolean isHit) {
        JButton button = buttons[x][y];
        if (isHit) {
            button.setBackground(Color.RED);
            button.setText("Hit");
        } else {
            button.setBackground(Color.BLUE);
            button.setText("Miss");
        }
    }

    private void showFinalTargets() {
        List<Target> allTargets = game.getAllTargets();
        for (Target target : allTargets) {
            int x = target.getX();
            int y = target.getY();
            JButton button = buttons[x][y];
            button.setBackground(Color.GREEN);
            button.setText("Target");
        }
    }

    private void delayedShowFinalTargets() {
        // Create a timer to delay the execution of showing final targets
        Timer timer = new Timer(DELAY_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFinalTargets(); // Update grid with target positions
            }
        });
        timer.setRepeats(false); // Timer should run only once
        timer.start();
    }

    private class ButtonClickListener implements ActionListener {
        private int x;
        private int y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.isGameOver()) {
                statusLabel.setText("Game Over!");
                delayedShowFinalTargets(); // Delay showing final targets
                restartButton.setEnabled(true); // Enable restart button
                return;
            }

            boolean hit = game.makeMove(x, y);
            updateButton(x, y, hit);
            game.incrementTurns();
            statusLabel.setText(game.getGameStatus());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}