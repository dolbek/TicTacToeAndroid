package com.jessedean.tictactoe;

/*A class to store player info regarding name, last played, and number of wins */
public class Player {

    private String name;
    private String wins;
    private String lastPlayed;

    //Constructor
    public Player(String name, String wins, String lastPlayed) {
        this.name = name;
        this.wins = wins;
        this.lastPlayed = lastPlayed;
    }

    //Returns the name
    public String getName() {
        return name;
    }

    //Returns the number of wins
    public String getWins() {
        return wins;
    }

    //Returns the last played date
    public String getLastPlayed() {
        return lastPlayed;
    }
}
