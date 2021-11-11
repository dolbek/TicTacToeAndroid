package com.jessedean.tictactoe;

public class GameBoard {

    final int BLANK = -1;
    int ai;
    int player1;
    int player2;
    int [][] board;
    int winner;

    //Constructor. Sets up a new board with every space set to blank
    public GameBoard(int ai, int player1, int player2) {
        board = new int[3][3];
        for(int i = 0; i < 3; i++) {
            board[i][0] = BLANK;
            board[i][1] = BLANK;
            board[i][2] = BLANK;
        }
        this.ai = ai;
        this.player1 = player1;
        this.player2 = player2;

        winner = BLANK;
    }

    //Assign a move to the board if it is valid
    public boolean playMove(int col, int row, int player) {
        boolean legalMove = false;
        if(board[col][row] == BLANK && (player == ai || player == player1 || player == player2)) {
            board[col][row] = player;
            legalMove = true;
        }
        return legalMove;
    }

    //Check if the game is over, and returns the winner if there is one, -1 if not
    public boolean checkGameOver() {
        boolean gameOver = true;
        int val;

        //Check for full board
        for(int i = 0; i < 3 && gameOver; i++)
            for(int j = 0; j < 3 && gameOver; j++)
                if(board[i][j] == BLANK || board[j][i] == BLANK)
                    gameOver = false;


        //Check for column win
        for(int i = 0; i < 3; i++) {
            val = board[i][0];
            if((val != BLANK) && (val == board[i][1]) && (val == board[i][2])) {
                winner = val;
                gameOver = true;
            }
        }
        //Check for row win
        if(winner == BLANK) {
            for(int j = 0; j < 3; j++) {
                val = board[0][j];
                if((val != BLANK) && (val == board[1][j]) && (val == board[2][j])) {
                    winner = val;
                    gameOver = true;
                }
            }
        }
        //Check for diagonal win
        if(winner != BLANK && board [1][1] != BLANK) {
            if((board[0][0] == board[1][1] && board [0][0] == board [2][2]) || (board[0][2] == board[1][1] && board[0][2] == board[2][0])) {
                winner = board[1][1];
                gameOver = true;
            }
        }


        return gameOver;
    }

    public int getWinner() {
        return winner;
    }
}
