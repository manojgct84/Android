package app.infogen.cs.com.flipkart.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.infogen.cs.com.flipkart.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsProductFragment extends android.support.v4.app.Fragment {


    public DetailsProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_product, container, false);
        return view;
    }

}
