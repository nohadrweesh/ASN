package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.ViewHolder> {
    private View.OnClickListener mOnClickListener;

    private List<AdvertisementClass> ads;
    private Context context;
    private RecyclerView mRecyclerView;

    public RecyclerViewAdapterClass(List<AdvertisementClass> ads, Context context) {
        this.ads = ads;
        this.context = context;
    }

    @NonNull
    @Override
    // Called whenever a ViewHolder instance is created
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);

        mOnClickListener  = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                AdvertisementClass item = ads.get(itemPosition);
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, SingleAdActivity.class);
                i.putExtra("adPosition", itemPosition);
                context.startActivity(i);
            }
        };
        v.setOnClickListener(mOnClickListener);
        return new ViewHolder(v);
    }

    @Override
    // Called after calling the above onCreateViewHolder
    // Binds the Recycler View with data
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdvertisementClass ad = ads.get(position);

        holder.adTitle.setText(ad.getTitle());
        holder.adDesc.setText(ad.getDescription());
        holder.ownerName.setText(ad.getOwnerName());
//        Log.d("Show Ad Img",ad.getImgURL());
        Picasso.with(context).load(ad.getImgURL()).into(holder.adImg);
        if(!ad.getExpirationDate().equals("0000-00-00")){
            holder.expDateValue.setText(ad.getExpirationDate());
            holder.expDate.setVisibility(View.VISIBLE);
        }
        else{
            holder.expDate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView adImg;
        public TextView adTitle, adDesc, expDateValue, ownerName;
        public LinearLayout expDate;

        public ViewHolder(View itemView) {
            super(itemView);

            adImg = (ImageView) itemView.findViewById(R.id.adImg);
            adTitle = (TextView) itemView.findViewById(R.id.adTitle);
            adDesc = (TextView) itemView.findViewById(R.id.adDesc);
            expDateValue = (TextView) itemView.findViewById(R.id.expDateValue);
            ownerName = (TextView) itemView.findViewById(R.id.adOwner);
            expDate = (LinearLayout) itemView.findViewById(R.id.expDate);
        }
    }
}
