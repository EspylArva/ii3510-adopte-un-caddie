package com.wheretobuy.adopteuncaddie.ui.sources;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.components.SourceEntry;
import com.wheretobuy.adopteuncaddie.module.xml.XmlSourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;



public class SourcesFragment extends Fragment {

    private LinearLayout layout_sources;
    private SourcesViewModel sourcesViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sourcesViewModel = ViewModelProviders.of(this).get(SourcesViewModel.class);
        if (container != null) {
            container.removeAllViews();
        }
        root = init(inflater, container);
        try {
            importSourcesFromXml();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        return root;
    }

    private void importSourcesFromXml() throws IOException, XmlPullParserException {
        InputStream is = getResources().openRawResource(R.raw.sources);
        List<XmlSourceParser.DevelopmentSource> sources = XmlSourceParser.parse(is);

        for(XmlSourceParser.DevelopmentSource category : sources)
        {
            TextView cat = new TextView(getContext());
            String title = category.getCategory(); if(category.getSubcategory() != null) { title += " - " + category.getSubcategory(); }
            cat.setText(title); cat.setTypeface(Typeface.DEFAULT_BOLD);
            layout_sources.addView(cat);

            for(Map.Entry<String, String> s : category.getLinks())
            {
                layout_sources.addView(new SourceEntry(getContext(), s));
            }
        }
    }

    private View init(LayoutInflater inflater, ViewGroup container) { // Init views here (AHK: findId)
        View root = inflater.inflate(R.layout.fragment_sources, container, false);
        layout_sources = root.findViewById(R.id.layout_sources);

        return root;
    }
}
