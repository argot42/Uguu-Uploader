package com.example.uguu_uploader.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uguu_uploader.R;
import com.example.uguu_uploader.dao.UguuDatabase;
import com.example.uguu_uploader.model.Upload;

import java.util.List;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.UploadViewHolder> {
    private List<Upload> uploadList;

    public class UploadViewHolder extends RecyclerView.ViewHolder {
        TextView tvUploadRowName;
        TextView tvUploadRowState;
        Button btnUploadRowDelete;

        public UploadViewHolder(View base) {
            super(base);
            tvUploadRowName = base.findViewById(R.id.tvUploadRowName);
            tvUploadRowState = base.findViewById(R.id.tvUploadRowState);
            btnUploadRowDelete = base.findViewById(R.id.btnUploadRowDelete);

            final Context ctx = base.getContext();

            btnUploadRowDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(ctx, getAdapterPosition());
                }
            });

            tvUploadRowName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return copyToClipboard(ctx, getAdapterPosition());
                }
            });
        }

        private void removeItem(final Context ctx, int index) {
            final Upload u = uploadList.get(index);
            uploadList.remove(index);
            notifyItemRemoved(index);

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    UguuDatabase db = UguuDatabase.getDatabase(ctx);
                    db.uploadDao().delete(u);
                }
            };
            Thread t = new Thread(r);
            t.start();
        }

        private boolean copyToClipboard(Context ctx, int index) {
            Upload u = uploadList.get(index);
            switch(u.getStatus()) {
                case FAIL:
                    return false;
                case SUCCESS:
                    ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(ctx.CLIPBOARD_SERVICE);
                    if (clipboard == null) {
                        Log.d("UploadAdapter", "Error getting Clipboard Manager");
                        return false;
                    }
                    ClipData clip = ClipData.newPlainText("url", u.getUrl());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ctx, "Copied " + u.getUrl() + " to Clipboard", Toast.LENGTH_LONG).show();
                    return true;
                case UPLOADING:
                    Toast.makeText(ctx, "File being uploaded", Toast.LENGTH_LONG).show();
                    return false;
                default:
                    Log.d("UploadAdapter", "???");
                    return false;
            }
        }
    }

    @Override
    public UploadAdapter.UploadViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        return new UploadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_row, parent, false));
    }

    public UploadAdapter(List<Upload> uploadList) {
        this.uploadList = uploadList;
    }

    @Override
    public void onBindViewHolder(UploadViewHolder holder, int position) {
        Upload u = uploadList.get(position);
        holder.tvUploadRowName.setText(u.getName());
        switch(u.getStatus()) {
            case FAIL:
                holder.tvUploadRowState.setText("Failed");
                break;
            case UPLOADING:
                holder.tvUploadRowState.setText("Uploading");
                break;
            case SUCCESS:
                holder.tvUploadRowState.setText("Uploaded");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public void addItem(Upload u) {
        uploadList.add(u);
        notifyItemInserted(this.getItemCount() - 1);
    }
}
