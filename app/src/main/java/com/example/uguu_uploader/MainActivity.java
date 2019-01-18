package com.example.uguu_uploader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.uguu_uploader.adapter.UploadAdapter;
import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.Upload;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int NEWUPLOAD = 0;
    private List<Upload> uploads = new ArrayList<>();
    private UploadAdapter uploadAdapter = new UploadAdapter(uploads);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        toolbar.setTitle("Uguu Uploader | " + username);

        setSupportActionBar(toolbar);

        setUpRecyclerView(username);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                i.putExtra("username", username);
                startActivityForResult(i, NEWUPLOAD);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpRecyclerView(final String username) {
        final RecyclerView lstUploadList;
        lstUploadList = findViewById(R.id.lstUploadList);
        lstUploadList.setHasFixedSize(true);
        lstUploadList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        lstUploadList.setAdapter(uploadAdapter);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                final UguuDatabase db = UguuDatabase.getDatabase(MainActivity.this);
                List<Upload> ups = db.uploadDao().getByUsername(username);
                uploads.addAll(ups);
                uploadAdapter.notifyItemRangeInserted(0, ups.size());
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEWUPLOAD) {
            if (resultCode == RESULT_OK) {
                Upload u = data.getExtras().getParcelable("newupload");
                uploadAdapter.addItem(u);
            }
        }
    }
}
