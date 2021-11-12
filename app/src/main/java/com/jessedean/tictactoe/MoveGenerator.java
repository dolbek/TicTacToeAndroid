package com.jessedean.tictactoe;

import java.util.concurrent.Callable;

public class MoveGenerator implements Callable<int[]> {

    int [] move;
    boolean smartMoves;
    GameBoard board;
    int id;

    //Constructor
    public MoveGenerator(GameBoard board, int id, boolean smart) {
        move = new int[2];
        this.board = board;
        this.id = id;
        smartMoves = smart;
    }

    //Calls the appropriate move generation method
    public int[] generateMove() {
        if(smartMoves)
            move = generateOptimalMove();
        else
            move = generateRandomMove();
        return move;
    }

    //Generates a random valid move
    private int[] generateRandomMove() {

        int [] move = new int[2];

        do {
            move[0] = ((int)(Math.random() * 1000) % 3);
            move[1] = ((int)(Math.random() * 1000) % 3);
        } while(!board.isValidMove(move[0], move[1]));
        return move;
    }

    //Determines the optimal move for the given player
    private int[] generateOptimalMove() {

        int[][] boardState = board.getBoardState();
        int[] blockingMove = {-1, -1};
        int[] winningMove = {-1, -1};
        int[] bestMove = {-1, -1};

        //Check for linear winning move and play it if it exists. Else play a move to block a win if it exists.
        for(int i = 0; i < 3 && winningMove[0] < 0; i++) {
            //Col
            if (boardState[i][0] >= 0 && boardState[i][0] == boardState[i][1] && board.isValidMove(i, 2)) {
                if (boardState[i][0] == id) {
                    winningMove[0] = i;
                    winningMove[1] = 2;
                } else {
                    blockingMove[0] = i;
                    blockingMove[1] = 2;
                }
            }
            else if (boardState[i][0] >= 0 && boardState[i][0] == boardState[i][2] && board.isValidMove(i, 1)) {
                if (boardState[i][0] == id) {
                    winningMove[0] = i;
                    winningMove[1] = 1;
                } else {
                    blockingMove[0] = i;
                    blockingMove[1] = 1;
                }
            }
            else if (boardState[i][1] >= 0 && boardState[i][1] == boardState[i][2] && board.isValidMove(i, 0)) {
                if (boardState[i][1] == id) {
                    winningMove[0] = i;
                    winningMove[1] = 0;
                } else {
                    blockingMove[0] = i;
                    blockingMove[1] = 0;
                }
            }
            //Row
            else if (boardState[0][i] >= 0 && boardState[0][i] == boardState[1][i] && board.isValidMove(2, i)) {
                if (boardState[0][i] == id) {
                    winningMove[0] = 2;
                    winningMove[1] = i;
                } else {
                    blockingMove[0] = 2;
                    blockingMove[1] = i;
                }
            }
            else if (boardState[0][i] >= 0 && boardState[0][i] == boardState[2][i] && board.isValidMove(1, i)) {
                if (boardState[0][i] == id) {
                    winningMove[0] = 1;
                    winningMove[1] = i;
                } else {
                    blockingMove[0] = 1;
                    blockingMove[1] = i;
                }
            }
            else if (boardState[1][i] >= 0 && boardState[1][i] == boardState[2][i] && board.isValidMove(0, i)) {
                if (boardState[1][i] == id) {
                    winningMove[0] = 0;
                    winningMove[1] = i;
                } else {
                    blockingMove[0] = 0;
                    blockingMove[1] = i;
                }
            }
        }
        if(winningMove[0] < 0) {
            //Check for diagonal winning or blocking move, as above
            if (boardState[0][0] >= 0 && boardState[0][0] == boardState[1][1] && board.isValidMove(2, 2)) {
                if (boardState[0][0] == id) {
                    winningMove[0] = 2;
                    winningMove[1] = 2;
                } else {
                    blockingMove[0] = 2;
                    blockingMove[1] = 2;
                }
            } else if (boardState[0][0] >= 0 && boardState[0][0] == boardState[2][2] && board.isValidMove(1, 1)) {
                if (boardState[0][0] == id) {
                    winningMove[0] = 1;
                    winningMove[1] = 1;
                } else {
                    blockingMove[0] = 1;
                    blockingMove[1] = 1;
                }
            } else if (boardState[1][1] >= 0 && boardState[1][1] == boardState[2][2] && board.isValidMove(0, 0)) {
                if (boardState[1][1] == id) {
                    winningMove[0] = 0;
                    winningMove[1] = 0;
                } else {
                    blockingMove[0] = 0;
                    blockingMove[1] = 0;
                }
            } else if (boardState[2][0] >= 0 && boardState[2][0] == boardState[0][2] && board.isValidMove(1, 1)) {
                if (boardState[2][0] == id) {
                    winningMove[0] = 1;
                    winningMove[1] = 1;
                } else {
                    blockingMove[0] = 1;
                    blockingMove[1] = 1;
                }
            } else if (boardState[2][0] >= 0 && boardState[2][0] == boardState[1][1] && board.isValidMove(0, 2)) {
                if (boardState[2][0] == id) {
                    winningMove[0] = 0;
                    winningMove[1] = 2;
                } else {
                    blockingMove[0] = 0;
                    blockingMove[1] = 2;
                }
            } else if (boardState[1][1] >= 0 && boardState[1][1] == boardState[0][2] && board.isValidMove(2, 0)) {
                if (boardState[1][1] == id) {
                    winningMove[0] = 2;
                    winningMove[1] = 0;
                } else {
                    blockingMove[0] = 2;
                    blockingMove[1] = 0;
                }
            }
        }
        //With no wins or blocks possible, find the next optimal move
        if(winningMove[0] < 0 && blockingMove[0] < 0) {
            //Take center if it is free
            if(board.isValidMove(1, 1)) {
                bestMove[0] = 1;
                bestMove[1] = 1;
            }
            //Take corners with open opposites next
            else if(board.isValidMove(0,0) && board.isValidMove(2, 2)) {
                bestMove[0] = 0;
                bestMove[1] = 0;
            }
            else if(board.isValidMove(2,0) && board.isValidMove(0, 2)) {
                bestMove[0] = 2;
                bestMove[1] = 0;
            }

            if(bestMove[0] < 0) {
                //Take any corner next, unless it is the last corner
                if(board.isValidMove(0, 0) && (board.isValidMove(2,2) || board.isValidMove(2,0) || board.isValidMove(0,2))) {
                    bestMove[0] = 0;
                    bestMove[1] = 0;
                }
                else if(board.isValidMove(2, 2) && (board.isValidMove(0,0) || board.isValidMove(2,0) || board.isValidMove(0,2))) {
                    bestMove[0] = 2;
                    bestMove[1] = 2;
                }
                else if(board.isValidMove(2, 0) && (board.isValidMove(2,2) || board.isValidMove(0,0) || board.isValidMove(0,2))) {
                    bestMove[0] = 2;
                    bestMove[1] = 0;
                }
                else if(board.isValidMove(0, 2) && (board.isValidMove(2,2) || board.isValidMove(2,0) || board.isValidMove(0,0))) {
                    bestMove[0] = 0;
                    bestMove[1] = 2;
                }
                //Take random space from remaining spaces
                if(bestMove[0] < 0) {
                    if(board.isValidMove(0, 1)) {
                        bestMove[0] = 0;
                        bestMove[1] = 1;
                    }
                    else if(board.isValidMove(1, 0)) {
                        bestMove[0] = 1;
                        bestMove[1] = 0;
                    }
                    else if(board.isValidMove(1, 2)) {
                        bestMove[0] = 1;
                        bestMove[1] = 2;
                    }
                    else if(board.isValidMove(2, 1)) {
                        bestMove[0] = 2;
                        bestMove[1] = 1;
                    }
                    //Take final corner last
                    if(bestMove[0] < 0) {
                        if(board.isValidMove(0, 0)) {
                            bestMove[0] = 0;
                            bestMove[1] = 0;
                        }
                        else if(board.isValidMove(0, 2)) {
                            bestMove[0] = 0;
                            bestMove[1] = 2;
                        }
                        else if(board.isValidMove(2, 0)) {
                            bestMove[0] = 2;
                            bestMove[1] = 0;
                        }
                        else if(board.isValidMove(2, 2)) {
                            bestMove[0] = 2;
                            bestMove[1] = 2;
                        }
                    }
                }
            }
        }
        //Return the appropriate move
        if(winningMove[0] >= 0)
            move = winningMove;
        else if(blockingMove[0] >=0)
            move = blockingMove;
        else
            move = bestMove;
        return move;
    }

    //Used to call the class in a separate thread
    @Override
    public int[] call() {
        return generateMove();
    }
}
