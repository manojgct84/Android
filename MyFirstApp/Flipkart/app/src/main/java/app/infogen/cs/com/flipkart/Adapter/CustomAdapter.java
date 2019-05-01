package app.infogen.cs.com.flipkart.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.infogen.cs.com.flipkart.Fragment.DetailsProductFragment;
import app.infogen.cs.com.flipkart.ItemsObject.BundleObject;
import app.infogen.cs.com.flipkart.R;

/**
 * Created by Dell on 12/4/2017.
 */


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    private ArrayList<BundleObject> bundleObjectLst = new ArrayList<>();

    public CustomAdapter(ArrayList<BundleObject> bundleObjectLst) {
        this.bundleObjectLst = bundleObjectLst;
    }


    @Override
    public CustomAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flipkart_items, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.Holder holder, int position) {
        holder.imageDetails.setImageResource(bundleObjectLst.get(position).getImageItems());
        holder.prodName.setText(bundleObjectLst.get(position).getProductName());
        holder.prodRate.setText(bundleObjectLst.get(position).getDetailsDetails());
    }

    @Override
    public int getItemCount() {
        return bundleObjectLst.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView prodRate, prodName;
        private ImageView imageDetails;

        public Holder(View itemView) {
            super(itemView);
            prodRate = itemView.findViewById(R.id.prodRate);
            prodName = itemView.findViewById(R.id.prodName);
            imageDetails = itemView.findViewById(R.id.itemImage);

            imageDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    DetailsProductFragment detailsProductFragment = new DetailsProductFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.itemsFrame, detailsProductFragment,"DetailsProductFragment").addToBackStack(null).commit();
                }
            });
        }
    }
}


