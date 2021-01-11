package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.ProductScannedFragmentDirections;
import com.wheretobuy.adopteuncaddie.ui.payment.PaymentFragment;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    /**
     * @Antoine FIXME
     * Choses ajoutées :
     * - Navigation (voir ligne #64 et #107, 108)
     * <p>
     * Choses à revoir :
     * - Revoir la visibilité des attributs (private en priorité, public sinon)
     * - Préférer le XML au code java, tu peux plus facilement set des propriétés sur le XML (genre le texte d'un bouton) et ca nettoie un peu le code
     * - Shared Preference, le panier est fait a partir des SP
     * - Changer quantite articles
     */

    BasketViewModel vm;
    Spinner shopList;
    FloatingActionButton addItem;
    Button payButton;

    String url = "http://vps-bfc92ef6.vps.ovh.net/index.php/products/getBy/";










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
        if (getArguments() != null) {
            itemAddedProductState = (ProductState) getArguments().getSerializable("productState");
            itemAddedNumber = getArguments().getInt("numberOfProduct", 1);
            if (itemAddedProductState != null) {
                addItemToBasket(itemAddedProductState.getProduct().getProductName(),itemAddedProductState.getProduct().getImageFrontUrl(), itemAddedNumber);
            }
        }

        Gson gson = new Gson();
        List<Articles> yourClassList = new ArrayList<Articles>();
        String json = vm.basketList.getString("Articles", "");
        Type listType = new TypeToken<ArrayList<Articles>>(){}.getType();
        yourClassList = new Gson().fromJson(json, listType);

        if (yourClassList != null) {
            for (Articles art : yourClassList) {
                System.out.println(art.getName());
                addItemToBasket(art.getName(), art.getUrl(), art.getQuantity());
            }
        }

        // addItem.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_circle_24));

        return root;
    }

    private void addItemToBasket(String itemName, String itemImageUrl, int itemAddedNumber) {

        String urlComplete = url.concat(itemName.replace(" ", "_"));

        Log.d("onVolleyResponse", urlComplete);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onVolleyResponse: ", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onVolleyResponse error: ", error.toString());
            }
        });
        queue.add(request);
        System.out.println(itemAddedNumber);
        Articles article = new Articles(itemImageUrl, itemName, itemAddedNumber, 10.9f);
        vm.getArticlesArrayList().getValue().add(article);

        SharedPreferences.Editor prefsEditor = vm.basketList.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vm.getArticlesArrayList().getValue());
        prefsEditor.putString("Articles", json);
        prefsEditor.commit();

        Log.d("addItemBasket", String.valueOf(vm.getArticlesArrayList().getValue().size()));
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        SharedPreferences basketList = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = basketList.edit();
//    }



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
//        initImageBitmaps();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_items);


        BasketRecyclerViewAdapter adapter = new BasketRecyclerViewAdapter(vm.getArticlesName(vm.getArticlesArrayList().getValue()), vm.getArticlesUrl(vm.getArticlesArrayList().getValue()), vm.getArticlesQuantity(vm.getArticlesArrayList().getValue()), vm.getArticlesPrice(vm.getArticlesArrayList().getValue()), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

//    private void initImageBitmaps() {
//
//        vm.getArticlesArrayList().getValue().add(new Articles("https://www.carrefour.fr/media/280x280/Photosite/PRODUITS_FRAIS_TRANSFORMATION/FRUITS_ET_LEGUMES/3276552308414_PHOTOSITE_20160318_163311_0.jpg", "Kiwi", 10, 5));
//
//    }


}


