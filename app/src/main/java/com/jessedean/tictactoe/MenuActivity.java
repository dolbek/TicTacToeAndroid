package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button playButton;
    Button settingsButton;
    Button scoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setButtons();
    }

    @Override
    public void onClick(View v) {
        if(v == playButton)
            clickPlay();
        else if(v == settingsButton)
            clickSettings();
        else if(v == scoresButton)
            clickScores();
    }

    private void clickScores() {
        Intent scoresIntent = new Intent(this, StandingsActivity.class);
        startActivity(scoresIntent);
        this.finish();
    }

    private void clickSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        this.finish();
    }

    private void clickPlay() {
        Intent playIntent = new Intent(this, GameActivity.class);
        startActivity(playIntent);
        this.finish();
    }

    private void setButtons() {
        playButton = findViewById(R.id.menuPlayButton);
        settingsButton = findViewById(R.id.menuSettingsButton);
        scoresButton = findViewById(R.id.menuScoresButton);
        Button [] buttons = {playButton, settingsButton, scoresButton};

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }
}