package com.example.myfirstapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Dell on 12/10/2016.
 */
public class MusicIntentReceiver extends BroadcastReceiver {
    private static final String TAG = "Audio Plugin";
    MediaPlayer player;
    public IntentFilter intentFilter;
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    break;
                case 1:
                    Log.d(TAG, "Headset plugged");
                    playMusic(context,intent);
                    break;
            }
        }

    }

    public void playMusic(Context context, Intent intent){
        if(android.os.Build.VERSION.SDK_INT>=15){
            Intent MUSIC_PLAYER_INTENT=Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Min SDK 15
            //getExternalStorgetLocation();
             context.startActivity(MUSIC_PLAYER_INTENT);
            Log.d(TAG, "Headset plugged" + "SDK_INT >=15");

        }else{
            Log.d(TAG, "Headset plugged" + "ELSE Music player ");
            Intent MUSIC_PLAYER_INTENT = new Intent("android.intent.action.MUSIC_PLAYER");//Min SDK 8
            context.startActivity(MUSIC_PLAYER_INTENT);
        }
    }

    private void getExternalStorgetLocation() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equalsIgnoreCase(state)){
            File primaryExternalStorage = Environment.getExternalStorageDirectory();

            Log.d(TAG, "primaryExternalStorage : " + primaryExternalStorage.getPath() + "\n");

            String externalStorageRootDir;
            if ( (externalStorageRootDir = primaryExternalStorage.getParent()) == null ) {  // no parent...
                Log.d(TAG, "External Storage: " + primaryExternalStorage + "\n");
            }
            else {
                final File externalStorageRoot = new File( externalStorageRootDir );
                Log.v(TAG ,"externalStorageRoot AbsolutePath :" + externalStorageRoot.getAbsolutePath());
                Log.v(TAG ,"externalStorageRoot Path :" + externalStorageRoot.getPath());
                final File[] files = externalStorageRoot.listFiles();
                Log.v(TAG ,"Files Array :" + files);
                if (files !=null && files.length > 0) {
                    for (final File file : files) {
                        if (file.isDirectory() && file.canRead() && (file.listFiles().length > 0)) {  // it is a real directory (not a USB drive)...
                            Log.d(TAG, "External Storage: " + file.getAbsolutePath() + "\n");
                        }
                    }
                }
            }

        }
    }

}
