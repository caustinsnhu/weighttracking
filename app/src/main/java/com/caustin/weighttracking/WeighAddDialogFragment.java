package com.caustin.weighttracking;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class WeighAddDialogFragment extends DialogFragment {



    public interface OnWeightEnteredListener {
        void onWeightEntered(Float value);
    }

    private OnWeightEnteredListener mListener;

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        final EditText valueEditText = new EditText(getActivity());
        valueEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        valueEditText.setMaxLines(1);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.subject)
                .setView(valueEditText)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Float value = Float.parseFloat(valueEditText.getText().toString());
                        mListener.onWeightEntered(value);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnWeightEnteredListener) context;
    }
}
