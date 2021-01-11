package com.wheretobuy.adopteuncaddie.ui.basket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wheretobuy.adopteuncaddie.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
//import timber.log.Timber;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "BasketRecyclerViewAdapt";

    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<Integer> mItemQuantity = new ArrayList<>();
    private ArrayList<Float> mItemPrices = new ArrayList<>();
    private Context mContext;

    public BasketRecyclerViewAdapter(ArrayList<String> itemNames, ArrayList<String> images, ArrayList<Integer> quantity, ArrayList<Float> prices,  android.content.Context context) {
        mItemNames = itemNames;
        mImages = images;
        mItemQuantity = quantity;
        mItemPrices = prices;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_basket, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

//        Timber.d("onBindViewHolder: called.");


        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        holder.itemName.setText(mItemNames.get(position));
        holder.itemQuantity.setText(mItemQuantity.get(position).toString());
        holder.itemPrice.setText(String.format("%.2f", mItemPrices.get(position)));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Timber.d("OnClick clicked on: " + mItemNames.get(position));

                Toast.makeText(mContext, mItemNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView itemName;
        TextView itemQuantity;
        TextView itemPrice;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemPrice = itemView.findViewById(R.id.item_price);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
