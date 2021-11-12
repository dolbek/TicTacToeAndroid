package com.jessedean.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class StandingsActivity extends AppCompatActivity implements View.OnClickListener{

    //Shared preferences to store settings
    SharedPreferences settings;

    ListView scoreBoard;

    String[] scoreNames;
    String scoreNamesString;
    String[] scores;
    String scoresString;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        res = getResources();
        scoreBoard = findViewById(R.id.standingsListView);

        //Load standings records
        settings = getSharedPreferences(res.getString(R.string.prefKey), Context.MODE_PRIVATE);
        scoreNamesString = settings.getString(res.getString(R.string.namesKey), "");
        scoresString = settings.getString(res.getString(R.string.scoresKey), "");

        //Set the scores in an array, and populate the list
        setScoreArrays();
        populateScoreboard();

        Button backButton = findViewById(R.id.standingsBackButton);
        backButton.setOnClickListener(this);
    }

    //Populates the list with the stored scoreboard values
    //TODO figure out an alternate way to do this without the deprecated class
    private void populateScoreboard() {
        final String NAME = "Player: ";
        final String SCORE = "Wins: ";
        final String[] MATRIX = {"_id", "name", "score"};
        final String[] COLUMNS = {"name", "score"};
        final int[] LAYOUTS = {android.R.id.text1, android.R.id.text2};

        MatrixCursor cursor = new MatrixCursor(MATRIX);

        //cursor.addRow(new Object[] {0, NAME, SCORE});
        for(int i = 0; i < scoreNames.length; i++)
            cursor.addRow(new Object[] {i, scoreNames[i], scores[i]});

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_two_items, cursor, COLUMNS, LAYOUTS);

        scoreBoard.setAdapter(adapter);

    }

    //Switches back to the menu and closes this activity
    @Override
    public void onClick(View v) {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        this.finish();
    }

    //Translates the stored strings of scores into arrays for ease of use
    private void setScoreArrays() {
        scoreNames = scoreNamesString.split(",");
        scores = scoresString.split(",");
    }
}