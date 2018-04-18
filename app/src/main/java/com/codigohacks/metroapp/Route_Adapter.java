package com.codigohacks.metroapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by k_y on 08/04/18.
 */

public class Route_Adapter extends RecyclerView.Adapter<Route_Adapter.ViewHolder> {
    private List<Route_items> listItems;
    private Context context;

    public Route_Adapter(List<Route_items> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stations_layout,parent,false);
        return  new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Route_items listItem = listItems.get(position);
        if(listItem.getRoute().equals(listItem.getInterchanges()))
        {
            holder.textViewhead.setText("Interchange");
            holder.textViewName.setText(listItem.getRoute());
            holder.imageViewstation.setImageResource(R.drawable.iterchange);
        }
        else
        {
            holder.textViewhead.setText("Station");
            holder.textViewName.setText(listItem.getRoute());
            if(position==0)
            {
                holder.imageViewstation.setImageResource(R.drawable.source);
            }
            else if(position==listItems.size()-1)
            {
                holder.imageViewstation.setImageResource(R.drawable.destination);
            }
            else {
                holder.imageViewstation.setImageResource(R.drawable.station4);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewhead;
        public TextView textViewName;
        public ImageView imageViewstation;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewhead=(TextView)itemView.findViewById(R.id.station_head);
            textViewName=(TextView)itemView.findViewById(R.id.station_name);
            imageViewstation=(ImageView) itemView.findViewById(R.id.station_image);
        }
    }
}
