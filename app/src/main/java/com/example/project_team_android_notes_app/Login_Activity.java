package com.example.project_team_android_notes_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    EditText edt_Email, edt_Pass;
    Button btn_login;
    ProgressBar progressBar;
    TextView tv_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        /*Ánh xạ id từ layout*/
        setContentView(R.layout.activity_login);
        edt_Email = findViewById(R.id.edt_mailID_lg);
        edt_Pass = findViewById(R.id.edt_pass_lg);
        btn_login = findViewById(R.id.btn_login_lg);
        tv_signUp = findViewById(R.id.tv_signUp_lg);
        progressBar = findViewById(R.id.progress_bar);

        //Xử lí click button login
        btn_login.setOnClickListener(v-> loginUser());
        //xử lí click vào textview SignUp
        tv_signUp.setOnClickListener(v-> startActivity(new Intent(Login_Activity.this, SignUp_Activity.class)));


    }

    private void loginUser() {
        //Tạo biến đón các chuỗi từ edittext ở layout
        String strEmail = edt_Email.getText().toString();
        String strPass = edt_Pass.getText().toString();

        boolean isValidate = validateData(strEmail, strPass);
        if (!isValidate){
            return;
        }

        loginAccountInFireBase(strEmail, strPass);
    }

    private void loginAccountInFireBase(String strEmail, String strPass) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(strEmail,strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(Login_Activity.this, MainActivity.class));

                    }else {
                        Toast.makeText(Login_Activity.this, "Email not verified, Please check your email!", Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(Login_Activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void changeInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        }
    }


    //Kiểm tra dữ liệu trước khi tạo account mới
    boolean validateData(String email, String pass)
    {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edt_Email.setError("Email is invalid");
            return false;
        }
        if (pass.length()<6)
        {
            edt_Pass.setError("Password length is invalid");
            return false;
        }

        return true;



    }
}