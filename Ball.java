import java.awt.Rectangle;

public class Ball {
    private int x, y;
    private int diameter;
    private int speedX, speedY;

    private int panelWidth, panelHeight;

    public Ball(int x, int y, int diameter, int speedX, int speedY, int panelWidth, int panelHeight) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.speedX = speedX;
        this.speedY = speedY;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }


    public void updatePos() {
        x += speedX;
        y += speedY;

        if (y <= 0) {
            y = 0;
            speedY = -speedY;
        } else if (y + diameter >= panelHeight) {
            y = panelHeight - diameter;
            speedY = -speedY;
        }
    }

    public void reset(int newX, int newY, int newSpeedX, int newSpeedY) {
        this.x = newX;
        this.y = newY;
        this.speedX = newSpeedX;
        this.speedY = newSpeedY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
}
