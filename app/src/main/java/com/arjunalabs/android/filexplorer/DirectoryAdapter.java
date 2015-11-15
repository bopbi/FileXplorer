package com.arjunalabs.android.filexplorer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bobbyadiprabowo on 11/15/15.
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {

    private ArrayList<File> fileList;
    private ArrayList<File> dirList;
    private FileListSelected fileListSelected;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtTitle;
        public boolean isDir;
        public String path;
        public FileListSelected fileListSelected;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.item_file_title);
            isDir = false;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // only handle is directory
            if (isDir) {
                fileListSelected.openDirectory(path, false);
            }
        }
    }

    public DirectoryAdapter(FileListSelected fileListSelected) {
        this.fileListSelected = fileListSelected;
    }

    @Override
    public DirectoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        DirectoryAdapter.ViewHolder vh = new DirectoryAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DirectoryAdapter.ViewHolder holder, int position) {
        // show directory first then file
        boolean isDir = false;
        if (position < dirList.size()) {
            isDir = true;
        }

        String title;
        if (isDir) {
            File directory = dirList.get(position);
            title = "(Dir)" + directory.getName();
            holder.path = directory.getAbsolutePath();
        } else {
            title = fileList.get(position - dirList.size()).getName();
            holder.path = null;
        }

        holder.fileListSelected = fileListSelected;
        holder.isDir = isDir;
        holder.txtTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        if (dirList == null) {
            return 0;
        }

        if (fileList == null) {
            return 0;
        }

        return dirList.size() + fileList.size();
    }

    public void setFileList(ArrayList<File> fileList) {
        this.fileList = fileList;
    }

    public void setDirList(ArrayList<File> dirList) {
        this.dirList = dirList;
    }

    public interface FileListSelected {

        void openDirectory(String openPath, boolean back);
    }
}
