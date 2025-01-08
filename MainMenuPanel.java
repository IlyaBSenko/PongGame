import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // add best to three below the title 
        JLabel bestToThreeLabel = new JLabel("Best to Three!");
        bestToThreeLabel.setFont(new Font("Consolas", Font.PLAIN, 24));
        bestToThreeLabel.setForeground(Color.WHITE);
        gbc.gridy = 1; 
        this.add(bestToThreeLabel, gbc);

        // Single Player Button
        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setFont(new Font("Consolas", Font.PLAIN, 24));
        gbc.gridy = 2;
        this.add(singlePlayerButton, gbc);

        // Multiplayer Button
        JButton multiplayerButton = new JButton("Multiplayer");
        multiplayerButton.setFont(new Font("Consolas", Font.PLAIN, 24));
        gbc.gridy = 3;
        this.add(multiplayerButton, gbc);

        // Action Listeners for Buttons
        singlePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel gamePanel = (GamePanel) container.getComponent(1); 
                gamePanel.setGameMode(0); 
                gamePanel.startGameThread();
                cardLayout.show(container, "Game");

                gamePanel.requestFocusInWindow();
            }
        });

        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel gamePanel = (GamePanel) container.getComponent(1); 
                gamePanel.setGameMode(1); 
                gamePanel.startGameThread();
                cardLayout.show(container, "Game");

                gamePanel.requestFocusInWindow();
            }
        });
    }
}
