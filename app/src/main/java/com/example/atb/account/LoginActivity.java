package com.example.atb.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atb.BaseActivity;
import com.example.atb.MainActivity;
import com.example.atb.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atb.application.HomeApplication;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.LoginDTO;
import com.example.atb.network.account.dto.ValidationRegisterDTO;
import com.example.atb.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private TextView tvInfo;
    private TextInputLayout textFieldEmail;
    private TextInputEditText txtEmail;

    private TextInputLayout textFieldPassword;
    private TextInputEditText txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvInfo = findViewById(R.id.tvInfo);
        textFieldEmail = findViewById(R.id.textFieldEmail);
        txtEmail = findViewById(R.id.txtEmail);

        textFieldPassword = findViewById(R.id.textFieldPassword);
        txtPassword = findViewById(R.id.txtPassword);


    }

    public void handleClick(View view) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(txtEmail.getText().toString());
        loginDTO.setPassword(txtPassword.getText().toString());

        if (!validationFields(loginDTO))
            return;
        AccountService.getInstance()
                .jsonApi()
                .login(loginDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful()) {
                            AccountResponseDTO data = response.body();

                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());

                            //tvInfo.setText("response is good");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                showErrorsServer(response.errorBody().string());
                            } catch (Exception e) {
                                System.out.println("------Error response parse body-----");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str = t.toString();
                        int a = 12;
                    }
                });
    }

    private boolean validationFields(LoginDTO loginDTO) {
        textFieldEmail.setError("");
        if (loginDTO.getEmail().equals("")) {
            textFieldEmail.setError("Вкажіть пошту");
            return false;
        }

        textFieldPassword.setError("");
        if (loginDTO.getPassword().equals("")) {
            textFieldPassword.setError("Вкажіть пароль");
            return false;
        }

        return true;
    }

    private void showErrorsServer(String json) {
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json, ValidationRegisterDTO.class);
        String str = "";
        if (result.getErrors().getEmail() != null) {
            for (String item : result.getErrors().getEmail()) {
                str += item + "\n";
            }
        }
        textFieldEmail.setError(str);
    }
}