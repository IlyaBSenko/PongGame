import java.awt.Rectangle;

public class Paddle {
    private int x, y;
    private int width, height;
    private int speed;
    private int panelHeight;

    public Paddle(int x, int y, int width, int height, int speed, int panelHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.panelHeight = panelHeight;
    }

    public void moveUp() {
        y = Math.max(0, y - speed);
    }

    public void moveDown() {
        y = Math.min(panelHeight - height, y + speed);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
}
