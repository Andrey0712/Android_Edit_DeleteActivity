package com.example.atb.network.account;

import com.example.atb.account.userscard.UserDTO;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.DelDTO;
import com.example.atb.network.account.dto.EditDTO;
import com.example.atb.network.account.dto.LoginDTO;
import com.example.atb.network.account.dto.RegisterDTO;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AccountApi {
    @POST("/api/account/register")
    public Call<AccountResponseDTO> register(@Body RegisterDTO model);
    @GET("/api/account/users")
    public Call<List<UserDTO>> users();
    @POST("/api/account/login")
    public Call<AccountResponseDTO> login(@Body LoginDTO model);
    @POST("/api/account/delete")
    public Call<AccountResponseDTO> delete(@Body DelDTO model);
    @POST("/api/account/edit")
    public Call<AccountResponseDTO> edit(@Body EditDTO model);

}
