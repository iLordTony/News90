package mx.lordtony.news90;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by USER on 13/03/2015.
 */
public class News90Application extends Application {

    private static final String TAG = News90Application.class.getSimpleName();
    private boolean serviceRunningFlag;

    public boolean isServiceRunningFlag(){
        return serviceRunningFlag;
    }

    public void setServiceRunningFlag(boolean serviceRunningFlag){
        this.serviceRunningFlag = serviceRunningFlag;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreated la Aplication");
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "onTerminate la Aplication");
        stopService(new Intent(this, UpdaterService.class));
    }
}
