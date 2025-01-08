import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class Pong {
    public static void main(String[] args) {
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
    }

    

    
}
