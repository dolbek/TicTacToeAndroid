package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class StandingsActivity extends AppCompatActivity implements View.OnClickListener{

    //Shared preferences to store settings
    SharedPreferences settings;

    //Separator for stored strings
    final String SEP = "⛏";

    ListView scoreList;

    String[] scoreNames;
    String scoreNamesString;
    String[] scores;
    String scoresString;
    String[] lastPlayed;
    String lastPlayedString;

    Player[] records;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        res = getResources();
        scoreList = findViewById(R.id.scoreList);

        //Load standings records
        settings = getSharedPreferences(res.getString(R.string.prefKey), Context.MODE_PRIVATE);
        scoreNamesString = settings.getString(res.getString(R.string.namesKey), "");
        scoresString = settings.getString(res.getString(R.string.scoresKey), "");
        lastPlayedString = settings.getString(res.getString(R.string.lastPlayedKey), "");


        //Set the scores in an array, and populate the list
        setScoreArrays();
        populateScoreboard();

        Button backButton = findViewById(R.id.standingsBackButton);
        backButton.setOnClickListener(this);
    }

    //Populates the list with the stored scoreboard values
    private void populateScoreboard() {

        ThreeColAdapter<Player> adapter = new ThreeColAdapter<Player>(this, R.layout.three_column_list, records);

        scoreList.setAdapter(adapter);


    }

    //Switches back to the menu and closes this activity
    @Override
    public void onClick(View v) {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        this.finish();
    }

    //Translates the stored strings of scores into Player objects
    private void setScoreArrays() {
        scoreNames = scoreNamesString.split(SEP);
        scores = scoresString.split(SEP);
        lastPlayed = lastPlayedString.split(SEP);

        records = new Player[scoreNames.length - 1];

        for(int i = 1; i < scoreNames.length; i++) {
            Player player = new Player(scoreNames[i], scores[i], lastPlayed[i]);
            records[i-1] = player;
        }
    }
}