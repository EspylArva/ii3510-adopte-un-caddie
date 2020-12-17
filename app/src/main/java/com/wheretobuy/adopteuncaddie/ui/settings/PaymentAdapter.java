package com.wheretobuy.adopteuncaddie.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretobuy.adopteuncaddie.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img_illustration;
        private TextView lbl_description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lbl_description = itemView.findViewById(R.id.lbl_description);
            img_illustration = itemView.findViewById(R.id.img_illustration);
        }
    }


    private List<String> payments;
    public PaymentAdapter(List<String> payment) {
        payments = payment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_settings, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //FIXME
//        holder.textView.setText(dataset.get(position).get...());

        // get Item #position
        // Call Glide
        holder.lbl_description.setText(payments.get(position));
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }
}