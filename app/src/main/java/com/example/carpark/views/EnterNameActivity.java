package com.example.carpark.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.carpark.Api.ParkingApi;
import com.example.carpark.Api.Responses.BaseDataResponse;
import com.example.carpark.Api.Responses.LoginReg.UserResponse;
import com.example.carpark.Api.RetrofitClient;
import com.example.carpark.Model.NewUser;
import com.example.carpark.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnterNameActivity extends AppCompatActivity {
    EditText firstName,lastName;
    Button btnContinue;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        initView();

        /*
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EnterNameActivity.this, EnterOTP.class);
                startActivity(i);
            }
        });*/

    }


    private void initView(){
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        btnContinue = findViewById(R.id.btnContinue);
        progressBar = findViewById(R.id.en_progress_bar);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContinue();
            }
        });
    }

    public void editContinue(){
        String firstName = this.firstName.getText().toString();
        String lastName = this.lastName.getText().toString();
        String phoneNum = getIntent().getStringExtra("VerifiedPhone");
        String otp = getIntent().getStringExtra("OTP");


        if (firstName.isEmpty()) {
            this.firstName.setError("Please enter your first name");
            this.firstName.requestFocus();
        }
        if (lastName.isEmpty()) {
            this.lastName.setError("Please enter your last name");
            this.lastName.requestFocus();
        }
        NewUser newUser = new NewUser(otp, phoneNum, firstName, lastName);

        ParkingApi parkingApi = RetrofitClient.getInstance().create(ParkingApi.class);
        progressBar.setVisibility(View.VISIBLE);
        parkingApi.registerUser(newUser).enqueue(new Callback<BaseDataResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<BaseDataResponse<UserResponse>> call, Response<BaseDataResponse<UserResponse>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EnterNameActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EnterNameActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EnterNameActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseDataResponse<UserResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EnterNameActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
