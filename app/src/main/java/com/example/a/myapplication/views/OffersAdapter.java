package com.example.a.myapplication.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a.myapplication.R;
import com.example.a.myapplication.database.model.Offer;

import java.util.List;

/**
 * Created by Speed on 28/06/2018.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    private Context context;
    private List<Offer> offersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView offerTitle;
        public TextView dot;
        public TextView timestamp;
        public TextView offerContent;
        public TextView offerCenterName;

        public MyViewHolder(View view) {
            super(view);
            offerTitle = view.findViewById(R.id.offerTitle);
            offerContent = view.findViewById(R.id.offerContent);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            offerCenterName=view.findViewById(R.id.offerCenterName);
        }
    }


    public OffersAdapter(Context context, List<Offer> offersList) {
        this.context = context;
        this.offersList = offersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offersList.get(position);

        holder.offerTitle.setText(offer.getOffer_title());
        holder.offerCenterName.setText("Offered From : "+offer.getCenter_name());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText((!offer.getExpiry_date().equals(""))?"Expires in: "+offer.getExpiry_date():"Available All the time");
        holder.offerContent.setText((offer.getOffer_content()));
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

}