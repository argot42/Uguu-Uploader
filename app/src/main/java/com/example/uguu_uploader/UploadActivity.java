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
import android.widget.Toast;

import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.Upload;

import org.w3c.dom.Text;

public class UploadActivity extends AppCompatActivity {

    private static String URL = "http://192.168.122.117/api.php?id=upload-tool";
    //static String URL = "https://uguu.se/api.php?d=upload-tool";

    private EditText edtUploadPath;
    private CheckBox chkUploadRanFilename;
    private EditText edtUploadCustomFilename;

    private UguuDatabase db = UguuDatabase.getDatabase(this);

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("upload | " + username);
        }

        edtUploadPath = findViewById(R.id.edtUploadPath);
        chkUploadRanFilename = findViewById(R.id.chkUploadRanFilename);
        edtUploadCustomFilename = findViewById(R.id.edtUploadCustomFilename);
        Button btnUploadUButton = findViewById(R.id.btnUploadUButton);
        Button btnUploadFile = findViewById(R.id.btnUploadFile);

        btnUploadUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload u = getUploadInfo();
                if (u == null)
                    return;
                u = upload(u);
                if (u == null)
                    return;
                save(u);
                finish();
            }
        });

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Upload", "FILE!");
            }
        });
    }

    private Upload getUploadInfo() {
        String path = edtUploadPath.getText().toString();
        if (TextUtils.isEmpty(path)) {
            Log.d("upload", "Empty Path");
            Toast.makeText(this, "Empty Path", Toast.LENGTH_SHORT).show();
            edtUploadPath.requestFocus();
            return null;
        }

        Upload u = new Upload();
        u.setPath(path);
        u.setRandomfilename(chkUploadRanFilename.isChecked());
        u.setCustomName(edtUploadCustomFilename.getText().toString());
        return u;
    }

    private Upload upload(Upload u) {
        /* dummy */
        u.setUrl("");
        u.setName("");
        u.setUser(username);
        return u;
    }

    private void save(final Upload u) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                db.uploadDao().insert(u);
            }
        };
        Thread t = new Thread(r);
        t.start();
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
