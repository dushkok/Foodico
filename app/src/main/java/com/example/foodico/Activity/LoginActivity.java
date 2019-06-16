package com.example.foodico.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodico.Helper.DatabaseHelper;
import com.example.foodico.Model.User;
import com.example.foodico.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginEmailInput)
    EditText loginEmailInput;

    @BindView(R.id.loginPasswordInput)
    EditText loginPasswordInput;

    @BindView(R.id.progressBarLogin)
    ProgressBar progressBarLogin;

    private DatabaseReference databaseReference;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Log in");
        User user = (User) getIntent().getSerializableExtra("signedupUser");
        if (user != null) {
            loginEmailInput.setText(user.getEmail());
            loginPasswordInput.setText(user.getPassword());
        }
        databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.getLoggedUser() != null) {
            Intent startMenuActivity = new Intent(this, MenuActivity.class);
            startActivity(startMenuActivity);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onBackPressed() {
        // nothing happens.
    }

    @OnClick(R.id.openSignupButton)
    public void onOpenSignupButtonClick() {
        Intent openSignUpActivity = new Intent(this, SignupActivity.class);
        startActivity(openSignUpActivity);
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick() {
        if (validateInput()) {
            progressBarLogin.setVisibility(View.VISIBLE);
            String email = loginEmailInput.getText().toString();
            String password = loginPasswordInput.getText().toString();
            Query query = databaseReference.orderByChild("email").equalTo(email);
            query.addValueEventListener(valueEventListener(email, password));
        }
    }

    public ValueEventListener valueEventListener(final String email, final String password) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    if (user.getEmail().compareTo(email) == 0 &&
                            user.getPassword().compareTo(password) == 0) {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                        databaseHelper.logInUser(user);
                        Intent openMenu = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(openMenu);
                    } else {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        loginEmailInput.setError("Wrong email or password!");
                    }
                } else {
                    progressBarLogin.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Failed login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public boolean validateInput() {
        if (!loginEmailInput.getText().toString().contains("@")) {
            loginEmailInput.setError("Invalid email address");
            return false;
        }
        return true;
    }
}
