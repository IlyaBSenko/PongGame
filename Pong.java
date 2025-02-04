import javax.swing.*;
import java.awt.CardLayout;

public class Pong {
    public static void main(String[] args) {
        // Ensure Swing components are created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Pong");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            MainMenuPanel menuPanel = new MainMenuPanel(container, cardLayout);
            GamePanel gamePanel = new GamePanel();

            container.add(menuPanel, "MainMenu");
            container.add(gamePanel, "Game");

            window.add(container);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
