package com.wheretobuy.adopteuncaddie.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.ProductScannedFragmentArgs;
import com.wheretobuy.adopteuncaddie.ui.basket.Article;
import com.wheretobuy.adopteuncaddie.ui.basket.BasketViewModel;


import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PaymentFragment extends Fragment {


    private PaymentViewModel vm;
    Toolbar toolbar;
    private Button paymentFragmentButton;
    private TextView totalPriceText;
    private Spinner cardList;
    private BasketViewModel bVM;

    private static DecimalFormat df = new DecimalFormat("0.00");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(PaymentViewModel.class);


        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        if(getArguments() != null ) {
            float price = getArguments().getFloat("basket_total_price", 0f);
            Timber.d("Price: %s", price);
            vm.setPrice(price);
            bVM = (BasketViewModel) getArguments().get("viewmodel");
        }





        return root;
    }

    private void setClickListeners() {
        paymentFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bVM.emptyBasket();
                Toast.makeText(getContext(), R.string.payment_complete, Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigateUp();
            }
        });

    }

    private void setViewModelObservers() {
        vm.getPrice().observe(getViewLifecycleOwner(), new Observer<Float>() {
        @Override
        public void onChanged(@Nullable Float price) {
            String text = getString(R.string.empty_basket);
            if(price != 0f)
            {
                text = String.format(getResources().getString(R.string.payment_price), price);
                String json = vm.cbList.getString("CB", "");
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> yourClassList = new Gson().fromJson(json, listType);
                if (yourClassList != null) {
                    for (String cb : yourClassList) {
                        vm.cbArrayList.add(cb);
                    }
                }
                setSpinnerCb(vm.getGetCbArrayList());
            }
            totalPriceText.setText(text);
        }
    });

    }

//    private void getTotalPrice(List<Article> articlesList) {
//        double totalPrice = 0;
//        for (Article article : articlesList){
//            totalPrice += article.getPrice() * article.getQuantity();
//        }
//        totalPriceText.setText("Le prix total de votre panier est de " + String.valueOf(df.format(totalPrice)) + "€");
//    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        toolbar = root.findViewById(R.id.toolbar);

        paymentFragmentButton = root.findViewById(R.id.paymentFragmentButton);
        cardList = root.findViewById(R.id.cardList);
        totalPriceText = root.findViewById(R.id.totalPriceText);

        return root;
    }

    private void setSpinnerCb(ArrayList<String> items){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items);
        cardList.setAdapter(adapter);
    }
}