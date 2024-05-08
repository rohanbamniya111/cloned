package com.explo.fact_o_pedia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class InstructionDialogFragment extends DialogFragment {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "fact_o_pedia_prefs";
    private static final String DONT_SHOW_AGAIN_PREF = "dont_show_again";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        // Inflate the layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_layout, null);

        CheckBox dontShowAgainCheckBox = view.findViewById(R.id.checkbox_dont_show_again);
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, requireContext().MODE_PRIVATE);

        // Set the checked state of the checkbox based on saved preference
        dontShowAgainCheckBox.setChecked(sharedPreferences.getBoolean(DONT_SHOW_AGAIN_PREF, false));

        Button okButton = view.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the checkbox state
                saveCheckboxState(dontShowAgainCheckBox.isChecked());
                // Close the dialog
                dismiss();
            }
        });

        // Set the layout to the dialog
        builder.setView(view);
        return builder.create();
    }

    private void saveCheckboxState(boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DONT_SHOW_AGAIN_PREF, isChecked);
        editor.apply();
    }
}