package mx.lordtony.news90;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.lordtony.news90.db.DBOperations;
import mx.lordtony.news90.list.TweetAdapter;
import mx.lordtony.news90.models.Tweet;
import mx.lordtony.news90.utils.ConstantsUtils;
import mx.lordtony.news90.utils.TwitterUtils;


public class TimelineActivity extends ActionBarActivity {

    private ListView listView;
    private DBOperations dbOperations;
    private TweetAdapter adapter;
    private TimelineReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        listView = (ListView) findViewById(R.id.lv_timeline);
        dbOperations = new DBOperations(this);
        receiver = new TimelineReceiver();
        filter = new IntentFilter(ConstantsUtils.NEW_TWEETS_INTENT_FILTER);
        new GetTimelineTask().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public void updateListView(ArrayList<Tweet> tweets){
        adapter = new TweetAdapter(this, R.layout.row_tweet, tweets);
        listView.setAdapter(adapter);
    }

    private void updateListViewWithCache(){
        adapter = new TweetAdapter(this, R.layout.row_tweet, dbOperations.getStatusUpdates());
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        Log.e("tag90", "Funciono el updateListView");
    }

    class GetTimelineTask extends AsyncTask<Object, Void, ArrayList<Tweet>>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TimelineActivity.this);
            progressDialog.setTitle(getResources().getString(R.string.label_tweet_search_loader));
            progressDialog.setMessage(getResources().getString(R.string.label_tweet_msg));
            progressDialog.show();
        }

        @Override
        protected ArrayList<Tweet> doInBackground(Object... params) {
            return TwitterUtils.getTimelineForSearchTerm(ConstantsUtils.POLI_TERM);
        }

        @Override
        protected void onPostExecute(ArrayList<Tweet> timeline) {
            super.onPostExecute(timeline);

            progressDialog.dismiss();

            if (timeline.isEmpty()) {
                Toast.makeText(TimelineActivity.this,
                        getResources().
                        getString(R.string.label_tweets_not_found),
                        Toast.LENGTH_SHORT).show();
            }else{
                updateListView(timeline);
                Toast.makeText(TimelineActivity.this,
                        getResources().
                        getString(R.string.label_tweets_downloaded),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class TimelineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag90", "Funciono el intent");
            updateListViewWithCache();
        }
    }

}
