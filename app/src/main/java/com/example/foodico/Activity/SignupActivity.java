package com.example.foodico.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodico.Model.User;
import com.example.foodico.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.signupEmailInput)
    EditText signupEmailInput;

    @BindView(R.id.signupNameInput)
    EditText signupNameInput;

    @BindView(R.id.signupPasswordInput)
    EditText signupPasswordInput;

    @BindView(R.id.progressBarSignup)
    ProgressBar progressBarSignup;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    @OnClick(R.id.signupButton)
    public void onSignupButtonClick() {
        final User newUser = getUser();
        Query query = databaseReference.orderByChild("email").equalTo(newUser.getEmail());
        if (validateInput()) {
            progressBarSignup.setVisibility(View.VISIBLE);
            query.addListenerForSingleValueEvent(valueEventListener(newUser));
        }
    }

    public ValueEventListener valueEventListener(final User user) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    signupEmailInput.setError("Email already in use!");
                } else {
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBarSignup.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_LONG).show();
                            Intent startLoginActivity = new Intent(SignupActivity.this, LoginActivity.class);
                            startLoginActivity.putExtra("signedupUser", user);
                            startActivity(startLoginActivity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarSignup.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Error signing up!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public User getUser() {
        String name = signupNameInput.getText().toString();
        String email = signupEmailInput.getText().toString();
        String password = signupPasswordInput.getText().toString();
        return new User(name, email, password);
    }

    public boolean validateInput() {
        if (!signupEmailInput.getText().toString().contains("@")) {
            signupEmailInput.setError("Invalid email address");
            return false;
        } else if (signupNameInput.getText().toString().isEmpty()) {
            signupNameInput.setError("Name can't be empty");
            return false;
        } else if (signupPasswordInput.getText().toString().length() < 5) {
            signupPasswordInput.setError("Password too short");
            return false;
        }
        return true;
    }
}
