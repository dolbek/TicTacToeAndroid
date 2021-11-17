package com.jessedean.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*An array adapter to display three columns of data in a listview */
public class ThreeColAdapter<T> extends ArrayAdapter<Player> {

    private Context mContext;
    private int mResource;

    //Constructor
    public ThreeColAdapter(@NonNull Context context, int resource, @NonNull Player[] objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    //Sets the text and inflates the view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Player player = getItem(position);
        String name = player.getName();
        String wins = player.getWins();
        String lastPlayed = player.getLastPlayed();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nameText = convertView.findViewById(R.id.text1);
        TextView winsText = convertView.findViewById(R.id.text2);
        TextView lastPlayedText = convertView.findViewById(R.id.text3);

        nameText.setText(name);
        winsText.setText(wins);
        lastPlayedText.setText(lastPlayed);

        return convertView;
    }
}
