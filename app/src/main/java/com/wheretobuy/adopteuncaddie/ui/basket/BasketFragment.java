package com.wheretobuy.adopteuncaddie.ui.basket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.ProductScannedFragmentDirections;
import com.wheretobuy.adopteuncaddie.ui.payment.PaymentFragment;

public class BasketFragment extends Fragment {

    BasketViewModel vm;
    Spinner shopList;
    FloatingActionButton addItem;
    Button payButton;

    // Ajout par TK
    // Gère la transmission d'article de ProductScannedFragment -> BasketFragment
    private int itemAddedNumber;                // Nombre d'item à ajouter
    private ProductState itemAddedProductState; // Fiche OpenFoodFacts de l'article
    // Gère la redirection des fragments via Navigation
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BasketViewModel.class);

        navController = NavHostFragment.findNavController(this);

        View root = viewsInit(inflater, container);

        setViewModelObservers();
        setClickListeners();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.shop_names,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopList.setAdapter(adapter);

        // get the item if the fragment request comes from ProductScannedFragment
        if(getArguments() != null)
        {
            itemAddedProductState = (ProductState) getArguments().getSerializable("productState");
            itemAddedNumber = getArguments().getInt("numberOfProduct", 1);
            addItemToBasket(itemAddedProductState, itemAddedNumber);
        }

        // addItem.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_circle_24));

        return root;
    }

    private void addItemToBasket(ProductState itemAddedProductState, int itemAddedNumber) {
        // FIXME
        vm.getmImageUrls().add(itemAddedProductState.getProduct().getImageFrontUrl());
        vm.getmItemNames().add(itemAddedProductState.getProduct().getProductName());
    }

    private void setClickListeners() {
//        btn_<ID_HERE>.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: DO STUFF
//            }
//        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = BasketFragmentDirections.actionNavBasketToNavBarcodeScanner();
                navController.navigate(action);
            }
        });

        shopList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedShop = shopList.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = BasketFragmentDirections.actionNavBasketToNavPayment();
                navController.navigate(action);

//                Fragment payFragment = new PaymentFragment();  // https://stackoverflow.com/questions/40871451/how-implement-a-next-button-in-a-fragment
//                FragmentTransaction transaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment, payFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
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

    private void initImageBitmaps() {

        vm.getmImageUrls().add("https://www.carrefour.fr/media/280x280/Photosite/PRODUITS_FRAIS_TRANSFORMATION/FRUITS_ET_LEGUMES/3276552308414_PHOTOSITE_20160318_163311_0.jpg");
        vm.getmItemNames().add("Kiwi");

        vm.getmImageUrls().add("https://www.carrefour.fr/media/280x280/Photosite/PRODUITS_FRAIS_TRANSFORMATION/FRUITS_ET_LEGUMES/3000001032670_PHOTOSITE_20160318_163819_0.jpg");
        vm.getmItemNames().add("Citron");
    }


}


