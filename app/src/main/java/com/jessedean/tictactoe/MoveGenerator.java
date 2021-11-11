package com.jessedean.tictactoe;

public class MoveGenerator {

    int [] move;
    boolean smartMoves;

    //Constructor
    public MoveGenerator(boolean smart) {
        move = new int[2];
        smartMoves = smart;
    }

    //Calls the appropriate move generation method
    public int[] generateMove(GameBoard board, int id) {
        if(smartMoves)
            move = generateOptimalMove(board, id);
        else
            move = generateRandomMove(board);
        return move;
    }

    //Generates a random valid move
    private int[] generateRandomMove(GameBoard board) {

        int [] move = new int[2];

        do {
            move[0] = ((int)(Math.random() * 1000) % 3);
            move[1] = ((int)(Math.random() * 1000) % 3);
        } while(!board.isValidMove(move[0], move[1]));
        return move;
    }

    //Determines the optimal move for the given player
    //TODO: Implement optimal move algorithm
    private int[] generateOptimalMove(GameBoard board, int id) {
        move = generateRandomMove(board);
        return move;
    }

}
