package com.caustin.weighttracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.caustin.weighttracking.db.WeightTrackerDatabase;
import com.caustin.weighttracking.models.Weight;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class WeightListActivity extends AppCompatActivity
        implements ListFragment.OnWeightSelectedListener,
        WeighAddDialogFragment.OnWeightEnteredListener,
        View.OnLongClickListener
        {

    private static final int REQUEST_SMS = 0;

    private Fragment fragment;
    private ListFragment.WeightHolder holder;

    private Weight mSelected;
    private int mPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    private static String[] PERMISSION_SMS = {
            Manifest.permission.SEND_SMS
    };

    private View mLayout;


    private void requestSMSPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i("LIST Activity",  "Displaying sms permission rationale to provide additional context.");
            Snackbar.make(mLayout, "It would be nice to share progress", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(WeightListActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_SMS);
                        }
                    })
                    .show();
        } else {

            // SMS permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mLayout = findViewById(R.id.action_add);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.list_fragment_container);

        if (fragment == null) {
            fragment = new ListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.list_fragment_container, fragment)
                    .commit();
//            fragment.setLongClickListener(this);
        }

    }



    @Override
    public void onWeightSelected(int weightId) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_WEIGHT_ID, weightId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        switch (item.getItemId()) {
            case R.id.action_add:
                addWeight();
            case R.id.action_sms:
                requestSMSPermission();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void addWeight() {
        FragmentManager manager = getSupportFragmentManager();
        WeighAddDialogFragment dialog = new WeighAddDialogFragment();
        dialog.show(manager, "weightDialog");
    }

    @Override
    public void onWeightEntered(Float value) {
        Weight weight = new Weight();
        weight.setDate(Calendar.getInstance().getTime());
        weight.setWeight(value);

        WeightTrackerDatabase weightDb = new WeightTrackerDatabase(getApplicationContext());

        if (weightDb.insertWeight(weight.getWeight(), weight.getDateAsString())) {
            Toast.makeText(this, "Added Weight", Toast.LENGTH_SHORT).show();
            if (fragment instanceof ListFragment) {
                ListFragment.WeightAdapter adapter = ((ListFragment) fragment).getAdapter();
                adapter.addWeight(weight);
            }
        } else {
            String message = "There was an error adding this weight";
            Toast.makeText(this , message, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onLongClick(View view) {
        if (fragment instanceof ListFragment) {
            ListFragment.WeightAdapter adapter = ((ListFragment) fragment).getAdapter();
            holder = adapter.getHolder();

            if (mActionMode != null) {
                return false;
            }
            mSelected = holder.getWeight();
            mPosition = holder.getAbsoluteAdapterPosition();
            adapter.notifyItemChanged(mPosition);
            mActionMode = WeightListActivity.this.startActionMode(mActionModeCallback);
            return true;
        } else {
            return false;
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Provide context menu for CAB
            MenuInflater inflater = mode.getMenuInflater();
            ((MenuInflater) inflater).inflate(R.menu.detail_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            WeightTrackerDatabase weightDb = new WeightTrackerDatabase(getApplicationContext());
            ListFragment.WeightAdapter adapter;
            if (fragment instanceof ListFragment) {
                adapter = ((ListFragment) fragment).getAdapter();
            } else {
                return false;
            }
            // Process action item selection
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // Delete from the database and remove from the RecyclerView
                    weightDb.deleteWeight(mSelected.getId());
                    adapter.removeWeight(mSelected);
                    // Close the CAB
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            ListFragment.WeightAdapter adapter;
            if (fragment instanceof ListFragment) {
                adapter = ((ListFragment) fragment).getAdapter();
            } else {
                return;
            }

            // CAB closing, need to deselect item if not deleted
            adapter.notifyItemChanged(mPosition);
            mPosition = RecyclerView.NO_POSITION;
        }
    };

}