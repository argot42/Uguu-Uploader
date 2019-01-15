package com.example.uguu_uploader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class UploadActivity extends AppCompatActivity {

    static String URL = "http://192.168.122.117/api.php?id=upload-tool";
    //static String URL = "https://uguu.se/api.php?d=upload-tool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Upload - " + username);
        }

        final EditText edtUploadPath = findViewById(R.id.edtUploadPath);
        final CheckBox chkUploadRanFilename = findViewById(R.id.chkUploadRanFilename);
        final EditText edtUploadCustomFilename = findViewById(R.id.edtUploadCustomFilename);
        final Button btnUploadUButton = findViewById(R.id.btnUploadUButton);
        final Button btnUploadFile = findViewById(R.id.btnUploadFile);

        btnUploadUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        String path = edtUploadPath.getText().toString();
                        if (TextUtils.isEmpty(path)) {
                            Log.d("Upload", "Empty Path");
                            return;
                        }


                    }
                };
            }
        });

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Upload", "FILE!");
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
