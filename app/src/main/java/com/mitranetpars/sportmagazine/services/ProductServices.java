package com.mitranetpars.sportmagazine.services;

import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.product.ProductSearchFilter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hamed on 11/21/2016.
 */

public interface ProductServices {
    @POST("/product/create")
    Call<Product> create(@Body Product product);

    @POST("/product/find")
    Call<ArrayList<Product>> search(@Body ProductSearchFilter filter);
}
