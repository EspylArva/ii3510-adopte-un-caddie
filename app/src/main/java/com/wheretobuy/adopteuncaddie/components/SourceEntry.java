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
    private int entry_frag;

    public SourceEntry(Context context, Map.Entry<String, String> sourceEntry)
    {
        super(context);

        entry_title = sourceEntry.getKey();
        entry_imgSrc = R.mipmap.sources;
        entry_link = sourceEntry.getValue();
        entry_frag = -1;

        init(context);
        setButtonsOnClickListener();
    }

//    public SourceEntry(Context context, AttributeSet attrs)
//    {
//        super(context, attrs);
//
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs, R.styleable.AppEntry, 0, 0);
//
//        try
//        {
//            entry_title = a.getString(R.styleable.AppEntry_title);
//            entry_imgSrc = a.getResourceId(R.styleable.AppEntry_imgSrc, R.mipmap.ic_launcher_round);
//            entry_link = a.getString(R.styleable.AppEntry_url);
//            entry_frag = a.getResourceId(R.styleable.AppEntry_frag, -1);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//        }
//        finally {
//            a.recycle();
//        }
//
//        init(context);
//        setButtonsOnClickListener();
//    }

    private void setButtonsOnClickListener() {
        btn_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Link: %s  | Frag: %s", entry_link, entry_frag);
                if(entry_frag != -1)
                {
                    // FIXME
//                    try {
//                        AppCompatActivity activity = (AppCompatActivity)getContext();
//                        Fragment newFragment = null;
//                        switch (entry_frag)
//                        {
//                            case R.id.nav_course1:
//                                newFragment = new fr.isep.ii3510.ui.course1.Course1Fragment();
//                                break;
//                            case R.id.nav_assignment1:
//                                newFragment = new fr.isep.ii3510.ui.assignment1.Assignment1Fragment();
//                                break;
//                            case R.id.nav_course2:
//                                newFragment = new fr.isep.ii3510.ui.course2.Course2Fragment();
//                                break;
//                            case R.id.nav_course3:
//                                newFragment = new fr.isep.ii3510.ui.course3.Course3Fragment();
//                                break;
//                            case R.id.nav_assignment2:
//                                newFragment = new fr.isep.ii3510.ui.assignment2.Assignment2Fragment();
//                                break;
//                            case R.id.nav_sources:
//                                newFragment = new fr.isep.ii3510.ui.sources.SourcesFragment();
//                                break;
//                            default:
//                                newFragment = new fr.isep.ii3510.ui.mainmenu.MainMenuFragment();
//                                break;

//                        }

//                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.nav_host_fragment, newFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    }
//                    catch(Exception e) { e.printStackTrace(); }
                }
                else if(entry_link != null && !entry_link.equals(""))
                {
                    Toast.makeText(getContext(), entry_link, Toast.LENGTH_SHORT).show();
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry_link));
                        getContext().startActivity(browserIntent);
                    }
                    catch (Exception e)
                    {
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

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.source_entry, this);
        img_icon = root.findViewById(R.id.img_entry_icon);
        lbl_title = root.findViewById(R.id.lbl_entry_title);
        btn_goto = root.findViewById(R.id.btn_go_next);

        lbl_title.setSelected(true);
        img_icon.setImageResource(entry_imgSrc);
        img_icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        lbl_title.setText(entry_title);
        btn_goto.setImageResource(R.mipmap.right_arrow);
        btn_goto.setAdjustViewBounds(true);
        btn_goto.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}