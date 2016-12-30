package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mitranetpars.sportmagazine.common.dto.product.ProductSearchFilter;
import com.satsuware.usefulviews.LabelledSpinner;

/**
 * Created by Hamed on 12/30/2016.
 */

public class ProductAdvancedSearchDialog extends DialogFragment{

    private EditText nameEditText;
    private EditText fromPriceEditText;
    private EditText toPriceEditText;
    private EditText sizeEditText;
    private EditText brandEditText;
    private LabelledSpinner categorySpinner;
    private LabelledSpinner ageCategorySpinner;
    private LabelledSpinner genderSpinner;
    private Button searchButton;

    public ProductAdvancedSearchDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_search_filter_fragment, container);
        getDialog().setTitle(getText(R.string.product_search));

        this.nameEditText = (EditText) view.findViewById(R.id.txt_search_filter_product_name);
        this.fromPriceEditText = (EditText) view.findViewById(R.id.txt_search_filter_product_price_from);
        this.toPriceEditText = (EditText) view.findViewById(R.id.txt_search_filter_product_price_to);
        this.sizeEditText = (EditText) view.findViewById(R.id.txt_search_filter_product_size);
        this.brandEditText = (EditText) view.findViewById(R.id.txt_search_filter_product_brand);
        this.categorySpinner = (LabelledSpinner) view.findViewById(R.id.spn_search_filter_category);
        this.ageCategorySpinner = (LabelledSpinner) view.findViewById(R.id.spn_search_filter_age_category);
        this.genderSpinner = (LabelledSpinner) view.findViewById(R.id.spn_search_filter_gender);
        this.searchButton = (Button) view.findViewById(R.id.btn_search_filter_product);

        this.categorySpinner.setItemsArray(getResources().getStringArray(R.array.product_category_items));
        this.ageCategorySpinner.setItemsArray(getResources().getStringArray(R.array.product_age_category_items));
        this.genderSpinner.setItemsArray(getResources().getStringArray(R.array.product_gender_items));
        this.categorySpinner.getSpinner().setSelection(-1);
        this.ageCategorySpinner.getSpinner().setSelection(-1);
        this.genderSpinner.getSpinner().setSelection(-1);

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });

        return view;
    }

    private void onSearch(){
        Intent data = new Intent();

        String name = this.nameEditText.getText().toString();
        if (name != null && name != "" && name.trim() != ""){
            data.putExtra("name", name.trim());
        } else {
            data.putExtra("name", "");
        }

        try {
            double fromPrice = Double.parseDouble(this.fromPriceEditText.getText().toString());
            data.putExtra("from_price", fromPrice);
        } catch (Exception error) {
            data.putExtra("from_price", 0);
        }

        try {
            double toPrice = Double.parseDouble(this.toPriceEditText.getText().toString());
            data.putExtra("to_price", toPrice);
        } catch (Exception error) {
            data.putExtra("to_price", 0);
        }

        String size = this.sizeEditText.getText().toString();
        if (size != null && size != "" && size.trim() != ""){
            data.putExtra("size", size.trim());
        } else {
            data.putExtra("size", "");
        }

        String brand = this.brandEditText.getText().toString();
        if (brand != null && brand != "" && brand.trim() != ""){
            data.putExtra("brand", brand.trim());
        } else {
            data.putExtra("brand", "");
        }

        data.putExtra("categories", this.categorySpinner.getSpinner().getSelectedItemPosition());
        data.putExtra("age_categories", this.ageCategorySpinner.getSpinner().getSelectedItemPosition());
        data.putExtra("gender", this.genderSpinner.getSpinner().getSelectedItemPosition());

        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, data);
        this.dismiss();
    }
}
