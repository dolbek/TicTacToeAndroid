package com.jessedean.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveButton = findViewById(R.id.settingsSaveButton);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickSave();
    }

    private void clickSave() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        this.finish();
    }
}