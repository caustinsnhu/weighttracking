package com.caustin.weighttracking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caustin.weighttracking.db.WeightTrackerDatabase;
import com.caustin.weighttracking.models.Weight;

/**
 * A simple {@link Fragment} subclass.
 * Use the {DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private WeightTrackerDatabase db;
    private Weight mWeight;

    public static DetailsFragment newInstance(int weightId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("weightId", weightId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new WeightTrackerDatabase(getContext());

        if (getArguments() != null) {
            int weightId = getArguments().getInt("weightId");
            Weight res = db.getWeight(weightId);
            Log.d("The weight queried is", String.valueOf(res.getId()));
            mWeight = db.getWeight(weightId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView weightTextView = (TextView) view.findViewById(R.id.weightDate);
        weightTextView.setText(String.format("Date: %s", String.valueOf(mWeight.getDateAsString())));

        TextView descriptionTextView = (TextView) view.findViewById(R.id.dateValue);
        descriptionTextView.setText(String.format("Weight: %s", String.valueOf(mWeight.getWeight())));

        return view;
    }

}