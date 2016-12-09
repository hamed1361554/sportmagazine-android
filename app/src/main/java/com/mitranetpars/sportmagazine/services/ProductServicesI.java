package com.mitranetpars.sportmagazine.services;

import android.os.AsyncTask;
import android.text.Html;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.product.ProductSearchFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hamed on 11/21/2016.
 */

public class ProductServicesI {
    private static final ProductServicesI instance;
    private final ProductServices productServices;

    private final BlockingQueue<Product> createQueue = new ArrayBlockingQueue<Product>(1, true);
    private final BlockingQueue<ArrayList<Product>> searchQueue = new ArrayBlockingQueue<ArrayList<Product>>(1, true);

    static {
        instance = new ProductServicesI();
    }

    public static ProductServicesI getInstance() {
        return instance;
    }

    private ProductServicesI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SportMagazineApplication.GetServerUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.productServices = retrofit.create(ProductServices.class);
    }

    public Product create(String name, double price, int category, String colors, String sizes, String brands,
                          int counter, int ageCategory, int gender, int wholesaleType, String comment,
                          String image) throws Exception {
        Product product = new Product();

        product.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());
        product.setUserName(SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName());

        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setColors(colors);
        product.setSizes(sizes);
        product.setBrands(brands);
        product.setCounter(counter);
        product.setAgeCategory(ageCategory);
        product.setGender(gender);
        product.setWholesaleType(wholesaleType);
        product.setComment(comment);
        product.setImage(image);

        CreateAsyncTask createAsyncTask = new CreateAsyncTask();
        createAsyncTask.execute(product);

        Product createdProduct = this.createQueue.take();
        if (createdProduct == null) {
            throw new Exception(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
        }

        if (createdProduct.getName() == null || createdProduct.getName() == ""){
            if(createdProduct.getComment() != null &&
                    createdProduct.getComment() != "") {
                if (createdProduct.getComment().startsWith("error")) {
                    String error = createdProduct.getComment().substring(5);
                    throw new Exception(error);
                }
                if (createdProduct.getComment().startsWith("htmlerrorbody")) {
                    String error = createdProduct.getComment().substring(13);
                    throw new Exception(Html.fromHtml(error).toString());
                }
            }
        }

        return createdProduct;
    }

    public ArrayList<Product> search(Date fromCreationDate, Date toCreationDate, double fromPrice, double toPrice,
                                     String name, int categories, int wholesaleType, int offset, int limit) throws Exception{
        ProductSearchFilter filter = new ProductSearchFilter();

        filter.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());
        filter.setUserName(SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName());

        filter.from_creation_date = fromCreationDate;
        filter.to_creation_date = toCreationDate;
        filter.from_price = fromPrice;
        filter.to_price = toPrice;
        filter.name = name;
        filter.categories = categories;
        filter.wholesale_type = wholesaleType;

        filter.__limit__ = limit;
        filter.__offset__ = offset;

        SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute(filter);

        ArrayList<Product> products = this.searchQueue.take();

        if (products.size() == 1) {
            Product p = products.get(0);
            if (p.getName() == null || p.getName() == "") {
                if (p.getComment() != null &&
                        p.getComment() != "") {
                    if (p.getComment().startsWith("error")) {
                        String error = p.getComment().substring(5);
                        throw new Exception(error);
                    }
                    if (p.getComment().startsWith("htmlerrorbody")) {
                        String error = p.getComment().substring(13);
                        throw new Exception(Html.fromHtml(error).toString());
                    }
                }
            }
        }

        return products;
    }

    private class CreateAsyncTask extends AsyncTask {

        @Override
        protected Product doInBackground(Object[] callVariables) {
            Call<Product> callProduct = productServices.create((Product) callVariables[0]);

            Product product = null;
            try {
                Response<Product> response = callProduct.execute();

                Product body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    product = new Product();
                    product.setComment(String.format("%s%s", "htmlerrorbody", error));
                    createQueue.put(product);
                }
                else {
                    product = response.body();
                    if (product != null) {
                        createQueue.put(product);
                    } else {
                        product = new Product();
                        product.setComment(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_create_product)));
                        createQueue.put(product);
                    }
                }
            } catch (Exception productEx) {
                product = new Product();
                try {
                    product.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    createQueue.put(product);
                } catch (Exception queueEx){
                    product.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    createQueue.add(product);
                }
            }

            return product;
        }
    }

    private class SearchAsyncTask extends AsyncTask {

        @Override
        protected ArrayList<Product> doInBackground(Object[] callVariables) {
            Call<ArrayList<Product>> callProducts = productServices.search((ProductSearchFilter) callVariables[0]);

            ArrayList<Product> products = null;
            try {
                Response<ArrayList<Product>> response = callProducts.execute();

                ArrayList<Product> body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    Product prd = new Product();
                    prd.setComment(String.format("%s%s", "htmlerrorbody", error));
                    products = new ArrayList<Product>();
                    products.add(prd);
                    searchQueue.put(products);
                }
                else {
                    products = response.body();
                    if (products != null) {
                        searchQueue.put(products);
                    } else {
                        Product prd = new Product();
                        prd.setComment(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_search_product)));
                        products = new ArrayList<Product>();
                        products.add(prd);
                        searchQueue.put(products);
                    }
                }
            } catch (Exception productEx) {
                products = new ArrayList<Product>();
                try {
                    Product prd = new Product();
                    prd.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    products.add(prd);
                    searchQueue.put(products);
                } catch (Exception queueEx){
                    Product prd = new Product();
                    prd.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    products.add(prd);
                    searchQueue.add(products);
                }
            }

            return products;
        }
    }
}
