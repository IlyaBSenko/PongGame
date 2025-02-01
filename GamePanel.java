import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;

    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Ball ball;
    private Score score;
    private int gameMode = 1;

    private Thread gameThread;
    private boolean running = false;
    private final int FPS = 60;

    private boolean wPressed, sPressed;     // left paddle for player1
    private boolean upPressed, downPressed; // right paddle for player2
    private int bounceCount;                // count for when the ball should speed up

    private Random random;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        random = new Random();

        initGameObjects();
    }

    private void initGameObjects() {
        leftPaddle = new Paddle(50, PANEL_HEIGHT / 2 - 40, 10, 80, 5, PANEL_HEIGHT);
        rightPaddle = new Paddle(PANEL_WIDTH - 60, PANEL_HEIGHT / 2 - 40, 10, 80, 5, PANEL_HEIGHT);

        // start ball at center
        ball = new Ball(PANEL_WIDTH / 2 - 8, PANEL_HEIGHT / 2 - 8, 16, 4, 3, PANEL_WIDTH, PANEL_HEIGHT);

        score = new Score();
    }

    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long timePerFrame = 1000 / FPS;

        while (running) {
            long startTime = System.currentTimeMillis();

            updateGame();

            repaint();

            long elapsed = System.currentTimeMillis() - startTime;
        long wait = timePerFrame - elapsed;
        if (wait < 0) {
            wait = 0;
        }

        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }

    }

    private int powerUpTimer = 0;

    private void updateGame() {
        if (wPressed) {
            leftPaddle.moveUp();
        }
        if (sPressed) {
            leftPaddle.moveDown();
            
        }

        if (gameMode == 1) {
            if (upPressed) {
                rightPaddle.moveUp();
            }
            if (downPressed) {
                rightPaddle.moveDown();
            }
        
        } else {
            moveAiPaddle();
        }
        

        ball.updatePos();

        checkCollisions();

        checkScoring();


        if (wPressed) {
        leftPaddle.moveUp();
    }
    if (sPressed) {
        leftPaddle.moveDown();
    }
    if (gameMode == 1) {
        if (upPressed) {
            rightPaddle.moveUp();
        }
        if (downPressed) {
            rightPaddle.moveDown();
        }
    } else {
        moveAiPaddle();
    }

    ball.updatePos();
    checkCollisions();
    checkScoring();

    


    }

    private void moveAiPaddle() {
        int paddleCenter = rightPaddle.getY() + rightPaddle.getHeight() / 2;
        if (ball.getY() < paddleCenter && rightPaddle.getY() > 0) {
            rightPaddle.moveUp();
        } else if (ball.getY() > paddleCenter && rightPaddle.getY() + rightPaddle.getHeight() < PANEL_HEIGHT) {
            rightPaddle.moveDown();
        }
    }

    private void checkCollisions() {
        boolean leftCollision = ball.getBounds().intersects(leftPaddle.getBounds());
        boolean rightCollision = ball.getBounds().intersects(rightPaddle.getBounds());
        
        if (leftCollision) {
            ball.setSpeedX(Math.abs(ball.getSpeedX())); 
            bounceCount++; 
            System.out.println("Bounce Count: " + bounceCount); 
        }
        
        if (rightCollision) {
            ball.setSpeedX(-Math.abs(ball.getSpeedX())); 
            bounceCount++; 
            System.out.println("Bounce Count: " + bounceCount); 
        }
    }
    
    

    private Paddle increasePaddleLength(Paddle paddle) {
        int newHeight = (int) (paddle.getHeight() * 1.15); /
        return new Paddle(paddle.getX(), paddle.getY(), paddle.getWidth(), newHeight, paddle.getSpeed(), PANEL_HEIGHT);
    }
    
    

    private void increaseBallSpeed() {
        int currentSpeedX = ball.getSpeedX();
        int currentSpeedY = ball.getSpeedY();

        double factor = 1.5;

        double newSpeedXd = currentSpeedX * factor;
        double newSpeedYd = currentSpeedY * factor;

        int newSpeedX = (int) Math.round(newSpeedXd);
        int newSpeedY = (int) Math.round(newSpeedYd);

        if (newSpeedX == 0) {
            newSpeedX = currentSpeedX > 0 ? 1 : -1;
        }

        if (newSpeedY == 0) {
            newSpeedY = currentSpeedY > 0 ? 1 : -1;
        }

        ball.setSpeedX(newSpeedX);
        ball.setSpeedY(newSpeedY);
    }

    private void checkScoring() {
        if (ball.getX() < 0) {
            score.incrementRight(); 
            bounceCount = 0; 
            ball.reset(PANEL_WIDTH / 2 - 8, PANEL_HEIGHT / 2 - 8, 4, 3);
    
            if (score.getRightScore() == 3) {
                endGame("Right Player Wins!");
            }
        }
        else if (ball.getX() + ball.getDiameter() > PANEL_WIDTH) {
            score.incrementLeft(); 
            bounceCount = 0; 
            ball.reset(PANEL_WIDTH / 2 - 8, PANEL_HEIGHT / 2 - 8, -4, 3);
    
            if (score.getLeftScore() == 3) {
                endGame("Left Player Wins!");
            }
        }
    }
    
    

    private void endGame(String message) {
        running = false;
    
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    
        score = new Score(); 
        bounceCount = 0;    
        initGameObjects();   
    
        Container parent = this.getParent();
        if (parent instanceof JPanel) {
            CardLayout cardLayout = (CardLayout) parent.getLayout();
            cardLayout.show(parent, "MainMenu");
        }
    }

    // all the drawing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // center dividing line
        g.setColor(Color.WHITE);
        g.drawLine(PANEL_WIDTH / 2, 0, PANEL_WIDTH / 2, PANEL_HEIGHT);

        // left paddle
        g.fillRect(leftPaddle.getX(), leftPaddle.getY(),
                   leftPaddle.getWidth(), leftPaddle.getHeight());
        
        // right paddle
        g.fillRect(rightPaddle.getX(), rightPaddle.getY(),
                   rightPaddle.getWidth(), rightPaddle.getHeight());

        // ball
        g.fillOval(ball.getX(), ball.getY(), 
                   ball.getDiameter(), ball.getDiameter());

        // scores
        g.setFont(new Font("Consolas", Font.BOLD, 36));
        String leftScoreStr = String.valueOf(score.getLeftScore());
        String rightScoreStr = String.valueOf(score.getRightScore());
        
        // Position: near the center for each score
        g.drawString(leftScoreStr, PANEL_WIDTH / 2 - 50, 50);
        g.drawString(rightScoreStr, PANEL_WIDTH / 2 + 25, 50);

        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString("Bounce Count: " + bounceCount, 10, 20);


    }

    // KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_W) {
            wPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            sPressed = true;
        }

        if (key == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            wPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            sPressed = false;
        }

        if (key == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void setGameMode(int mode) {
        this.gameMode = mode;
    }

}
