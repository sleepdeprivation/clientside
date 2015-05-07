package groovycarnage.com.hermes.activity.threads;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import groovycarnage.com.hermes.R;
import groovycarnage.com.hermes.activity.main;
import groovycarnage.com.hermes.utility.IDStrings;

/**
 * An activity representing a single thread detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link threadListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link threadDetailFragment}.
 */
public class threadDetailActivity extends ActionBarActivity {

    public threadDetailFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();


            arguments.putParcelable( IDStrings.OP_ID,
                    getIntent().getExtras().getParcelable(IDStrings.OP_ID));

            arguments.putString(threadDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(threadDetailFragment.ARG_ITEM_ID));
            threadDetailFragment fragment = new threadDetailFragment();
            fragment.setArguments(arguments);
            myFragment = fragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.thread_detail_container, fragment)
                    .commit();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent i = new Intent(getApplicationContext(), main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
      The user wants to reply to this message
   */
    public void submitReply(View view) {
        Log.d("SUBMIT REPLY" , "ACTIVITY");
        myFragment.submitReply(view);
        finish();
        /*
        Intent i = new Intent(getApplicationContext(), threadListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        */
    }


}
