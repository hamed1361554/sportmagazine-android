package com.mitranetpars.sportmagazine.services;

import com.mitranetpars.sportmagazine.common.dto.security.ChangeUserPasswordData;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by Hamed on 9/16/2016.
 */
public interface SecurityServices {
    @POST("/login")
    Call<User> login(@Body User user);

    @POST("/signin")
    Call<User> create(@Body User user);

    @POST("/user/get")
    Call<User> getUser(@Body User user);

    @POST("/user/update")
    Call<User> updateUser(@Body User user);

    @POST("/activate")
    Call<String> activate(@Body String userData);

    @POST("/change_password")
    Call<String> changePassword(@Body ChangeUserPasswordData data);
}
