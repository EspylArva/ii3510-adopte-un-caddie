package com.wheretobuy.adopteuncaddie.ui.settings;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretobuy.adopteuncaddie.R;

import java.util.List;
import java.util.Map;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView lbl_identifier;
        private TextView lbl_content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lbl_identifier = itemView.findViewById(R.id.lbl_identifier);
            lbl_content = itemView.findViewById(R.id.lbl_content);

            lbl_identifier.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }


    List<Map.Entry<String, String>> list;
    public SimpleTextAdapter(List<Map.Entry<String, String>> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_simple_text, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Map.Entry<String, String> entry = list.get(position);
        holder.lbl_identifier.setText(entry.getKey());
        holder.lbl_content.setText(entry.getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}