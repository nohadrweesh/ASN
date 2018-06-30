package com.example.a.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {


    private Context mCtx;
    private List<Center> centerList;

    public CenterAdapter(Context mCtx, List<Center> centerList) {
        this.mCtx = mCtx;
        this.centerList = centerList;
    }

    @Override
    public CenterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.center_list, null);
        return new CenterViewHolder(view);
    }

    //This function is called every time an item is added to the list.
    @Override
    public void onBindViewHolder(CenterAdapter.CenterViewHolder holder, final int position) {
        final Center center = centerList.get(position);
        holder.center_id.setText(center.getId());
        holder.center_name.setText(center.getName());
        holder.center_location.setText(center.getLocation());
        holder.select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx,"You Select "+centerList.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mCtx, SelecedCenterActivity.class);
                //To let the new activity know which item we clicked on we use :
                intent.putExtra("center_id", centerList.get(position).getId());
                intent.putExtra("center_name", centerList.get(position).getName());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return centerList.size();
    }



    class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView center_id, center_name, center_location;
        Button select_btn;
        public RelativeLayout R1;

        public CenterViewHolder(View itemView) {
            super(itemView);

            center_id = itemView.findViewById(R.id.center_id);
            center_name = itemView.findViewById(R.id.center_name);
            center_location = itemView.findViewById(R.id.center_location);
            R1 = (RelativeLayout)itemView.findViewById(R.id.center_relative_layout);
            select_btn = itemView.findViewById(R.id.selectCenterBtn);

        }
    }


}