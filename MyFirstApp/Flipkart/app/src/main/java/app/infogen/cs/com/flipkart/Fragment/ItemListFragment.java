package app.infogen.cs.com.flipkart.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.infogen.cs.com.flipkart.Adapter.CustomAdapter;
import app.infogen.cs.com.flipkart.ItemsObject.BundleObject;
import app.infogen.cs.com.flipkart.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends android.support.v4.app.Fragment {

    private RecyclerView.LayoutManager recycLayoutManager;
    private ArrayList<BundleObject> bundleLst;

    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);

        //recycLayoutManager = new LinearLayoutManager(getActivity());
        recycLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(recycLayoutManager);

        bundleLst = new ArrayList<>();
        String[] namelist = {"Black Shoes Top", "Black Show Front", "Black Show Side", "Black Show Back", "Brwon Show", "Brown Show"};
        Integer[] imageItems = {R.mipmap.shoes_images_1, R.mipmap.shoes_image_2, R.mipmap.shoes_images_3, R.mipmap.shoes_images_4, R.mipmap.shoes_images_5, R.mipmap.shoes_image_6};
        String[] prodRate = {"390", "500", "610", "230", "410", "799"};

        for (int i = 0; i < namelist.length; i++) {
            BundleObject bo = new BundleObject();
            bo.setImageItems(imageItems[i]);
            bo.setProductName(namelist[i]);
            bo.setDetailsDetails(prodRate[i]);
            bundleLst.add(bo);
        }

        CustomAdapter customAdapter = new CustomAdapter(bundleLst);

        recyclerView.setAdapter(customAdapter);

        return view;
    }
}
