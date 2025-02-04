import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class GamePanel extends JPanel {
    // Panel and game constants
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;
    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;

    // Game objects
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Ball ball;
    private Score score;

    // 0 for single-player (AI), 1 for multiplayer
    private int gameMode = 1;
    private int bounceCount = 0;
    private Timer gameTimer;
    private Random random;

    // Key flags for paddle movement
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    // Game state management
    private enum GameState { PLAYING, GAME_OVER }
    private GameState gameState = GameState.PLAYING;

    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        random = new Random();
        initGameObjects();
        setupKeyBindings();
    }

    // Initialize or reset game objects
    private void initGameObjects() {
        leftPaddle = new Paddle(50, PANEL_HEIGHT / 2 - 40, 10, 80, 5, PANEL_HEIGHT);
        rightPaddle = new Paddle(PANEL_WIDTH - 60, PANEL_HEIGHT / 2 - 40, 10, 80, 5, PANEL_HEIGHT);
        ball = new Ball(PANEL_WIDTH / 2 - 8, PANEL_HEIGHT / 2 - 8, 16, 4, 3, PANEL_WIDTH, PANEL_HEIGHT);
        score = new Score();
        bounceCount = 0;
        gameState = GameState.PLAYING;
    }

    // Called from the main menu to start (or restart) the game.
    public void startGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        initGameObjects();
        setupKeyBindings();

        gameTimer = new Timer(DELAY, e -> {
            updateGame();
            repaint();
        });
        gameTimer.start();
    }

    // Update game logic: movement, collisions, scoring.
    private void updateGame() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        // Update left paddle from key input
        if (wPressed) leftPaddle.moveUp();
        if (sPressed) leftPaddle.moveDown();

        // Update right paddle: either from key input or AI
        if (gameMode == 1) { // Multiplayer
            if (upPressed) rightPaddle.moveUp();
            if (downPressed) rightPaddle.moveDown();
        } else { // Single-player AI
            moveAiPaddle();
        }

        ball.updatePos();
        checkCollisions();
        checkScoring();
    }

    // Simple AI: move the right paddle toward the ball.
    private void moveAiPaddle() {
        int paddleCenter = rightPaddle.getY() + rightPaddle.getHeight() / 2;
        int ballCenter = ball.getY() + ball.getDiameter() / 2;
        if (ballCenter < paddleCenter) {
            rightPaddle.moveUp();
        } else if (ballCenter > paddleCenter) {
            rightPaddle.moveDown();
        }
    }

    // Detect collisions between the ball and paddles, then adjust speed and angle.
    private void checkCollisions() {
        Rectangle ballBounds = ball.getBounds();
        Rectangle leftBounds = leftPaddle.getBounds();
        Rectangle rightBounds = rightPaddle.getBounds();

        if (ballBounds.intersects(leftBounds)) {
            ball.setSpeedX(Math.abs(ball.getSpeedX()));
            adjustBallAngle(leftPaddle);
            bounceCount++;
            increaseSpeedOnBounce();
        }

        if (ballBounds.intersects(rightBounds)) {
            ball.setSpeedX(-Math.abs(ball.getSpeedX()));
            adjustBallAngle(rightPaddle);
            bounceCount++;
            increaseSpeedOnBounce();
        }
    }

    // Adjust ball’s vertical speed based on where it hit the paddle.
    private void adjustBallAngle(Paddle paddle) {
        int paddleCenter = paddle.getY() + paddle.getHeight() / 2;
        int ballCenter = ball.getY() + ball.getDiameter() / 2;
        int offset = ballCenter - paddleCenter;
        ball.setSpeedY(ball.getSpeedY() + offset / 10);
    }

    // Increase the ball’s speed every 5 bounces.
    private void increaseSpeedOnBounce() {
        if (bounceCount % 5 == 0) {
            int currentSpeedX = ball.getSpeedX();
            int currentSpeedY = ball.getSpeedY();
            double factor = 1.2;

            int newSpeedX = (int) Math.round(currentSpeedX * factor);
            int newSpeedY = (int) Math.round(currentSpeedY * factor);

            // Prevent zero speed
            if (newSpeedX == 0) newSpeedX = (currentSpeedX > 0) ? 1 : -1;
            if (newSpeedY == 0) newSpeedY = (currentSpeedY > 0) ? 1 : -1;

            ball.setSpeedX(newSpeedX);
            ball.setSpeedY(newSpeedY);
        }
    }

    // Check if the ball has gone off screen and update the score accordingly.
    private void checkScoring() {
        // Ball went off the left side
        if (ball.getX() < 0) {
            score.incrementRight();
            endRound("Right Player Wins the Round!");
            if (score.getRightScore() >= 3) {
                endGame("Right Player Wins the Game!");
            }
        }
        // Ball went off the right side
        else if (ball.getX() + ball.getDiameter() > PANEL_WIDTH) {
            score.incrementLeft();
            endRound("Left Player Wins the Round!");
            if (score.getLeftScore() >= 3) {
                endGame("Left Player Wins the Game!");
            }
        }
    }

    // Reset the ball for the next round.
    private void endRound(String message) {
        System.out.println(message);
        bounceCount = 0;
        int initialSpeedX = (ball.getX() < 0) ? 4 : -4;
        ball.reset(PANEL_WIDTH / 2 - 8, PANEL_HEIGHT / 2 - 8, initialSpeedX, 3);
    }

    // End the game and show a game over message.
    private void endGame(String message) {
        gameState = GameState.GAME_OVER;
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        score.reset();
        // Optionally, you could signal a return to the main menu here.
    }

    // Render the game objects.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw center dividing line
        g.setColor(Color.WHITE);
        g.drawLine(PANEL_WIDTH / 2, 0, PANEL_WIDTH / 2, PANEL_HEIGHT);

        // Draw paddles
        g.fillRect(leftPaddle.getX(), leftPaddle.getY(), leftPaddle.getWidth(), leftPaddle.getHeight());
        g.fillRect(rightPaddle.getX(), rightPaddle.getY(), rightPaddle.getWidth(), rightPaddle.getHeight());

        // Draw ball
        g.fillOval(ball.getX(), ball.getY(), ball.getDiameter(), ball.getDiameter());

        // Draw scores
        g.setFont(new Font("Consolas", Font.BOLD, 36));
        String leftScoreStr = String.valueOf(score.getLeftScore());
        String rightScoreStr = String.valueOf(score.getRightScore());
        g.drawString(leftScoreStr, PANEL_WIDTH / 2 - 70, 50);
        g.drawString(rightScoreStr, PANEL_WIDTH / 2 + 30, 50);

        // Draw bounce count
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString("Bounce Count: " + bounceCount, 10, 20);
    }

    // Set up key bindings for paddle control.
    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Left paddle - W key
        im.put(KeyStroke.getKeyStroke("pressed W"), "wPressed");
        am.put("wPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wPressed = true;
            }
        });
        im.put(KeyStroke.getKeyStroke("released W"), "wReleased");
        am.put("wReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wPressed = false;
            }
        });

        // Left paddle - S key
        im.put(KeyStroke.getKeyStroke("pressed S"), "sPressed");
        am.put("sPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sPressed = true;
            }
        });
        im.put(KeyStroke.getKeyStroke("released S"), "sReleased");
        am.put("sReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sPressed = false;
            }
        });

        // Right paddle - UP key
        im.put(KeyStroke.getKeyStroke("pressed UP"), "upPressed");
        am.put("upPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = true;
            }
        });
        im.put(KeyStroke.getKeyStroke("released UP"), "upReleased");
        am.put("upReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });

        // Right paddle - DOWN key
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "downPressed");
        am.put("downPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = true;
            }
        });
        im.put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");
        am.put("downReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });
    }

    // Allow switching game modes (0 = single-player, 1 = multiplayer).
    public void setGameMode(int mode) {
        this.gameMode = mode;
    }
}
