package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    Button quitButton;
    ImageView grid1_1;
    ImageView grid1_2;
    ImageView grid1_3;
    ImageView grid2_1;
    ImageView grid2_2;
    ImageView grid2_3;
    ImageView grid3_1;
    ImageView grid3_2;
    ImageView grid3_3;
    ImageView[][] imageGrid;

    String player1Name;
    String player2Name;
    String aiName = "AI";
    String [] names;

    TextView turnLabel;

    GameBoard board;
    MoveGenerator ai;

    final int AI = 0;
    final int PLAYER1 = 1;
    final int PLAYER2 = 2;

    boolean aiGame;

    int currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        quitButton = findViewById(R.id.gameQuitButton);
        quitButton.setOnClickListener(this);

        turnLabel = findViewById(R.id.turnText);

        currentPlayer = PLAYER1;
        player1Name = "Player 1";
        player2Name = "Player 2";

        names = new String[] {aiName, player1Name, player2Name};

        aiGame = false;
        ai = new MoveGenerator();

        setBoard();
    }

    private void setBoard() {
        grid1_1 = findViewById(R.id.imageView1_1);
        grid1_2 = findViewById(R.id.imageView1_2);
        grid1_3 = findViewById(R.id.imageView1_3);
        grid2_1 = findViewById(R.id.imageView2_1);
        grid2_2 = findViewById(R.id.imageView2_2);
        grid2_3 = findViewById(R.id.imageView2_3);
        grid3_1 = findViewById(R.id.imageView3_1);
        grid3_2 = findViewById(R.id.imageView3_2);
        grid3_3 = findViewById(R.id.imageView3_3);

        ImageView[] col1 = {grid1_1, grid1_2, grid1_3};
        ImageView[] col2 = {grid2_1, grid2_2, grid2_3};
        ImageView[] col3 = {grid3_1, grid3_2, grid3_3};
        imageGrid = new ImageView[][] {col1, col2, col3};

        for (ImageView[] imageRow: imageGrid) {
            for (ImageView image: imageRow) {
                image.setOnClickListener(this::clickMove);
                image.setBackgroundResource(R.drawable.blank);
            }
        }

        board = new GameBoard(AI, PLAYER1, PLAYER2);
    }

    @Override
    public void onClick(View v) {
        Intent quitIntent = new Intent(this, MenuActivity.class);
        startActivity(quitIntent);
        this.finish();
    }

    public void clickMove(View v) {
        if(currentPlayer != AI) {
            int col = -1, row = -1;
            for(int i = 0; i < 3; i++)
                for(int j = 0; j < 3; j++)
                    if(v == imageGrid[i][j]) {
                        col = i;
                        row = j;
                    }

            playMove(col, row);
        }
    }

    private void playMove(int col, int row) {
        if(board.playMove(col, row, currentPlayer)) {
            setMoveImage(col, row, currentPlayer);

            if (currentPlayer == PLAYER1) {
                if (aiGame)
                    currentPlayer = AI;
                else
                    currentPlayer = PLAYER2;
            } else if (currentPlayer == PLAYER2) {
                currentPlayer = PLAYER1;
            } else {
                currentPlayer = PLAYER1;
            }

            if (board.checkGameOver())
                winGame(board.getWinner());
            else {
                if (currentPlayer == PLAYER1)
                    turnLabel.setText("Player 1's Turn");
                else if (currentPlayer == PLAYER2)
                    turnLabel.setText("Player 2's Turn");
                else {
                    turnLabel.setText("AI's Turn");
                    aiTurn();
                }
            }
        }
    }

    private void winGame(int winner) {
        if(winner != -1)
            Toast.makeText(getBaseContext(), names[winner] + " wins!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getBaseContext(), "Draw!", Toast.LENGTH_SHORT).show();
        currentPlayer = -1;
    }

    private void aiTurn() {
        int[] aiMove = ai.generateMove(board, AI);
        playMove(aiMove[0], aiMove[1]);
    }

    public void setMoveImage(int col, int row, int player) {
        if(player == PLAYER1)
            imageGrid[col][row].setBackgroundResource(R.drawable.x);
        else
            imageGrid[col][row].setBackgroundResource(R.drawable.o);
    }
}