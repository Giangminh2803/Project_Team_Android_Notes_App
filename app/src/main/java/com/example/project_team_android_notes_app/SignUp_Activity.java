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

public class SignUp_Activity extends AppCompatActivity {

    EditText edt_Email, edt_Pass, edt_re_pass;
    Button btn_createAcc;
    ProgressBar progressBar;
    TextView tv_login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        /*Ánh xạ id từ layout*/
        edt_Email = findViewById(R.id.edt_mailID_su);
        edt_Pass = findViewById(R.id.edt_pass_su);
        edt_re_pass = findViewById(R.id.edt_re_pass_su);
        btn_createAcc = findViewById(R.id.btn_createAcc_su);
        tv_login = findViewById(R.id.tv_login_su);
        progressBar = findViewById(R.id.progress_bar);
        //Xử lý sự kiện click vào button Tạo mới tài khoản
        btn_createAcc.setOnClickListener(v->createAccount());
        //Xử lý sự kiến click textview login dừng activity SignUp
        tv_login.setOnClickListener(v-> finish());


    }
    void createAccount()
    {
        //Tạo biến đón các chuỗi từ edittext ở layout
        String strEmail = edt_Email.getText().toString();
        String strPass = edt_Pass.getText().toString();
        String strRe_Pass = edt_re_pass.getText().toString();

        boolean isValidate = validateData(strEmail, strPass, strRe_Pass);
        if (!isValidate){
            return;
        }

        createAccountInFireBase(strEmail, strPass);

    }

    //Hàm tạo acc dùng hàm creteUser... trong thư viện của FireBase
    private void createAccountInFireBase(String strEmail, String strPass) {

        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(SignUp_Activity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful())
                        {

                            Toast.makeText(SignUp_Activity.this, "Successfully, please verify your email", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            startActivity(new Intent(SignUp_Activity.this, Login_Activity.class));
                            finish();
                        }else {
                            Toast.makeText(SignUp_Activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Xử lí nút load khi thành công thì hiển thị thanh load và ần bút tạo acc và ngược lại
    void changeInProgress(boolean inProgress)
    {
        if (inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btn_createAcc.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.VISIBLE);
            btn_createAcc.setVisibility(View.GONE);
        }
    }


    //Kiểm tra dữ liệu trước khi tạo account mới
    boolean validateData(String email, String pass, String re_pass)
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
         if (!pass.matches(re_pass))
        {
            edt_re_pass.setError("Password not matched");
        }
            return true;



    }
}