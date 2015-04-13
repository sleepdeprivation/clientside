package groovycarnage.com.hermes.activity.threads;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.model.Message;

/**
 * An activity representing a list of threads. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link threadDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link threadListFragment} and the item details
 * (if present) is a {@link threadDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link threadListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class threadListActivity extends ActionBarActivity
        implements threadListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static final String OP_ID = "OP_HEAD_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);

        if (findViewById(R.id.thread_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((threadListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.thread_list))
                    .setActivateOnItemClick(true);

        }

    }

    /**
     * Callback method from {@link threadListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Message OP) {
        Log.d("FRAGMENT", "ITEM SELECTED");

        Bundle arguments = new Bundle();
        arguments.putParcelable(OP_ID, OP); //OP implements parcelable

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            threadDetailFragment fragment = new threadDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.thread_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, threadDetailActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
        }
    }
}
