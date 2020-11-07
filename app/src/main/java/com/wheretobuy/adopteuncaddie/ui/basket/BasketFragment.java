package com.wheretobuy.adopteuncaddie.ui.basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;

public class BasketFragment  extends Fragment {

    BasketViewModel vm;
    Spinner shopList;
    FloatingActionButton addItem;

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


        private void setViewModelObservers ()
        {
//        vm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String variable) {
            //TODO: DO STUFF
//        }
//    });

        }

        private View viewsInit (LayoutInflater inflater, ViewGroup container){
            View root = inflater.inflate(R.layout.fragment_basket, container, false);
            shopList = root.findViewById(R.id.shop_list);
            addItem = root.findViewById(R.id.fab);
            return root;
        }
    }


