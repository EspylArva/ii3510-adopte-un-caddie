package com.wheretobuy.adopteuncaddie.components;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.wheretobuy.adopteuncaddie.R;

import java.util.Map;

import timber.log.Timber;


public class SourceEntry extends ConstraintLayout
{
    private ImageView img_icon;
    private TextView lbl_title;
    private ImageButton btn_goto;

    private String entry_title;
    private int entry_imgSrc;
    private String entry_link;

    public SourceEntry(Context context, Map.Entry<String, String> sourceEntry)
    {
        super(context);

        entry_title = sourceEntry.getKey();
        entry_link = sourceEntry.getValue();
        entry_imgSrc = R.drawable.sources;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(inflater);
        setButtonsOnClickListener();
    }

//    public SourceEntry(Context context, @Nullable AttributeSet attrs)
//    {
//        super(context, attrs);
//
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AppEntry, 0, 0);
//
//        try
//        {
//            entry_title = a.getString(R.styleable.AppEntry_title);
//            entry_imgSrc = a.getResourceId(R.styleable.AppEntry_imgSrc, R.mipmap.ic_launcher_round);
//            entry_link = a.getString(R.styleable.AppEntry_url);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//        }
//        finally {
//            a.recycle();
//        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        init(inflater);
//        setButtonsOnClickListener();
//    }

    private void setButtonsOnClickListener() {
        btn_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Link: %s ", entry_link);
                if(entry_link != null && !entry_link.equals(""))
                {
                    Toast.makeText(getContext(), entry_link, Toast.LENGTH_SHORT).show();
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry_link));
                        getContext().startActivity(browserIntent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Entry link", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(LayoutInflater inflater)
    {
        View root = inflater.inflate(R.layout.source_entry, this, true);
        img_icon = root.findViewById(R.id.img_entry_icon);
        lbl_title = root.findViewById(R.id.lbl_entry_title);
        btn_goto = root.findViewById(R.id.btn_go_next);

//        lbl_title.setSelected(true);
//        img_icon.setImageResource(entry_imgSrc);
        img_icon.setAdjustViewBounds(true);
        lbl_title.setText(entry_title);
//        btn_goto.setImageResource(R.mipmap.right_arrow);
//        btn_goto.setAdjustViewBounds(true);
        btn_goto.setAdjustViewBounds(true);
    }
}