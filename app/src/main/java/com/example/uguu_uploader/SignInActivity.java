package com.example.uguu_uploader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.User;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final UguuDatabase db = UguuDatabase.getDatabase(SignInActivity.this);

        final EditText edtSignInUsername = findViewById(R.id.edtSignInUsername);
        final EditText edtSignInPassword = findViewById(R.id.edtSignInPassword);
        final EditText edtSignInRepeat = findViewById(R.id.edtSignInRepeat);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Uguu", edtSignInUsername.getText().toString());
                Log.d("Uguu", edtSignInPassword.getText().toString());
                Log.d("Uguu", edtSignInRepeat.getText().toString());

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        final String username = edtSignInUsername.getText().toString();
                        User user = db.userDao().getByUsername(username);
                        if (user != null) {
                            Log.d("SignIn", "User already exists");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignInActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                                    edtSignInUsername.requestFocus();
                                }
                            });
                            return;
                        }
                        final String password = edtSignInPassword.getText().toString();
                        if (TextUtils.isEmpty(password)) {
                            Log.d("SignIn", "Empty string");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignInActivity.this, "Empty string", Toast.LENGTH_LONG).show();
                                    edtSignInPassword.requestFocus();
                                    edtSignInRepeat.getText().clear();
                                }
                            });
                            return;
                        }
                        if (!password.equals(edtSignInRepeat.getText().toString())) {
                            Log.d("SignIn", "Passwords don't match");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignInActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                                    edtSignInPassword.requestFocus();
                                    edtSignInRepeat.getText().clear();
                                }
                            });
                            return;
                        }
                        user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        Log.d("SignIn", user.toString());
                        db.userDao().insert(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent output = new Intent();
                                output.putExtra("username", username);
                                setResult(Activity.RESULT_OK, output);
                                finish();
                            }
                        });
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
