package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    Resources res;

    //Separator for stored strings
    final String SEP = "⛏";

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
    final String AI_NAME = "AI";
    String[] names;

    String[] scoreNames;
    String scoreNamesString;
    String[] scores;
    String scoresString;
    String[] lastPlayed;
    String lastPlayedString;

    TextView turnLabel;

    GameBoard board;

    final int AI = 0;
    final int PLAYER1 = 1;
    final int PLAYER2 = 2;

    private boolean aiGame;
    private boolean hardAi;

    private int currentPlayer;
    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        res = getResources();
        quitButton = findViewById(R.id.gameQuitButton);
        quitButton.setOnClickListener(this);
        turnLabel = findViewById(R.id.turnText);

        //Load shared prefs
        settings = getSharedPreferences(res.getString(R.string.prefKey), Context.MODE_PRIVATE);
        editor = settings.edit();

        //Load the score strings
        scoreNamesString = settings.getString(res.getString(R.string.namesKey), "");
        scoresString = settings.getString(res.getString(R.string.scoresKey), "");
        lastPlayedString = settings.getString(res.getString(R.string.lastPlayedKey), "");

        //Set the score strings up in arrays for ease of use
        setScoreArrays();

        //Set up players with saved names
        player1Name = settings.getString(res.getString(R.string.player1Key), "Player 1");
        player2Name = settings.getString(res.getString(R.string.player2Key), "Player 2");
        names = new String[]{AI_NAME, player1Name, player2Name};

        //Determines AI mode

        hardAi = settings.getBoolean(res.getString(R.string.aiMode), false);
        aiGame = getIntent().getBooleanExtra("aiGame", false);

        //Determine first player
        boolean playerFirst = settings.getBoolean(res.getString(R.string.playerFirstKey), true);
        if(playerFirst)
            currentPlayer = PLAYER1;
        else
            determineFirst();

        //Set up a blank game board
        setBoard();

        //Starts with AI turn if it is an AI game and it is AI's turn
        if(aiGame && currentPlayer == AI)
            aiTurn();
    }

    //Randomly sets the first player
    private void determineFirst() {
        Random rand = new Random();
        int result = Math.abs(rand.nextInt()) % 2;
        if(result == PLAYER1)
            currentPlayer = PLAYER1;
        else
            if(aiGame)
                currentPlayer = AI;
            else
                currentPlayer = PLAYER2;
    }

    //Set up the board to initial state
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

        //Store image views in arrays for ease of use
        ImageView[] col1 = {grid1_1, grid1_2, grid1_3};
        ImageView[] col2 = {grid2_1, grid2_2, grid2_3};
        ImageView[] col3 = {grid3_1, grid3_2, grid3_3};
        imageGrid = new ImageView[][]{col1, col2, col3};

        //Set on click listeners and images
        for (ImageView[] imageRow : imageGrid) {
            for (ImageView image : imageRow) {
                image.setOnClickListener(this::clickMove);
                image.setBackgroundResource(R.drawable.blank);
            }
        }
        //Create new board object
        board = new GameBoard(AI, PLAYER1, PLAYER2);
        gameOver = false;

        //Set label
        String label = names[currentPlayer] + "'s turn";
        turnLabel.setText(label);

    }

    //Quits the game
    @Override
    public void onClick(View v) {
        Intent quitIntent = new Intent(this, MenuActivity.class);
        startActivity(quitIntent);
        this.finish();
    }

    //Handles a click on the game board
    public void clickMove(View v) {
        if (currentPlayer >= 0 && currentPlayer != AI) {
            int col = -1, row = -1;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (v == imageGrid[i][j]) {
                        col = i;
                        row = j;
                    }
            playMove(col, row);
            if (!gameOver && aiGame && currentPlayer == AI)
                aiTurn();
        }
    }

    //Applies the given move to the board
    private void playMove(int col, int row) {
        if (board.isValidMove(col, row)) {
            board.playMove(col, row, currentPlayer);
            setMoveImage(col, row, currentPlayer);

            //Check for game-ending state
            if (board.checkGameOver()) {
                gameOver = true;
                winGame(board.getWinner());
            }
            else {
                //Set player turn to opponent
                if (currentPlayer == PLAYER1) {
                    if (aiGame)
                        setPlayer(AI);
                    else
                        setPlayer(PLAYER2);
                } else
                    setPlayer(PLAYER1);
            }
        }
    }

    //Set the player whose turn it is
    private void setPlayer(int id) {
        currentPlayer = id;
        //Set turn label
        String label = names[currentPlayer] + "'s turn.";
        turnLabel.setText(label);
    }

    //Generate and play a move for the AI
    private void aiTurn() {
        int[] move = {-1, -1};
        try {
            move = generateMove();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
        playMove(move[0], move[1]);
    }

    //Generate a move in a separate thread
    private int[] generateMove() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<int[]> result;
        MoveGenerator ai = new MoveGenerator(board, AI, hardAi);
        result = executor.submit(ai);
        return result.get();
    }

    //Set the correct image for the played move
    private void setMoveImage(int col, int row, int player) {
        if (player == PLAYER1)
            imageGrid[col][row].setBackgroundResource(R.drawable.x);
        else
            imageGrid[col][row].setBackgroundResource(R.drawable.o);
    }

    //Handle end of game
    private void winGame(int winner) {
        if(winner >= 0) {
            Toast.makeText(getBaseContext(), names[winner] + " wins!", Toast.LENGTH_SHORT).show();

            boolean updated = false;

            int loser = -1;
            if(aiGame) {
                if(winner == AI)
                    loser = PLAYER1;
                else
                    loser = AI;
            }
            else {
                if(winner == PLAYER1)
                    loser = PLAYER2;
                else
                    loser = PLAYER1;
            }

            Date date = new Date();
            String dateString = DateFormat.format("d-MM-yyyy HH:mm", date).toString();

            //Check if the player is already in the scoreboard. Increment their score if they are
            for (int i = 0; i < scoreNames.length && !updated; i++) {
                if (scoreNames[i].equals(names[winner])) {
                    scores[i] = String.valueOf(Integer.parseInt(scores[i]) + 1);
                    lastPlayed[i] = dateString;
                    updated = true;
                    //Otherwise, add the player at the first empty spot (we assume we have checked
                    //all the names when we reach a null entry)
                } else if (scoreNames[i] == null) {
                    scoreNames[i] = names[currentPlayer];
                    scores[i] = "1";
                    lastPlayed[i] = dateString;
                    updated = true;
                }
            }
            //If they are not in the array and there is no empty spot, expand the arrays and add a new entry
            if (!updated) {
                increaseScoreArraySize();
                int j = 0;
                while (scoreNames[j] != null)
                    j++;
                scoreNames[j] = names[winner];
                scores[j] = "1";
                lastPlayed[j] = dateString;
            }

            updated = false;

            //Update last played stat for loser as well
            for (int i = 0; i < scoreNames.length && !updated; i++) {
                if (scoreNames[i] != null && scoreNames[i].equals(names[loser])) {
                    lastPlayed[i] = dateString;
                    updated = true;
                    //Otherwise, add the player at the first empty spot (we assume we have checked
                    //all the names when we reach a null entry)
                } else if (scoreNames[i] == null) {
                    scoreNames[i] = names[loser];
                    scores[i] = "0";
                    lastPlayed[i] = dateString;
                    updated = true;
                }
            }
            //If they are not in the array and there is no empty spot, expand the arrays and add a new entry
            if (!updated) {
                increaseScoreArraySize();
                int j = 0;
                while (scoreNames[j] != null)
                    j++;
                scoreNames[j] = names[loser];
                scores[j] = "0";
                lastPlayed[j] = dateString;
            }
            currentPlayer = -1;
            storeScoreArrays();
        }
        else {
            Toast.makeText(getBaseContext(), "Draw!", Toast.LENGTH_SHORT).show();
        }
    }

    //Increase the size of the arrays that store the scoreboard
    private void increaseScoreArraySize() {
        String[] newNames = new String[scoreNames.length * 2];
        String[] newScores = new String[scoreNames.length * 2];
        String[] newLastPlayed = new String[lastPlayed.length * 2];

        for (int i = 0; i < scoreNames.length; i++) {
            newNames[i] = scoreNames[i];
            newScores[i] = scores[i];
            newLastPlayed[i] = lastPlayed[i];
        }
        scoreNames = newNames;
        scores = newScores;
        lastPlayed = newLastPlayed;
    }

    //Translates the stored strings of scores into arrays for ease of use
    private void setScoreArrays() {
        scoreNames = scoreNamesString.split(SEP);
        scores = scoresString.split(SEP);
        lastPlayed = lastPlayedString.split(SEP);
    }

    //Stores the score arrays as strings in the shared preferences
    private void storeScoreArrays() {
        //Build the arrays into strings
        StringBuilder nameSB = new StringBuilder();
        StringBuilder scoreSB = new StringBuilder();
        StringBuilder lastPlayedSB = new StringBuilder();
        for (int i = 0; i < scoreNames.length; i++) {
            if(scoreNames[i] != null) {
                nameSB.append(scoreNames[i]).append(SEP);
                scoreSB.append(scores[i]).append(SEP);
                lastPlayedSB.append(lastPlayed[i]).append(SEP);
            }
        }
        //Store the built strings in the shared preferences
        editor.putString(res.getString(R.string.namesKey), nameSB.toString());
        editor.putString(res.getString(R.string.scoresKey), scoreSB.toString());
        editor.putString(res.getString(R.string.lastPlayedKey), lastPlayedSB.toString());
        editor.commit();
    }
}