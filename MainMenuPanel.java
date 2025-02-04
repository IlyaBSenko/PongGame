import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(JPanel container, CardLayout cardLayout) {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Pong");
        title.setFont(new Font("Consolas", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(title, gbc);

        JLabel bestToThreeLabel = new JLabel("Best to Three!");
        bestToThreeLabel.setFont(new Font("Consolas", Font.PLAIN, 24));
        bestToThreeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        this.add(bestToThreeLabel, gbc);

        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setFont(new Font("Consolas", Font.PLAIN, 24));
        gbc.gridy = 2;
        this.add(singlePlayerButton, gbc);

        JButton multiplayerButton = new JButton("Multiplayer");
        multiplayerButton.setFont(new Font("Consolas", Font.PLAIN, 24));
        gbc.gridy = 3;
        this.add(multiplayerButton, gbc);

        // When a button is clicked, switch the game mode and start the game.
        singlePlayerButton.addActionListener(e -> {
            GamePanel gamePanel = (GamePanel) container.getComponent(1);
            gamePanel.setGameMode(0);  // 0 indicates single-player (AI)
            gamePanel.startGame();
            cardLayout.show(container, "Game");
            gamePanel.requestFocusInWindow();
        });

        multiplayerButton.addActionListener(e -> {
            GamePanel gamePanel = (GamePanel) container.getComponent(1);
            gamePanel.setGameMode(1);  // 1 indicates multiplayer
            gamePanel.startGame();
            cardLayout.show(container, "Game");
            gamePanel.requestFocusInWindow();
        });
    }
}
