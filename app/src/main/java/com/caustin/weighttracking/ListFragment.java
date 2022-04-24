package com.caustin.weighttracking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caustin.weighttracking.models.Weight;
import com.caustin.weighttracking.db.WeightTrackerDatabase;

import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private WeightTrackerDatabase weighDb;
    private RecyclerView recyclerView;


    private WeightAdapter adapter;

    public interface OnWeightSelectedListener {
        void onWeightSelected(int weighId);
    }

    private OnWeightSelectedListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnWeightSelectedListener) {
            mListener = (OnWeightSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnWeightSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        weighDb = new WeightTrackerDatabase(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.weight_recycler_view);
        adapter = new WeightAdapter(weighDb.getAllWeights());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

//        ViewParent parent = (ViewParent) view.getParent();


        return view;
    }

    class WeightAdapter extends RecyclerView.Adapter<WeightHolder> {

        private List<Weight> mWeights;
        private WeightHolder holder;

        public WeightAdapter(List<Weight> weights) {
            mWeights = weights;
            Log.d("Size of weights", String.valueOf(weights.size()));
        }

        public WeightHolder getHolder() {
            return holder;
        }

        @Override
        public WeightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            holder = new WeightHolder(layoutInflater, parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(WeightHolder holder, int position) {
            Weight weight = mWeights.get(position);
            holder.bind(weight, position);
        }

        @Override
        public int getItemCount() {
            return mWeights.size();
        }

        public void addWeight(Weight weight) {
            mWeights.add(0, weight);
            notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }

        public void removeWeight(Weight weight) {
            int index = mWeights.indexOf(weight);
            if (index >= 0) {
                mWeights.remove(index);
            }
            notifyItemRemoved(index);
        }
    }

    class WeightHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Weight mWeight;
        private TextView mWeightTextView;

        private int mPosition = RecyclerView.NO_POSITION;



        public Weight getWeight() {
            return mWeight;
        }

        public WeightHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_weight, parent, false));
            itemView.setOnClickListener(this);
            WeightListActivity p = (WeightListActivity) getActivity();
            itemView.setOnLongClickListener(p);
            mWeightTextView = itemView.findViewById(R.id.weightId);
        }

        public void bind(Weight weight, int position) {
            mWeight = weight;
            mWeightTextView.setText(mWeight.getDateAsString());

            if (mPosition == position) {
                mWeightTextView.setBackgroundColor(Color.RED);
            }
        }

        @Override
        public void onClick(View view) {
            mPosition = getAbsoluteAdapterPosition();
            mListener.onWeightSelected(mWeight.getId());

        }
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String weightId = (String) view.getTag();
            Log.d("Current selected id", weightId);
            mListener.onWeightSelected(Integer.parseInt(weightId));
        }
    };

    public WeightAdapter getAdapter() {
        return adapter;
    }

}