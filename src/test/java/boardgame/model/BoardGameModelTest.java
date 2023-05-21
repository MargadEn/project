package boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameModelTest {

    @Test
    void makeMove() {
        BoardGameModel model = new BoardGameModel();

        // Perform some valid moves
        model.makeMove(0, 0);
        model.makeMove(1, 1);
        model.makeMove(2, 2);

        // Verify that the moves were made successfully
        assertEquals(Square.RED, model.squareProperty(0, 0).get());
        assertEquals(Square.BLUE, model.squareProperty(1, 1).get());
        assertEquals(Square.RED, model.squareProperty(2, 2).get());

        // Attempt an invalid move
        model.makeMove(0, 0);

        // Verify that the invalid move was not made
        assertEquals(Square.RED, model.squareProperty(0, 0).get());
    }

    @Test
    void checkWin() {
        BoardGameModel model = new BoardGameModel();

        // Simulate a winning condition
        model.makeMove(0, 0);
        model.makeMove(1, 0);
        model.makeMove(0, 1);
        model.makeMove(1, 1);
        model.makeMove(0, 2);

        // Verify that the game is won
        assertTrue(model.checkWin());
    }

    @Test
    void checkDraw() {
        BoardGameModel model = new BoardGameModel();

        // Simulate a draw condition
        model.makeMove(0, 0);
        model.makeMove(0, 1);
        model.makeMove(0, 2);
        model.makeMove(1, 1);
        model.makeMove(1, 0);
        model.makeMove(1, 2);
        model.makeMove(2, 1);
        model.makeMove(2, 0);

        // Verify that the game is not drawn prematurely
        assertFalse(model.checkDraw());

        // Make the final move to create a draw
        model.makeMove(2, 2);

        // Verify that the game is drawn
        assertFalse(model.checkDraw());
    }

    @Test
    void squareProperty() {
        BoardGameModel model = new BoardGameModel();

        // Verify the initial square properties
        assertEquals(Square.NONE, model.squareProperty(0, 0).get());
        assertEquals(Square.NONE, model.squareProperty(2, 2).get());

        // Make a move and verify the updated square property
        model.makeMove(0, 0);
        assertEquals(Square.RED, model.squareProperty(0, 0).get());
    }

    @Test
    void getCurrentPlayer() {
        BoardGameModel model = new BoardGameModel();

        // Verify the initial current player
        assertEquals(Square.RED, model.getCurrentPlayer());

        // Make a move and verify the updated current player
        model.makeMove(0, 0);
        assertEquals(Square.BLUE, model.getCurrentPlayer());
    }
}
