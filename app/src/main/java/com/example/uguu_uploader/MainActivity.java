package com.example.uguu_uploader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uguu_uploader.adapter.UploadAdapter;
import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.Upload;
import com.example.uguu_uploader.request.MultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static int NEWUPLOAD = 0;
    private List<Upload> uploads = new ArrayList<>();
    private UploadAdapter uploadAdapter = new UploadAdapter(uploads);

    private UguuDatabase db = UguuDatabase.getDatabase(this);

    //private static String url = "http://172.16.0.2/api.php?id=upload-tool"; // DEBUG
    //private static String url = "http://172.16.0.2/api.php"; // DEBUG
    private static String url = "https://uguu.se/api.php?d=upload-tool";

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
                upload(u);
                //save(u);
                uploadAdapter.addItem(u);
            }
        }
    }

    private void upload(final Upload u) {
        //RequestQueue queue = Volley.newRequestQueue(this);
        /*StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Upload File", "Uploaded!");
                Log.d("Upload File", response);
                save(u);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Upload File", "Error uploading file: " + error.toString());
                u.setUrl("fail");
                save(u);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("Upload File", "getParams executed");
                Map<String, String> params = new HashMap<String, String> ();

                params.put("file", u.getPath());
                if (!TextUtils.isEmpty(u.getCustomName()))
                    params.put("name", u.getCustomName());
                if (u.isRandomfilename())
                    params.put("randomname", "a");
                return params;
            }
        };*/
        //queue.add(stringRequest);
        /*MultipartRequest multipartRequest = new MultipartRequest(Request.Method.DEPRECATED_GET_OR_POST, url, new Response<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Upload File", "Uploaded!");
                Log.d("Upload File", resultResponse);
                save(u);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    errorMessage = new String(networkResponse.data);
                }
                Log.e("Upload File", errorMessage);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("Upload File", "getParams executed");
                Map<String, String> params = new HashMap<String, String> ();

                params.put("file", u.getPath());
                if (!TextUtils.isEmpty(u.getCustomName()))
                    params.put("name", u.getCustomName());
                if (u.isRandomfilename())
                    params.put("randomname", "a");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>()
            }
        }*/
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

    private void update(final Upload u) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                db.uploadDao().update(u);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
