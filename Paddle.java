import java.awt.Rectangle;

public class Paddle {
    
    // Need position, speed, dimensions, movement method

    private int x, y;
    private int width, height;   // the dimensions
    private int speed;           // the speed
    private int panelHeight;     // for boundary checking

    public Paddle(int x, int y, int width, int height, int speed, int panelHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.panelHeight = panelHeight;
    }

    public void moveUp() {
        if (y - speed >= 0) {
            y -= speed;
        } else {
            y = 0;
        }
        
    }

    public void moveDown() {
        if (y + height + speed <= panelHeight) {
            y += speed;
        } else {
            y = panelHeight - height;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
