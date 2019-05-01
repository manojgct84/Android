package app.infogen.cs.com.tabapplication;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {

    private SeekBar musicVolume;
    AudioManager audioManager;

    public OneFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_one, container, false);
        musicVolume = (SeekBar) v.findViewById(R.id.volumeMusic);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        return v;
    }

}
