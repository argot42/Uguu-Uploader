package com.example.uguu_uploader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.User;

public class LoginActivity extends AppCompatActivity {

    private static int NEWUSER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final UguuDatabase db = UguuDatabase.getDatabase(LoginActivity.this);

        final EditText edtLoginUsername = findViewById(R.id.edtLoginUsername);
        final EditText edtLoginPassword = findViewById(R.id.edtLoginPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView tvLoginSignin = findViewById(R.id.tvLoginSignin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edtLoginUsername.getText().toString();
                final String password = edtLoginPassword.getText().toString();

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        User user = db.userDao().getByUsername(username);
                        if (user == null) {
                            Log.d("Login", "The user doesn't exist");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
                                    edtLoginUsername.requestFocus();
                                }
                            });
                            return;
                        }
                        if (!user.getPassword().equals(password)) {
                            Log.d("Login", "Wrong password");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                                    edtLoginPassword.requestFocus();
                                }
                            });
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("username", username);
                                startActivity(i);
                            }
                        });
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        tvLoginSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignInActivity.class);
                startActivityForResult(i, NEWUSER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEWUSER) {
            if (resultCode == RESULT_OK) {
                final String username = data.getExtras().getString("username");
                Toast.makeText(LoginActivity.this, username + " created!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
