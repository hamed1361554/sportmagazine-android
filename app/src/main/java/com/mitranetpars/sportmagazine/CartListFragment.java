package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.Cart.CartHelper;
import com.mitranetpars.sportmagazine.Cart.Saleable;
import com.mitranetpars.sportmagazine.adapters.ProductsListAdapter;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.services.InvoiceServicesI;

import java.util.ArrayList;
import java.util.Calendar;

import at.markushi.ui.CircleButton;

/**
 * Created by Hamed on 12/2/2016.
 */

public class CartListFragment extends Fragment {
    private ListView listview;
    private CircleButton purchaseButton;
    private Activity parentActivity;

    ArrayList<Product> products;
    ProductsListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater
                .inflate(R.layout.cart_list_view, container, false);

        this.listview = (ListView) rootView.findViewById(R.id.cart_products_listview);
        this.purchaseButton = (CircleButton) rootView.findViewById(R.id.cart_products_purchase);

        // listening to single listitem click
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        this.products = new ArrayList<>();
        this.listAdapter = new ProductsListAdapter(this.parentActivity,
                R.layout.product_list_row,
                this.products);
        this.listview.setAdapter(this.listAdapter);

        for (Saleable p: CartHelper.getCart().getProducts()) {
            this.products.add((Product)p);
        }
        this.listAdapter.notifyDataSetChanged();

        this.purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    purchase();
                    Toast.makeText(parentActivity, "Purchase Successful", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(CartListFragment.this).commit();
                }
                catch (Exception error){
                    Toast.makeText(parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    private void purchase() throws Exception {
        ArrayList<InvoiceItem> items = new ArrayList<>();
        for (Saleable s: CartHelper.getCart().getProducts()){
            Product p = (Product)s;

            InvoiceItem i = new InvoiceItem();
            i.setPrice(p.getPrice());
            i.setProductID(p.getID());
            i.setQuantity(CartHelper.getCart().getQuantity(s));
            items.add(i);
        }

        InvoiceServicesI.getInstance().register(items);
        CartHelper.getCart().clear();
    }
}
