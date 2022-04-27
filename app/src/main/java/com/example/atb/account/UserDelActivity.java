package com.example.atb.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.atb.BaseActivity;
import com.example.atb.R;
import com.example.atb.account.userscard.UsersAdapter;
import com.example.atb.application.HomeApplication;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.DelDTO;
import com.example.atb.security.JwtSecurityService;
import com.example.atb.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserDelActivity extends BaseActivity {

    private TextView txtUserId;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_del);

        Bundle b =getIntent().getExtras();
        id = b.getLong("id");
        //String email=b.getString("email");
        txtUserId = findViewById(R.id.txtUserId);
        txtUserId.setText(String.valueOf(id));

    }

    public void handleClick(View view) {
        DelDTO delDTO= new DelDTO();
        delDTO.setId(id);

        AccountService.getInstance()
                .jsonApi()
                .delete(delDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful())
                        {
                            AccountResponseDTO data = response.body();
                            //JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            //jwtService.saveJwtToken(data.getToken());
                            //tvInfo.setText("response is good");
                            Intent intent = new Intent(UserDelActivity.this, UsersActivity.class);
                            startActivity(intent);
                            CommonUtils.hideLoading();//завершение прогресбара
                        } else {

                                System.out.println("------Error response parse body-----");

                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str = t.toString();
                        CommonUtils.hideLoading();//завершение прогресбара
                        int a = 12;
                    }
                });

    }
}