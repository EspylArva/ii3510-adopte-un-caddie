package com.wheretobuy.adopteuncaddie.ui.basket;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.ProductScannedFragmentArgs;
import com.wheretobuy.adopteuncaddie.ui.gallery.GalleryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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


    private static final float DEFAULT_PRICE = 10.9f;

    private BasketViewModel vm;
    private GalleryViewModel galleryViewModel;
    private Spinner shopList;
    private FloatingActionButton addItem;
    private Button payButton;
    private Button deleteAll;

    private final String URL = "http://vps-bfc92ef6.vps.ovh.net/index.php/products/getBy";
    String getClosestUrl = "http://vps-bfc92ef6.vps.ovh.net/index.php/shops/getClosest?";

    private RecyclerView recyclerView;


    private ArrayList<Integer> shopUIDs;

    // Ajout par TK
    // Gère la transmission d'article de ProductScannedFragment -> BasketFragment
    private int itemAddedNumber;                // Nombre d'item à ajouter
    private ProductState itemAddedProductState; // Fiche OpenFoodFacts de l'article
    // Gère la redirection des fragments via Navigation
    private NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BasketViewModel.class);
        galleryViewModel =ViewModelProviders.of(this).get(GalleryViewModel.class);
        navController = NavHostFragment.findNavController(this);

        View root = viewsInit(inflater, container);
        shopUIDs = new ArrayList<>();

        getUserLocation();
        setupNearestShops();
        computeFullPrice();

        setViewModelObservers();
        setClickListeners();

//        else {
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
        if(getArguments() != null && getArguments().getSerializable("productState") != null) {
            Timber.d("CurrentBackStackEntry: %s", navController.getGraph().getStartDestination() );
            ProductScannedFragmentArgs args = ProductScannedFragmentArgs.fromBundle(getArguments());
            itemAddedProductState = args.getProductState();
            itemAddedNumber = getArguments().getInt("numberOfProduct", 1);
            addItemToBasket(itemAddedProductState.getProduct().getProductName(), itemAddedProductState.getProduct().getImageFrontUrl(), itemAddedNumber);
        }
//        }
        return root;
    }

    private void computeFullPrice()
    {
        float sum = vm.getTotalPrice(vm.getArticlesArrayList().getValue());
        DecimalFormat df = new DecimalFormat("#.##");

        payButton.setText(getString(R.string.pay)+" ("+df.format(sum)+" €)");
    }

    private void setupNearestShops()
    {
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }
        });
        galleryViewModel.getLastLocation().observe(getViewLifecycleOwner(), new Observer<Location>()
        {
            @Override
            public void onChanged(Location location) {
                if(location != null){
                    fetchClosest(location);
                }
            }
        });
    }

    private void fetchClosest(Location location)
    {
        String urlComplete = getClosestUrl +"lat="+location.getLatitude()+"&long="+location.getLongitude()+"&radius=5";

        Log.d("onVolleyResponse", urlComplete);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("onVolleyResponse: ", response.getJSONArray("results").toString());
                    Toast.makeText(getContext(),response.getJSONArray("results").length()+" "+getString(R.string.shops_found),Toast.LENGTH_LONG).show();
                    updateShopListSpinner(response.getJSONArray("results"));
                }
                catch (JSONException e)
                {
                    Toast.makeText(getContext(),getString(R.string.shops_not_found),Toast.LENGTH_LONG).show();
                    Log.d("onVolleyResponse: ", "Error :"+e.getMessage());
                }
                //Log.d("onVolleyResponse: ", response.getJSONArray("results").toString());
                //Toast.makeText(getContext(),response.get.length()+" shops found ",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),getString(R.string.shops_not_found),Toast.LENGTH_LONG).show();
                Log.d("onVolleyResponse error: ", error.toString());
            }
        });
        queue.add(request);
    }

    private void updateShopListSpinner(JSONArray shops) throws JSONException
    {
        ArrayList<String> shopNames = new ArrayList<>();
        for (int i = 0; i < shops.length(); i++)
        {
            shopNames.add(shops.getJSONObject(i).getString("name"));
            shopUIDs.add(Integer.parseInt(shops.getJSONObject(i).getString("shop_uid")));
        }

        shopNames.set(0,shopNames.get(0)+"("+getString(R.string.nearest)+")");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, shopNames);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        shopList.setAdapter(spinnerArrayAdapter);
    }

/*    private void addItemToBasket(String itemName, String itemImageUrl, int itemAddedNumber) {

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
        Timber.d(String.valueOf(vm.getArticlesArrayList().getValue().size()));
    }*/

    private void addItemToBasket(String itemName, String itemImageUrl, int itemAddedNumber)
    {
        int shopUID = vm.basketList.getInt("Shop_UID",0);
        String urlComplete = URL+"?name="+itemName.replace(" ", "_")+"&shop_uid="+shopUID;

        Log.d("onVolleyResponse", urlComplete);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    float price = Float.parseFloat(response.getJSONArray("results").getJSONObject(0).getString("price"));
                    Log.d("onVolleyResponse: ","Found price "+price);
                    finalizeProductToBasket(itemImageUrl,itemName,itemAddedNumber,price);
                }
                catch (Exception e)
                {
                    Log.d("onVolleyResponse: ", "Error :"+e.getMessage());
                    finalizeProductToBasket(itemImageUrl,itemName,itemAddedNumber,DEFAULT_PRICE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onVolleyResponse error: ", error.toString());
                finalizeProductToBasket(itemImageUrl,itemName,itemAddedNumber,DEFAULT_PRICE);
            }
        });
        queue.add(request);
        System.out.println(itemAddedNumber);
    }

    private void finalizeProductToBasket(String itemImageUrl,String itemName,int itemAddedNumber,float price)
    {
        Article article = new Article(itemImageUrl, itemName, itemAddedNumber, price);
        vm.addItem(article);

//        vm.saveBasket();
//        SharedPreferences.Editor prefsEditor = vm.basketList.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(vm.getArticlesArrayList().getValue());
//        prefsEditor.putString("Articles", json);
//        prefsEditor.commit();

        Log.d("addItemBasket", String.valueOf(vm.getArticlesArrayList().getValue().size()));
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
                if(position < shopUIDs.size())
                {
                    SharedPreferences.Editor prefsEditor = vm.basketList.edit();
                    prefsEditor.putInt("Shop_UID", shopUIDs.get(position));
                    prefsEditor.commit();
                }
                computeFullPrice();
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

    private void getUserLocation()
    {
        if (!checkPermissions()) {
            requestPermissions();
        } else { // add null checker
            Timber.d("Requesting position");
            // Permission granted. Now check if module is activated
            // Checking if GPS module is activated
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
//                                GeolocationService serv = new GeolocationService(getActivity());
//                                serv.getLastLocation();
                        galleryViewModel.fetchLocation();
                    }
                    catch (ApiException exception) {
                        if(exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        getActivity(),
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                        }
                    }
                }
            });

        }
    }

    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    getView(),
                    "Location permission is needed for core functionality",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    34);
                        }
                    })
                    .show();
        } else {
            Timber.i("Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    34);
        }
    }
}


