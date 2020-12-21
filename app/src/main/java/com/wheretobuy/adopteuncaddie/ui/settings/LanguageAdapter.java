package com.wheretobuy.adopteuncaddie.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.utils.Utils;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView lbl_flag;
        private TextView lbl_description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lbl_flag = itemView.findViewById(R.id.lbl_identifier);
            lbl_description = itemView.findViewById(R.id.lbl_content);
        }
    }


    private List<Map.Entry<String, String>> languages;
    private Activity activity;
    public LanguageAdapter(List<Map.Entry<String, String>> languages, Activity activity) {
        this.languages = languages;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LanguageAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_simple_text, parent, false);
        LinearLayout l = v.findViewById(R.id.linearlayout_container);
        l.setOrientation(LinearLayout.HORIZONTAL);
        LanguageAdapter.MyViewHolder vh = new LanguageAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Map.Entry<String, String> language = languages.get(position); // en; English;

        holder.lbl_description.setText(language.getValue());
        holder.lbl_flag.setText(Utils.countryCodeToEmoji(language.getKey()));
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.changeLanguage)
                        .setMessage(R.string.confirmation_language_change)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Timber.d("Key: %s | Value: %s", language.getKey(), language.getValue());
                                Utils.setLocale(language.getKey(), activity);
                                Toast.makeText(activity, String.format("Locale in %s !", language.getValue()), Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return languages.size();
    }
}
