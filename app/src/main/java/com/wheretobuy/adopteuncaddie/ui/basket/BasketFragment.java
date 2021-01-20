package com.wheretobuy.adopteuncaddie.ui.basket;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
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
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.ProductScannedFragmentArgs;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

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

    private BasketViewModel vm;
    private Spinner shopList;
    private FloatingActionButton addItem;
    private Button payButton;
    private Button deleteAll;

    private final String URL = "http://vps-bfc92ef6.vps.ovh.net/index.php/products/getBy/";

    private RecyclerView recyclerView;


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

        if(getArguments() != null && getArguments().getSerializable("productState") != null)
        {
            Timber.d("CurrentBackStackEntry: %s", navController.getGraph().getStartDestination() );
            Timber.d("Adding product from passed arguments");
            ProductScannedFragmentArgs args = ProductScannedFragmentArgs.fromBundle(getArguments());
            itemAddedProductState = args.getProductState();
            itemAddedNumber = getArguments().getInt("numberOfProduct", 1);
            addItemToBasket(itemAddedProductState.getProduct().getProductName(), itemAddedProductState.getProduct().getImageFrontUrl(), itemAddedNumber);
        }
        else
        {
            if(vm.getArticlesArrayList().getValue().size() == 0)
            {
                String json = vm.basketList.getString("Articles", "");
                Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
                List<Article> yourClassList = new Gson().fromJson(json, listType);
                if (yourClassList != null) {
                    for (Article art : yourClassList) {
                        Timber.d(art.getName());
                        addItemToBasket(art.getName(), art.getUrl(), art.getQuantity());
                    }
                }
            }
        }
        return root;
    }

    private void addItemToBasket(String itemName, String itemImageUrl, int itemAddedNumber) {

        String urlComplete = URL.concat(itemName.replace(" ", "_"));

        Timber.d(urlComplete);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Timber.d(response.toString());
            }
        }, error -> Timber.d(error.toString()));
        queue.add(request);
        Article article = new Article(itemImageUrl, itemName, itemAddedNumber, 10.9f);
        vm.addItem(article);

        vm.saveBasket();

        Timber.d(String.valueOf(vm.getArticlesArrayList().getValue().size()));
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
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                vm.emptyBasket();
            }
        });

    }


    private void setViewModelObservers() {
        vm.getArticlesArrayList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Article>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Article> a) {
                BasketRecyclerViewAdapter adapter = new BasketRecyclerViewAdapter(vm.getArticlesArrayList(), getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }


    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        shopList = root.findViewById(R.id.shop_list);
        addItem = root.findViewById(R.id.fab);
        payButton = root.findViewById(R.id.pay_button);
        deleteAll = root.findViewById(R.id.deleteAll_button);
        recyclerView = root.findViewById(R.id.recycler_items);

        ArrayAdapter<CharSequence> shopAdapters = ArrayAdapter.createFromResource(getContext(),
                R.array.shop_names,
                android.R.layout.simple_spinner_item);
        shopAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopList.setAdapter(shopAdapters);

        BasketRecyclerViewAdapter adapter = new BasketRecyclerViewAdapter(vm.getArticlesArrayList(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }
}


