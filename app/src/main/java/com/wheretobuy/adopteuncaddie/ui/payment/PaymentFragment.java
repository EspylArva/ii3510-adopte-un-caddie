package com.wheretobuy.adopteuncaddie.ui.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.ui.basket.Article;
import com.wheretobuy.adopteuncaddie.ui.basket.BasketViewModel;


import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PaymentFragment extends Fragment {


    private PaymentViewModel vm;
    Toolbar toolbar;
    private Button paymentFragmentButton;
    private TextView totalPriceText;
    private Spinner cardList;

    private static DecimalFormat df = new DecimalFormat("0.00");


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("CREATE");
        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(PaymentViewModel.class);
        View root = viewsInit(inflater, container);

        if (vm.getArticlesArrayList().getValue().size() == 0) {
            String json = vm.basketList.getString("Articles", "");
            Type listType = new TypeToken<ArrayList<Article>>() {
            }.getType();
            List<Article> yourClassList = new Gson().fromJson(json, listType);
            if (yourClassList != null) {
                for (Article art : yourClassList) {
                    Timber.d(art.getName());
                    getTotalPrice(yourClassList);
                }
            }
        }

        if (vm.getGetCbArrayList().size() == 0) {
            String json = vm.cbList.getString("CB", "");
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> yourClassList = new Gson().fromJson(json, listType);
            if (yourClassList != null) {
                for (String cb : yourClassList) {
                    vm.cbArrayList.add(cb);
                }
            }
        }

        setViewModelObservers();
        setClickListeners();
        setSpinnerCb(vm.getGetCbArrayList());


        return root;
    }

    private void setClickListeners() {
//        btn_<ID_HERE>.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: DO STUFF
//            }
//        });
    }

    private void setViewModelObservers() {
//        vm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String variable) {
        //TODO: DO STUFF
//        }
//    });

    }

    private void getTotalPrice(List<Article> articlesList) {
        double totalPrice = 0;
        for (Article article : articlesList){
            totalPrice += article.getPrice() * article.getQuantity();
        }
        totalPriceText.setText("Le prix total de votre panier est de " + String.valueOf(df.format(totalPrice)) + "€");
    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        toolbar = root.findViewById(R.id.toolbar);

        paymentFragmentButton = root.findViewById(R.id.paymentFragmentButton);
        cardList = root.findViewById(R.id.cardList);
        totalPriceText = root.findViewById(R.id.totalPriceText);
        paymentFragmentButton.setText("Payer");


        return root;
    }

    private void setSpinnerCb(ArrayList<String> items){
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items);
        cardList.setAdapter(adapter);
    }
}