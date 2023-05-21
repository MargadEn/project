package boardgame;

import java.time.LocalDateTime;

public class GameStatistics {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    public int player1count;
    public int player2count;
    public String winner; // Add the "winner" field

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getPlayer1count() {
        return player1count;
    }

    public void setPlayer1count(int player1count) {
        this.player1count = player1count;
    }

    public int getPlayer2count() {
        return player2count;
    }

    public void setPlayer2count(int player2count) {
        this.player2count = player2count;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
