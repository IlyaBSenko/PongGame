public class Score {
    private int leftScore;
    private int rightScore;

    public Score() {
        leftScore = 0;
        rightScore = 0;
    }

    public void incrementLeft() {
        leftScore++;
    }

    public void incrementRight() {
        rightScore++;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public void reset() {
        leftScore = 0;
        rightScore = 0;
    }
}
