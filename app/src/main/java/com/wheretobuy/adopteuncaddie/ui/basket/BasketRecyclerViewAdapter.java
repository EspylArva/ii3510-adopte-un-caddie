package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wheretobuy.adopteuncaddie.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
//import timber.log.Timber;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "BasketRecyclerViewAdapt";


    private Context mContext;
//    private BasketViewModel vm;

    private MutableLiveData<ArrayList<Article>> articles;

    public BasketRecyclerViewAdapter(MutableLiveData<ArrayList<Article>> articles, android.content.Context context) {
        this.articles = articles;
        mContext = context;
//        vm = ViewModelProviders.of((FragmentActivity) context).get(BasketViewModel.class);
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

        Glide.with(mContext)
                .asBitmap()
                .load(articles.getValue().get(position).getUrl())
                .into(holder.image);
        holder.itemName.setText(articles.getValue().get(position).getName());
        holder.itemQuantity.setText(String.valueOf(articles.getValue().get(position).getQuantity()));
        holder.itemPrice.setText(String.format("%.2f", articles.getValue().get(position).getPrice()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             Toast.makeText(mContext, mItemNames.get(position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder itemCountPopup = new AlertDialog.Builder(mContext);
                itemCountPopup.setTitle("Ajout et suppression de produits");
                itemCountPopup.setPositiveButton("+", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        articles.getValue().get(position).setQuantity(articles.getValue().get(position).getQuantity() + 1);
//                        articles.setValue(articles);
                        articles.setValue(articles.getValue());
//                        vm.setArticlesArrayList(articles.getValue());
//                        vm.saveBasket();
//                        vm.setNewQuantityForArticle(position, articles.get(position).getQuantity() + 1);
                    }
                });
                itemCountPopup.setNegativeButton("-", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (articles.getValue().get(position).getQuantity() == 1){
//                            vm.deleteItem(articles.getValue().get(position));
                            articles.getValue().remove(articles.getValue().get(position));
                        } else {
                            articles.getValue().get(position).setQuantity(articles.getValue().get(position).getQuantity() - 1);
//                            vm.setArticlesArrayList(articles.getValue());
//                            vm.saveBasket();

//                            vm.setNewQuantityForArticle(position, articles.get(position).getQuantity() - 1);
                        }
                        articles.setValue(articles.getValue());
                    }

                });
                itemCountPopup.setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        articles.getValue().remove(articles.getValue().get(position));
                        articles.setValue(articles.getValue());
//                        vm.deleteItem(articles.getValue().get(position));
                    }
                });

                itemCountPopup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.getValue().size();
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
