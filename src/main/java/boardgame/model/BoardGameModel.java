package boardgame.model;

import boardgame.GameStatistics;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardGameModel {
    private File file = new File("game_statistics.json");
    private ObjectMapper mapper = new ObjectMapper();
    GameStatistics statistics = new GameStatistics();
    private static final int BOARD_SIZE = 5;
    private static final int WINNING_CONDITION = 3;

    private boolean gameEnded;

    private Square[][] board;
    private ObjectProperty<Square>[][] squareProperties;
    private ObjectProperty<Square> currentPlayer;

    public BoardGameModel() {
        mapper.registerModule(new JavaTimeModule());
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        squareProperties = new ObjectProperty[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = new SimpleObjectProperty<>(Square.RED);
        initializeBoard();
        initializeSquareProperties();
    }

    private void writeToJson(GameStatistics statistics){
        List<GameStatistics> statisticsList = new ArrayList<>();
        if(file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                statisticsList = mapper.readValue(reader, new TypeReference<ArrayList<GameStatistics>>(){});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            statisticsList.add(statistics);
            mapper.writeValue(writer,statisticsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Square.NONE;
            }
        }
        statistics.setStartDate(LocalDateTime.now());
    }

    @SuppressWarnings("unchecked")
    private void initializeSquareProperties() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                squareProperties[i][j] = new SimpleObjectProperty<>(Square.NONE);
            }
        }
    }


    public void makeMove(int row, int col) {
        if (!gameEnded && isValidMove(row, col)) {
            board[row][col] = currentPlayer.get();
            squareProperties[row][col].set(board[row][col]);
            if (currentPlayer.get() == Square.RED) {
                statistics.player1count++;
            } else {
                statistics.player2count++;
            }
            if (checkWin()) {
                System.out.println("Player " + currentPlayer.get() + " wins!");
                gameEnded = true;
                statistics.setEndDate(LocalDateTime.now());
            } else if (checkDraw()) {
                System.out.println("It's a draw!");
                gameEnded = true;
                statistics.setEndDate(LocalDateTime.now());
            } else {
                currentPlayer.set(currentPlayer.get() == Square.RED ? Square.BLUE : Square.RED);
            }
            if (gameEnded) {
                writeToJson(statistics);
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == Square.NONE;
    }

    public boolean checkWin() {
        return checkRows() || checkColumns() || checkDiagonals();
    }

    private boolean checkRows() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            int consecutiveCount = 0;
            Square currentSquare = null;

            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != Square.NONE && board[i][j] == currentSquare) {
                    consecutiveCount++;
                } else {
                    currentSquare = board[i][j];
                    consecutiveCount = 1;
                }

                if (consecutiveCount == WINNING_CONDITION) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkColumns() {
        for (int j = 0; j < BOARD_SIZE; j++) {
            int consecutiveCount = 0;
            Square currentSquare = null;

            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board[i][j] != Square.NONE && board[i][j] == currentSquare) {
                    consecutiveCount++;
                } else {
                    currentSquare = board[i][j];
                    consecutiveCount = 1;
                }

                if (consecutiveCount == WINNING_CONDITION) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkDiagonals() {
        // Check all diagonals
        for (int i = 0; i <= BOARD_SIZE - WINNING_CONDITION; i++) {
            for (int j = 0; j <= BOARD_SIZE - WINNING_CONDITION; j++) {
                if (checkDiagonal(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkDiagonal(int startRow, int startCol) {
        int consecutiveCount = 0;
        Square currentSquare = null;

        // Check diagonal from top-left to bottom-right
        for (int k = 0; k < WINNING_CONDITION; k++) {
            int row = startRow + k;
            int col = startCol + k;

            if (board[row][col] != Square.NONE && board[row][col] == currentSquare) {
                consecutiveCount++;
            } else {
                currentSquare = board[row][col];
                consecutiveCount = 1;
            }

            if (consecutiveCount == WINNING_CONDITION) {
                return true;
            }
        }

        consecutiveCount = 0;
        currentSquare = null;

        // Check diagonal from top-right to bottom-left
        for (int k = 0; k < WINNING_CONDITION; k++) {
            int row = startRow + k;
            int col = startCol + WINNING_CONDITION - 1 - k;

            if (board[row][col] != Square.NONE && board[row][col] == currentSquare) {
                consecutiveCount++;
            } else {
                currentSquare = board[row][col];
                consecutiveCount = 1;
            }

            if (consecutiveCount == WINNING_CONDITION) {
                return true;
            }
        }

        return false;
    }

    public boolean checkDraw() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Square.NONE) {
                    return false;
                }
            }
        }
        return true;
    }

    public ObjectProperty<Square> squareProperty(int row, int col) {
        return squareProperties[row][col];
    }

    public Square getCurrentPlayer() {
        return currentPlayer.get();
    }
}
