package app.infogen.cs.com.tracker.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import app.infogen.cs.com.tracker.common.TrackerConstants;

/**
 * Created by Dell on 11/24/2017.
 */

public class  PrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context _contex;

    public PrefManager(Context context){
        this._contex = context;
        sharedPreferences = _contex.getSharedPreferences(TrackerConstants.PREF_FILE,_contex.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
     editor.putBoolean(TrackerConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return  sharedPreferences.getBoolean(TrackerConstants.IS_FIRST_TIME_LAUNCH,true);
    }
}
