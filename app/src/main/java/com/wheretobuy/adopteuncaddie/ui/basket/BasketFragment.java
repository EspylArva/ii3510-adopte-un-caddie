package com.wheretobuy.adopteuncaddie.ui.basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;

public class BasketFragment extends Fragment {

    BasketViewModel vm;
    Spinner shopList;
    FloatingActionButton addItem;
    Button payButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BasketViewModel.class);

        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.shop_names,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopList.setAdapter(adapter);


        // addItem.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_circle_24));

        return root;
    }

    private void setClickListeners() {
//        btn_<ID_HERE>.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: DO STUFF
//            }
//        });
        shopList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedShop = shopList.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setViewModelObservers() {
//        vm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String variable) {
        //TODO: DO STUFF
//        }
//    });

    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        shopList = root.findViewById(R.id.shop_list);
        addItem = root.findViewById(R.id.fab);
        payButton = root.findViewById(R.id.pay_button);
        payButton.setText("Payer");
        initImageBitmaps();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_items);
        BasketRecyclerViewAdapter adapter = new BasketRecyclerViewAdapter(vm.getmItemNames(), vm.getmImageUrls(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    private void initImageBitmaps(){

        vm.getmImageUrls().add("https://www.carrefour.fr/media/280x280/Photosite/PRODUITS_FRAIS_TRANSFORMATION/FRUITS_ET_LEGUMES/3276552308414_PHOTOSITE_20160318_163311_0.jpg");
        vm.getmItemNames().add("Kiwi");

        vm.getmImageUrls().add("https://www.carrefour.fr/media/280x280/Photosite/PRODUITS_FRAIS_TRANSFORMATION/FRUITS_ET_LEGUMES/3000001032670_PHOTOSITE_20160318_163819_0.jpg");
        vm.getmItemNames().add("Citron");
    }


}


