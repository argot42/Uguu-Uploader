package com.example.uguu_uploader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.uguu_uploader.adapter.UploadAdapter;
import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.Upload;
import com.example.uguu_uploader.request.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private static int NEWUPLOAD = 0;
    private static int NEWPHOTO = 1;

    private static int WRITE_EXTERNAL_STORAGE_REQUEST = 0;

    private List<Upload> uploads = new ArrayList<>();
    private UploadAdapter uploadAdapter = new UploadAdapter(uploads);

    private String username;

    private Uri photoURI = null;

    private UguuDatabase db = UguuDatabase.getDatabase(this);

    private static String url = "http://172.16.0.2/api.php?d=upload-tool"; // DEBUG
    //private static String url = "http://192.168.0.12/api.php?d=upload-tool"; // DEBUG
    //private static String url = "https://uguu.se/api.php?d=upload-tool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        toolbar.setTitle("Uguu Uploader | " + username);

        setSupportActionBar(toolbar);

        setUpRecyclerView(username);

        // Open upload activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                i.putExtra("username", username);
                startActivityForResult(i, NEWUPLOAD);
            }
        });

        // Open camera
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) == null) {
                    return false;
                }
                File photoFile;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                /*if (photoFile == null) {
                    return false;
                }*/
                photoURI = FileProvider.getUriForFile(
                        getApplicationContext(),
                        "com.example.uguu_uploader.fileprovider",
                        photoFile
                );
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(i, NEWPHOTO);
                return true;
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
                save(u);
                uploadAdapter.addItem(u);
                upload(u);
            }
        }
        else if (requestCode == NEWPHOTO && resultCode == RESULT_OK) {
            Log.d("Picture", "Picture taken!");

            Upload u = new Upload();
            u.setUri(photoURI);
            u.setUser(username);
            u.setName(u.queryName(getContentResolver()));
            u.setRandomfilename(true);
            u.setCustomName("");

            save(u);
            uploadAdapter.addItem(u);
            upload(u);
        }
    }

    private void upload(final Upload u) {
        RequestQueue queue = Volley.newRequestQueue(this);
        MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.d("Upload File", resultResponse);

                if (URLUtil.isValidUrl(resultResponse)) {
                    u.setUrl(resultResponse);
                } else {
                    u.setUrl("fail");
                }
                update(u);
                uploadAdapter.notifyDataSetChanged();
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

                u.setUrl("fail");
                update(u);
                uploadAdapter.notifyDataSetChanged();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("Upload File", "getParams executed");
                Map<String, String> params = new HashMap<String, String>();

                if (!TextUtils.isEmpty(u.getCustomName()))
                    params.put("name", u.getCustomName());
                if (u.isRandomfilename())
                    params.put("randomname", "a");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                ContentResolver cr = getApplicationContext().getContentResolver();
                String filename = u.getName();
                String mimeType = u.queryType(cr);
                byte[] data = u.queryBody(cr);
                if (data == null)
                    return params;

                params.put("file", new DataPart(
                        filename,
                        data,
                        mimeType
                        )
                );
                return params;
            }
        };
        queue.add(multipartRequest);
    }

    private void save(final Upload u) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // TODO: clean this
                long i = db.uploadDao().insert(u);
                u.setId(i);
                Log.d("database", "saved! " + i + " -> " + u.toString());
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void update(final Upload u) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // TODO: clean this
                int i = db.uploadDao().update(u);
                Log.d("database", "updated! " + i);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "picture_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("createImageFile", storageDir.getPath());
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }
}