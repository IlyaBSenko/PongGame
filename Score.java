public class Score {
    private int player1score;
    private int player2score;

    public Score() {
        this.player1score = 0;
        this.player2score = 0;
    }

    public void incrementLeft() {
        player1score++;
    }

    public void incrementRight() {
        player2score++;
    }

    public int getLeftScore() {
        return player1score;
    }

    public int getRightScore() {
        return player2score;
    }

    
    
}
