package app.infogen.cs.com.navbar.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.infogen.cs.com.navbar.BasicActivity;
import app.infogen.cs.com.navbar.MainActivity;
import app.infogen.cs.com.navbar.R;

/**
 * Created by Dell on 11/19/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.HolderView> {

    private ArrayList<BundleObject> bundleLst = new ArrayList<>();

    private TextView bookShow;

    public CustomAdapter(ArrayList<BundleObject> list) {
        this.bundleLst = list;
    }

    @Override
    public CustomAdapter.HolderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmyshow, parent, false);

        bookShow = (TextView) view.findViewById(R.id.book_id);
        bookShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent book = new Intent(parent.getContext(), BasicActivity.class);
                v.getContext().startActivity(book);
            }
        });
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.HolderView holder, int position) {
        holder.title_id.setText(bundleLst.get(position).getName());
        holder.cert_id.setText(bundleLst.get(position).getCert());
        holder.details_id.setText(bundleLst.get(position).getDetails());
        ArrayList<ShowPlace> showPlaceTime = bundleLst.get(position).getShowPlace();
        for (ShowPlace time : showPlaceTime) {
            holder.show_palce.setText(time.getShowPlace());
            ArrayList<String> showTime = time.getShowTimings();
            for (String show : showTime) {
                holder.show_time.setText(show);
            }
        }
        holder.imageShow.setImageResource(bundleLst.get(position).getMovieImage());
    }

    @Override
    public int getItemCount() {
        return bundleLst.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        private TextView cert_id, title_id, details_id, book_id, show_palce, show_time;
        private ImageView imageShow;


        public HolderView(View itemView) {
            super(itemView);

            cert_id = itemView.findViewById(R.id.cert_id);
            //     movieImage = itemView.findViewById(R.id.cardViewImage);
            title_id = itemView.findViewById(R.id.title_id);
            details_id = itemView.findViewById(R.id.details_id);
            book_id = itemView.findViewById(R.id.book_id);
            show_palce = itemView.findViewById(R.id.show_details);
            show_time = itemView.findViewById(R.id.show_time);
            imageShow = itemView.findViewById(R.id.imageShow);
        }


    }
}
