package com.mitranetpars.sportmagazine.services;

import com.mitranetpars.sportmagazine.common.dto.security.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by Hamed on 9/16/2016.
 */
public interface SecurityServices {
    @POST("/login")
    Call<User> login(@Body User user);

    @POST("/signin")
    Call<User> create(@Body User user);

    @GET("/activate")
    Call<String> activate(@Body String userData);
}
