package com.wheretobuy.adopteuncaddie.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.components.RecyclerViewMargin;
import com.wheretobuy.adopteuncaddie.databinding.FragmentBarcodeScannerBinding;
import com.wheretobuy.adopteuncaddie.databinding.FragmentSettingsBinding;
import com.wheretobuy.adopteuncaddie.module.xml.XmlEulaParser;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.CaptureFragment;
import com.wheretobuy.adopteuncaddie.ui.basket.Article;
import com.wheretobuy.adopteuncaddie.ui.gallery.GalleryViewModel;
import com.wheretobuy.adopteuncaddie.ui.payment.PaymentViewModel;
import com.wheretobuy.adopteuncaddie.ui.shop.Shop;
import com.wheretobuy.adopteuncaddie.ui.shop.ShopViewModel;

import org.apache.commons.lang3.tuple.Pair;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class SettingsFragment extends Fragment {

    SettingsViewModel vm;
    private ShopViewModel shopViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        vm = ViewModelProviders.of(this).get(SettingsViewModel.class);
        shopViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);

        binding.setViewmodel(ViewModelProviders.of(this).get(SettingsViewModel.class));

        View root = binding.getRoot();

        initViews(binding);
        setClickListeners(binding);

        return root;
    }

    private void initViews(FragmentSettingsBinding binding){
        setupRecycler(binding.recyclerShops);
        setupRecycler(binding.recyclerPayment);
        setupRecycler(binding.recyclerLanguages);
        setupRecycler(binding.recyclerEula);


        //binding.recyclerShops.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("Auchan", "Carrefour", "Leclerc"))));
        binding.recyclerShops.setAdapter(new PaymentAdapter(getSavedShops()));
        binding.recyclerPayment.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("1234-5678-ABCD", "1234-5678-EFGH", "1234-5678-IJKL"))));

        vm.cbArrayList.add("1234-5678-ABCD");
        vm.cbArrayList.add("1234-5678-EFGH");
        vm.cbArrayList.add("1234-5678-IJKL");
        vm.saveCB();

        binding.recyclerLanguages.setAdapter(new LanguageAdapter(new ArrayList<Map.Entry<String,String>>(Arrays.asList(
                new AbstractMap.SimpleEntry<String, String>("fr","Français"), new AbstractMap.SimpleEntry<String, String>("us","English"))), getActivity()));

        try{
            binding.recyclerEula.setAdapter(new SimpleTextAdapter(XmlEulaParser.parse(getResources().openRawResource(R.raw.eula))));
        } catch (Exception e) {Timber.w(e, "Failed parsing EULA");}
    }

    private ArrayList<String> getSavedShops()
    {
        List<String> savedShops = shopViewModel.getSavedShopsName();
        if(savedShops == null)
        {
            return new ArrayList<String>(Arrays.asList(getString(R.string.no_favorite_shops)));
        }
        return (ArrayList<String>)savedShops;
    }

    private void setupRecycler(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerViewMargin decoration = new RecyclerViewMargin(4, 1);
        recyclerView.addItemDecoration(decoration);
    }

    public void setClickListeners(FragmentSettingsBinding binding)
    {
        binding.lblFavoriteShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerShops;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblPaymentList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerPayment;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblLanguages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerLanguages;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblEula.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerEula;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });

        binding.lblLanguages.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                collapseAllItemsExcept(binding, binding.recyclerLanguages);
                return true;
            }
        });
        binding.lblFavoriteShops.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                collapseAllItemsExcept(binding, binding.recyclerShops);
                return true;
            }
        });
        binding.lblPaymentList.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                collapseAllItemsExcept(binding, binding.recyclerPayment);
                return true;
            }
        });
        binding.lblEula.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                collapseAllItemsExcept(binding, binding.recyclerEula);
                return true;
            }
        });
    }

    private void collapseAllItemsExcept(FragmentSettingsBinding binding, RecyclerView recyclerView) {
        for(RecyclerView rec : new RecyclerView[]{binding.recyclerShops, binding.recyclerPayment, binding.recyclerLanguages, binding.recyclerEula}) {
            if(rec != recyclerView) {
                rec.setVisibility(View.GONE);
            }
            else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

    }
}

