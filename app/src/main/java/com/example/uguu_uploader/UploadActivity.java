package com.example.uguu_uploader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uguu_uploader.model.Upload;

public class UploadActivity extends AppCompatActivity {

    private static int PICKFILE = 0;

    private CheckBox chkUploadRanFilename;
    private EditText edtUploadCustomFilename;

    private String username;

    private Uri uri = null;

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
                Log.d("Upload", u.toString());

                Intent output = new Intent();
                output.putExtra("newupload", u);
                setResult(Activity.RESULT_OK, output);
                finish();
            }
        });

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, PICKFILE);
            }
        });
    }

    private Upload getUploadInfo() {
        if (uri == null) {
            String msg = "No file selected";
            Log.d("upload", msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return null;
        }

        Upload u = new Upload();
        u.setUri(uri);
        u.setRandomfilename(chkUploadRanFilename.isChecked());
        u.setCustomName(edtUploadCustomFilename.getText().toString());
        u.setUser(username);
        u.setName(uri.getLastPathSegment());
        return u;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICKFILE && resultCode == Activity.RESULT_OK) {
            if (resultData == null)
                return;
            uri = resultData.getData();
            if (uri == null)
                return;
            Log.d("PickFile", "Uri: " + uri.toString());

            TextView tvUploadPath = findViewById(R.id.tvUploadPath);
            tvUploadPath.setText(uri.getPath());
        }
    }
}
