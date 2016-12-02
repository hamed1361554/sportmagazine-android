package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mvc.imagepicker.ImagePicker;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.List;

public class ProducerProductActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText priceEditText;
    private LabelledSpinner categoryLabelledSpinner;
    private EditText sizesEditText;
    private EditText brandsEditText;
    private EditText counterEditText;
    private EditText commentEditText;
    private LabelledSpinner ageCategoryLabelledSpinner;
    private LabelledSpinner genderLabelledSpinner;
    private RadioButton retailRadioButton;
    private RadioButton wholesaleRadioButton;

    private Button createButton;
    private ImageView productImageView;
    private ListView productColorsListView;
    private List<String> productColors;
    private ArrayAdapter<String> adapter;
    private Bitmap productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_product);

        this.nameEditText = (EditText) findViewById(R.id.input_producer_product_name);
        this.priceEditText = (EditText) findViewById(R.id.input_producer_product_price);
        this.categoryLabelledSpinner = (LabelledSpinner) findViewById(R.id.producer_product_category);
        this.sizesEditText = (EditText) findViewById(R.id.input_producer_product_sizes);
        this.brandsEditText = (EditText) findViewById(R.id.input_producer_product_brands);
        this.counterEditText = (EditText) findViewById(R.id.input_producer_product_counter);
        this.commentEditText = (EditText) findViewById(R.id.input_producer_product_comment);
        this.ageCategoryLabelledSpinner = (LabelledSpinner) findViewById(R.id.producer_product_age_category);
        this.genderLabelledSpinner = (LabelledSpinner) findViewById(R.id.producer_product_gender);
        this.retailRadioButton = (RadioButton) findViewById(R.id.producer_product_retail_type);
        this.wholesaleRadioButton = (RadioButton) findViewById(R.id.producer_product_wholesale_type);

        this.createButton = (Button) findViewById(R.id.add_producer_product);
        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

        // width and height will be at least 200px long (optional).
        ImagePicker.setMinQuality(200, 200);
        this.productImageView = (ImageView) findViewById(R.id.producer_product_image);
        findViewById(R.id.acquire_product_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        findViewById(R.id.select_producer_product_colors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickColor(v);
            }
        });

        this.productColors = new ArrayList<>();
        this.productColorsListView = (ListView) findViewById(R.id.producer_product_colors_list);
        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.productColors){

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                int c = Integer.parseInt(text.getText().toString());
                int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));
                text.setText("");
                text.setBackgroundColor(color);

                return view;
            }
        };
        this.productColorsListView.setAdapter(this.adapter);

        this.productColorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                productColors.remove(position);
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnItems(productColorsListView);
            }
        });

        this.categoryLabelledSpinner.setItemsArray(getResources().getStringArray(R.array.product_category_items));
        this.ageCategoryLabelledSpinner.setItemsArray(getResources().getStringArray(R.array.product_age_category_items));
        this.genderLabelledSpinner.setItemsArray(getResources().getStringArray(R.array.product_gender_items));
        this.categoryLabelledSpinner.getSpinner().setSelection(-1);
        this.ageCategoryLabelledSpinner.getSpinner().setSelection(-1);
        this.genderLabelledSpinner.getSpinner().setSelection(-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Bitmap gotImage = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            if (gotImage != null) {
                this.productImage =
                        ImageUtils.compressBitmap(gotImage, 4, 64);
                this.productImageView.setImageBitmap(this.productImage);
            } else {
                this.productImage = null;
                this.productImageView.setImageBitmap(null);
            }
        }
        catch (Exception error) {
            Toast.makeText(getApplicationContext(), getString(R.string.processing_image_error, error.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, getString(R.string.select_your_image));
    }

    public void onPickColor(View v) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, Color.BLUE, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                productColors.add(String.valueOf(color));
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnItems(productColorsListView);
            }
        });
        colorPickerDialog.show();
    }

    public boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = this.productColorsListView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;
        } else {
            return false;
        }
    }

    private void create(){
        if(!validate()){
            onCreateFailed();
            return;
        }

        this.createButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ProducerProductActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_product));
        progressDialog.show();

        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        int category = categoryLabelledSpinner.getSpinner().getSelectedItemPosition();
        String sizes = sizesEditText.getText().toString();
        String brands = brandsEditText.getText().toString();
        int counter = Integer.parseInt(counterEditText.getText().toString());
        int ageCategory = ageCategoryLabelledSpinner.getSpinner().getSelectedItemPosition();
        int gender = genderLabelledSpinner.getSpinner().getSelectedItemPosition();
        String comment = commentEditText.getText().toString();
        int wholesaleType = 0;
        if (this.wholesaleRadioButton.isChecked()){
            wholesaleType = 1;
        }

        StringBuilder colors = new StringBuilder();
        for (int i = 0; i < this.productColorsListView.getAdapter().getCount(); i++){
            colors.append(this.productColorsListView.getAdapter().getItem(i));
            colors.append(",");
        }

        try {
            String pdrctImg = "";
            if (this.productImage != null){
                pdrctImg = ImageUtils.encodeToBase64(this.productImage);
            }
            ProductServicesI.getInstance().create(name, Double.parseDouble(price), category,
                    colors.toString(), sizes, brands, counter, ageCategory, gender, wholesaleType, comment,
                    pdrctImg);
            Toast.makeText(getApplicationContext(), getString(R.string.create_product_succeeded), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onCreateSuccess();
                            // onCreateFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.create_product_failed, ex.getMessage()), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // onCreateSuccess();
                            onCreateFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    public void onCreateSuccess() {
        this.createButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onCreateFailed() {
        this.createButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        int category = categoryLabelledSpinner.getSpinner().getSelectedItemPosition();
        String sizes = sizesEditText.getText().toString();
        String brands = brandsEditText.getText().toString();
        String counter = counterEditText.getText().toString();
        int ageCategory = ageCategoryLabelledSpinner.getSpinner().getSelectedItemPosition();
        int gender = genderLabelledSpinner.getSpinner().getSelectedItemPosition();

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError(getString(R.string.invalid_product_name));
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (price.isEmpty() || priceEditText.length() < 4) {
            priceEditText.setError(getString(R.string.invalid_product_price));
            valid = false;
        } else {
            priceEditText.setError(null);
        }

        TextView spinnerTextView = (TextView) categoryLabelledSpinner.getSpinner().getSelectedView();
        if (category <= -1) {
            if (spinnerTextView != null) {
                spinnerTextView.setError(getString(R.string.invalid_product_category));
            }
                valid = false;
            } else {
            if (spinnerTextView != null) {
                spinnerTextView.setError(null);
            }
        }

        if (this.productColorsListView.getAdapter().getCount() <= 0) {
            valid = false;
        }

        if (sizes.isEmpty()) {
            sizesEditText.setError(getString(R.string.invalid_product_sizes));
            valid = false;
        } else {
            sizesEditText.setError(null);
        }

        if (brands.isEmpty()) {
            brandsEditText.setError(getString(R.string.invalid_product_brands));
            valid = false;
        } else {
            brandsEditText.setError(null);
        }

        if (counter == "") {
            counterEditText.setError(getString(R.string.invalid_product_counter));
            valid = false;
        } else {
            try {
                int c = Integer.parseInt(counter);
                if (c > 0 ) {
                    counterEditText.setError(null);
                } else {
                    counterEditText.setError(getString(R.string.invalid_product_counter));
                    valid = false;
                }
            }
            catch (Exception error) {
                counterEditText.setError(getString(R.string.invalid_product_counter));
                valid = false;
            }
        }

        spinnerTextView = (TextView) ageCategoryLabelledSpinner.getSpinner().getSelectedView();
        if (ageCategory <= -1) {
            if (spinnerTextView != null) {
                spinnerTextView.setError(getString(R.string.invalid_product_age_category));
            }
            valid = false;
        } else {
            if (spinnerTextView != null) {
                spinnerTextView.setError(null);
            }
        }

        spinnerTextView = (TextView) genderLabelledSpinner.getSpinner().getSelectedView();
        if (gender <= -1) {
            if (spinnerTextView != null) {
                spinnerTextView.setError(getString(R.string.invalid_product_gender));
            }
            valid = false;
        } else {
            if (spinnerTextView != null) {
                spinnerTextView.setError(null);
            }
        }

        return valid;
    }
}
