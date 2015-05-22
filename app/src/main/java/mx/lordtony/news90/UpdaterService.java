package mx.lordtony.news90;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import mx.lordtony.news90.db.DBHelper;
import mx.lordtony.news90.db.DBOperations;
import mx.lordtony.news90.models.Tweet;
import mx.lordtony.news90.utils.ConstantsUtils;
import mx.lordtony.news90.utils.TwitterUtils;

public class UpdaterService extends Service {

    private static final String TAG = UpdaterService.class.getSimpleName();
    private final int DELAY = 300000;
    private boolean runFlag = false;
    private Updater updater;
    private News90Application application;
    private DBOperations dbOperations;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreated Updater");
        application = (News90Application) getApplication();
        updater = new Updater();
        dbOperations = new DBOperations(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy Updater");
        runFlag = false;
        application.setServiceRunningFlag(false);
        updater.interrupt();
        updater = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "onStartCommand Updater");
        if(!runFlag){
            Log.d(TAG, "Entro al if en onStartCommand");
            runFlag = true;
            application.setServiceRunningFlag(true);
            updater.start();

        }
        return START_STICKY;
    }

    private class Updater extends Thread{
        private Intent intent;

        ArrayList<Tweet> timeline = new ArrayList<Tweet>();

        private Updater() {
            super("UpdaterService-UpdaterThread");

        }

        @Override
        public void run() {
            super.run();
            UpdaterService updaterService = UpdaterService.this;

            while (updaterService.runFlag) {
                Log.d(TAG, "UpdaterThread running");

                try{
                    timeline = TwitterUtils.getTimelineForSearchTerm(ConstantsUtils.POLI_TERM);

                    ContentValues values = new ContentValues();
                    for(Tweet tweet : timeline){
                        values.clear();
                        values.put(DBHelper.C_ID, tweet.getId());
                        values.put(DBHelper.C_NAME, tweet.getName());
                        values.put(DBHelper.C_SCREEN_NAME, tweet.getScreenName());
                        values.put(DBHelper.C_IMAGE_PROFILE_URL, tweet.getProfileImageUrl());
                        values.put(DBHelper.C_TEXT, tweet.getText());
                        values.put(DBHelper.C_CREATED_AT, tweet.getCreatedAt());

                        Log.i(TAG, "CREATED AT_SERVICE: " + tweet.getCreatedAt());

                        dbOperations.insertOrIgnore(values);
                    }
                    intent = new Intent(ConstantsUtils.NEW_TWEETS_INTENT_FILTER);
                    updaterService.sendBroadcast(intent);
                    Thread.sleep(DELAY);

                }catch (InterruptedException e){
                    updaterService.runFlag = false;
                    application.setServiceRunningFlag(false);
                }

            }
        }
    }
}
