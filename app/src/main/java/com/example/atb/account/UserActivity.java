package com.example.atb.account;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atb.BaseActivity;
import com.example.atb.R;
import com.example.atb.application.HomeApplication;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.EditDTO;
import com.example.atb.network.account.dto.RegisterDTO;
import com.example.atb.network.account.dto.ValidationRegisterDTO;
import com.example.atb.security.JwtSecurityService;
import com.example.atb.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseActivity {
    private  TextView txtUserEmail;
    private TextInputLayout textFieldFirstName;
    private TextInputEditText txtFirstName;

    private TextInputLayout textFieldSecondName;
    private TextInputEditText txtSecondName;

    private TextInputLayout textFieldPhone;
    private TextInputEditText txtPhone;

    long id;
    String email="";
    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    String sImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle b =getIntent().getExtras();
        //id = b.getLong("id");
        email=b.getString("email");

        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserEmail.setText(String.valueOf(email));

        textFieldFirstName = findViewById(R.id.textFieldFirstName);
        txtFirstName = findViewById(R.id.txtFirstName);
        textFieldSecondName = findViewById(R.id.textFieldSecondName);
        txtSecondName = findViewById(R.id.txtSecondName);
        textFieldPhone = findViewById(R.id.textFieldPhone);
        txtPhone = findViewById(R.id.txtPhone);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
    }

    public void handleSelectUpdateImageClick(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                // update the preview image in the layout
                IVPreviewImage.setImageURI(uri);
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            }
        }
    }

    public void handleClick(View view) {
        EditDTO editDTO = new EditDTO();
        editDTO.setEmail(email);
        editDTO.setFirstName(txtFirstName.getText().toString());
        editDTO.setSecondName(txtSecondName.getText().toString());
        editDTO.setPhone(txtPhone.getText().toString());
//        editDTO.setFirstName("test");
//        editDTO.setSecondName("test");
//        editDTO.setPhone("1234567899");
//        registerDTO.setPassword("12345");
//        registerDTO.setConfirmPassword("12345");
        editDTO.setPhoto(sImage);

        if (!validationFields(editDTO))
            return;
        CommonUtils.showLoading(this);//запуск прогресбара
        AccountService.getInstance()
                .jsonApi()
                .edit(editDTO)
                .enqueue(new Callback<AccountResponseDTO>() {
                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if (response.isSuccessful())
                        {
                            AccountResponseDTO data = response.body();
                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());
                            //tvInfo.setText("response is good");
                            Intent intent = new Intent(UserActivity.this, UsersActivity.class);
                            startActivity(intent);
                            CommonUtils.hideLoading();//завершение прогресбара
                        } else {
                            try {
                                showErrorsServer(response.errorBody().string());
                                CommonUtils.hideLoading();//завершение прогресбара
                            } catch (Exception e) {
                                System.out.println("------Error response parse body-----");
                            }
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

    private boolean validationFields(EditDTO editDTO) {

        textFieldSecondName.setError("");
        if (editDTO.getSecondName().equals("")) {
            textFieldSecondName.setError("Вкажіть прізвище");
            return false;
        }

        textFieldFirstName.setError("");
        if (editDTO.getFirstName().equals("")) {
            textFieldFirstName.setError("Вкажіть ім'я");
            return false;
        }
        textFieldPhone.setError("");
        if (editDTO.getPhone().equals("")) {
            textFieldPhone.setError("Вкажіть номер телефона");
            return false;
        }

        textFieldFirstName.setError("");
        if (sImage.equals("")) {
            textFieldFirstName.setError("Оберіть фотку");
            return false;
        }
        return true;
    }

    private void showErrorsServer(String json) {
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json, ValidationRegisterDTO.class);
        String str = "";
        if (result.getErrors().getSecondName()!= null) {
            for (String item : result.getErrors().getSecondName()) {
                str += item + "\n";
            }
        }
        textFieldSecondName.setError(str);

        str = "";
        if (result.getErrors().getFirstName() != null) {
            for (String item : result.getErrors().getFirstName()) {
                str += item + "\n";
            }
        }
        textFieldFirstName.setError(str);

        str = "";
        if (result.getErrors().getPhone() != null) {
            for (String item : result.getErrors().getPhone()) {
                str += item + "\n";
            }
        }
        textFieldPhone.setError(str);
    }
}