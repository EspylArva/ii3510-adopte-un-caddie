package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductScannedFragment extends Fragment {


    private ProductState state;
    private TextView lbl_productName, lbl_priceValue, lbl_nutriscoreScore;
    private Button btn_addToBasket;
    private ImageButton btn_incrementQuantity, btn_decrementQuantity;
    private ImageView img_productVisual;
    private TextView textArea_PrimaryInformations, textArea_SecondaryInformations;

    private ProductScannedViewModel vm;

    private NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(ProductScannedViewModel.class);

        navController = NavHostFragment.findNavController(this);
        state = (ProductState) getArguments().getSerializable("productState");

        View root = viewsInit(inflater, container);

        importDataFromProductState(state);

        setViewModelObservers();
        setClickListeners();


        return root;
    }

    private void importDataFromProductState(ProductState state) {

        try {
            Product product = state.getProduct();

            String productName  = product.getProductName() != null  ? product.getProductName()          : "-";
            String nutriscore   = product.getNutritionGradeTag(); // error cases managed
            String image        = product.getImageFrontUrl(); // Glide manages error case
            String packaging    = product.getPackaging() != null    ? product.getPackaging()            : "-";
            String brand        = product.getBrands() != null       ? product.getBrands()               : "-";
            int nb_ingredients  = product.getIngredients() != null  ? product.getIngredients().size()   : 0;
            String allergens    = product.getAllergens() != null    ? product.getAllergens()            : "-";
            String quantity     = product.getQuantity() != null     ? product.getQuantity()             : "-";

            lbl_productName.setText(productName);
            if(!nutriscore.equals("not-applicable"))
            {
                switch (nutriscore)
                {
                    case "a":
                    case "b":
                        lbl_nutriscoreScore.setBackgroundResource(R.drawable.low);
                        break;
                    case "c":
                        lbl_nutriscoreScore.setBackgroundResource(R.drawable.moderate);
                        break;
                    case "d":
                    case "e":
                        lbl_nutriscoreScore.setBackgroundResource(R.drawable.high);
                        break;
                }
                lbl_nutriscoreScore.setText(nutriscore.toUpperCase());
            }
            else
            {
                lbl_nutriscoreScore.setText("-");
            }
              // TODO: Get price, awaiting private API
//            lbl_priceValue.setText(); //

            String primaryText = "<b>" + getResources().getString(R.string.brand) + " : </b>" + brand + "<br/>" +
                    "<b>" + getResources().getString(R.string.nbOfIngredients) + " : </b>" + nb_ingredients + "<br/>" +
                    "<b>" + getResources().getString(R.string.packaging) + " : </b>" + packaging + "<br/>" +
                    "<b>" + getResources().getString(R.string.allergens) + " : </b>" + allergens + "<br/>" +
                    "<b>" + getResources().getString(R.string.volume) + " : </b>" + quantity;
            String secondaryText = "<b>" + getResources().getString(R.string.energy_kcal) + " : </b>" + product.getNutriments().get100g("energy-kcal") + "<br/>" +
                    "<b>" + getResources().getString(R.string.sugars) + " : </b>" + product.getNutriments().get100g("sugars") + "<br/>" +
                    "<b>" + getResources().getString(R.string.salt) + " : </b>" + product.getNutriments().get100g("salt") + "<br/>" +
                    "<b>" + getResources().getString(R.string.proteins) + " : </b>" + product.getNutriments().get100g("proteins") + "<br/>" +
                    "<b>" + getResources().getString(R.string.carbohydrates) + " : </b>" + product.getNutriments().get100g("carbohydrates") + "<br/>" +
                    "<b>" + getResources().getString(R.string.sodium) + " : </b>" + product.getNutriments().get100g("sodium");

            textArea_PrimaryInformations.setText(Html.fromHtml(primaryText, Html.FROM_HTML_MODE_LEGACY));
            textArea_SecondaryInformations.setText(Html.fromHtml(secondaryText, Html.FROM_HTML_MODE_LEGACY));

            Glide.with(this).load(image).placeholder(R.drawable.adopte_un_caddie).fitCenter().into(img_productVisual);
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void setClickListeners() {
        btn_incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.incrementQuantity();
            }
        });

        btn_decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vm.getQuantity().getValue() > 0)
                {
                    vm.decrementQuantity();
                }
            }
        });

        btn_addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add to list in basket
                if(navController.getCurrentDestination().getId() == R.id.nav_productScanned)
                {
                    ProductScannedFragmentDirections.ActionNavProductScannedToNavBasket action = ProductScannedFragmentDirections.actionNavProductScannedToNavBasket(state, vm.getQuantity().getValue());
                    navController.navigate(action);
                } // else: a navRequest has already been posted, we're just waiting for the transition.
                // Avoid the following code from being ran twice, as the fragment has technically already been changed
            }
        });
    }

    private void setViewModelObservers() {
        vm.getQuantity().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String btnText = String.format("Ajouter %s au panier", integer);
                btn_addToBasket.setText(btnText);
            }
        });
    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_product_scanned, container, false);

        lbl_productName = root.findViewById(R.id.lbl_productName);
        lbl_nutriscoreScore = root.findViewById(R.id.lbl_nutriscore_score);
        lbl_priceValue = root.findViewById(R.id.lbl_price_value);
        btn_addToBasket = root.findViewById(R.id.btn_addToBasket);
        btn_decrementQuantity = root.findViewById(R.id.btn_decreaseNumber);
        btn_incrementQuantity = root.findViewById(R.id.btn_increaseNumber);
        img_productVisual = root.findViewById(R.id.img_productFront);
        textArea_PrimaryInformations = root.findViewById(R.id.txtArea_mainInformations);
        textArea_SecondaryInformations = root.findViewById(R.id.txtArea_secondaryInformations);

        textArea_PrimaryInformations.setMovementMethod(new ScrollingMovementMethod());
        textArea_SecondaryInformations.setMovementMethod(new ScrollingMovementMethod());

        return root;
    }
}
