package app.infogen.cs.com.navbar.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.infogen.cs.com.navbar.Adapter.BundleObject;
import app.infogen.cs.com.navbar.Adapter.CustomAdapter;
import app.infogen.cs.com.navbar.Adapter.ShowPlace;
import app.infogen.cs.com.navbar.BasicActivity;
import app.infogen.cs.com.navbar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolsFragment extends Fragment {

    private RecyclerView.LayoutManager rvLayout;
    // private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private ArrayList<BundleObject> bundleLst;
    private ArrayList<ShowPlace> showTimesPlace;
    private ArrayList<String> showTimings;

    public ToolsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        RecyclerView rv = view.findViewById(R.id.rv);
        rvLayout = new LinearLayoutManager(getActivity());
        //  rvLayout = new GridLayoutManager(getActivity(),2);
        //  rvLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        //  gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);

        rv.setLayoutManager(rvLayout);

        String[] namelist = {"Bahuubali", "Kabali", "Villan"};
        String[] cert = {"UA", "A", "U"};
        String[] details = {"TAMIL", "KANADA", "ENGILIS"};
        // String[] showPlace = {"WED 29, NOV| Gopalan Grand Mall ,Old Madras Road", "WED 29, NOV| Gopalan Signature Mall ,Old Madras Road", "WED 29, NOV| Gopalan Grand Mall ,Old Airport Road"};


        showTimings = new ArrayList<>();
        showTimings.add("7.30");
        showTimings.add("9.30");

        showTimesPlace = new ArrayList<>();
        showTimesPlace.add(new ShowPlace("WED 29, NOV| Gopalan Grand Mall ,Old Madras Road", showTimings));
        showTimesPlace.add(new ShowPlace("WED 29, NOV| Gopalan Signature Mall ,Old Madras Road", showTimings));
        showTimesPlace.add(new ShowPlace("WED 29, NOV| Gopalan Grand Mall ,Old Airport Road", showTimings));

        Integer[] images = {R.mipmap.baahubali, R.mipmap.kabali, R.mipmap.villain};
        bundleLst = new ArrayList<>();

        for (int i = 0; i < namelist.length; i++) {
            BundleObject bo = new BundleObject();
            bo.setCert(cert[i]);
            bo.setDetails(details[i]);
            bo.setMovieImage(images[i]);
            bo.setName(namelist[i]);
            bo.setShowPlace(showTimesPlace);
            bundleLst.add(bo);
        }
        CustomAdapter customAdapter = new CustomAdapter(bundleLst);
        rv.setAdapter(customAdapter);

        return view;
    }
}