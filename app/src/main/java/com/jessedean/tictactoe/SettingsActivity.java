package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    //Shared preferences to store settings
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    String player1Name;
    String player2Name;
    boolean hardAI;

    Button saveButton;
    EditText player1EditText;
    EditText player2EditText;
    CheckBox hardAICheck;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        res = getResources();

        settings = getSharedPreferences(res.getString(R.string.prefKey), Context.MODE_PRIVATE);
        editor = settings.edit();

        saveButton = findViewById(R.id.settingsSaveButton);
        saveButton.setOnClickListener(this);

        player1EditText = findViewById(R.id.player1EditText);
        player2EditText = findViewById(R.id.player2EditText);
        hardAICheck = findViewById(R.id.hardAICheck);

        //Load values to variables
        player1Name = settings.getString(res.getString(R.string.player1Key), "Player 1");
        player2Name = settings.getString(res.getString(R.string.player2Key), "Player 2");
        hardAI = settings.getBoolean(res.getString(R.string.aiMode), false);

        //Apply loaded values to UI
        player1EditText.setText(player1Name);
        player2EditText.setText(player2Name);
        hardAICheck.setChecked(hardAI);
    }

    @Override
    public void onClick(View v) {
        //Save if save button was clicked
        if(v == saveButton) {
            editor.putString(res.getString(R.string.player1Key), player1EditText.getText().toString());
            editor.putString(res.getString(R.string.player2Key), player2EditText.getText().toString());
            editor.putBoolean(res.getString(R.string.aiMode), hardAICheck.isChecked());
            editor.commit();
        }

        //Load main menu
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        this.finish();
    }

    //Save the current settings
    private void clickSave() {


    }
}