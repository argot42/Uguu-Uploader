package com.example.uguu_uploader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.uguu_uploader.R;
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
            this.tvUploadRowName = base.findViewById(R.id.tvUploadRowName);
            this.tvUploadRowState = base.findViewById(R.id.tvUploadRowState);
            this.btnUploadRowDelete = base.findViewById(R.id.btnUploadRowDelete);
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
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }
}
