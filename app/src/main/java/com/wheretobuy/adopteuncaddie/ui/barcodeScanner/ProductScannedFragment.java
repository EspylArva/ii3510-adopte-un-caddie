package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;

public class ProductScannedFragment extends Fragment {


    private ProductState state;
    private TextView lbl_productName, lbl_priceValue, lbl_nutriscoreScore;
    private Button btn_addToBasket;
    private ImageButton btn_incrementQuantity, btn_decrementQuantity;
    private ImageView img_productVisual;
    private TextView textArea_PrimaryInformations, textArea_SecondaryInformations;

    private ProductScannedViewModel vm;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(ProductScannedViewModel.class);

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

            String productName = !product.getProductName().isEmpty() ? product.getProductName() : "-";
            String nutriscore = product.getNutritionGradeTag();
            String image = product.getImageFrontUrl();
            String packaging = !product.getPackaging().isEmpty() ? product.getPackaging() : "-";
            String brand = !product.getBrands().isEmpty() ? product.getBrands() : "-";
            int nb_ingredients = (product.getIngredients() != null && product.getIngredients().size() != 0) ? product.getIngredients().size() : null;
            String allergenes = !product.getAllergens().isEmpty() ? product.getAllergens() : "-";
            String quantity = !product.getQuantity().isEmpty() ? product.getQuantity() : "-";

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
//        lbl_priceValue.setText(); // TODO


            textArea_PrimaryInformations.setText(
                    String.format("Marque : %s\nNombre d'ingrédients : %s\nPackaging : %s\nAllergènes : %s\nPoids/Volume : %s",
                    brand, nb_ingredients, packaging, allergenes, quantity));

//            String proteins = !product.getNutriments().contains("proteins")
            // TODO: Showing nutriments list


            textArea_SecondaryInformations.setText(String.format(""));
            // TODO
            /** txtArea 2
             * Protéines
             * Lipides
             * Energie
             * etc.
             */

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
                // TODO
                // Add to list in basket
                // Close fragment
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

        return root;
    }
}
