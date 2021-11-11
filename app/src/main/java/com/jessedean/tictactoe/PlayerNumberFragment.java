package com.jessedean.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlayerNumberFragment extends DialogFragment {

    //Alert dialogue to select game mode
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.playerCount).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionInterface.cancelSelect(PlayerNumberFragment.this);
            }
        })
        .setNeutralButton(R.string.onePlayer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionInterface.onePlayerSelect(PlayerNumberFragment.this);
            }
        })
        .setPositiveButton(R.string.twoPlayer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionInterface.twoPlayerSelect(PlayerNumberFragment.this);
            }
        });
        return builder.create();
    }

    //Interface to communicate with main activity
    public interface SelectionInterface {
        void cancelSelect(DialogFragment dialog);
        void onePlayerSelect(DialogFragment dialog);
        void twoPlayerSelect(DialogFragment dialog);
    }
    SelectionInterface selectionInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selectionInterface = (SelectionInterface) context;
        }
        catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SelectionInterface");
        }
    }
}