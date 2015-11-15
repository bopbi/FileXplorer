package com.arjunalabs.android.filexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements DirectoryAdapter.FileListSelected {

    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 9;
    private RecyclerView recyclerView;
    private DirectoryAdapter directoryAdapter;
    private ArrayList<File> fileList;
    private ArrayList<File> dirList;
    private LinkedList<String> historyPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileList = new ArrayList<>();
        dirList = new ArrayList<>();
        historyPath = new LinkedList<>();

        directoryAdapter = new DirectoryAdapter(this);
        recyclerView.setAdapter(directoryAdapter);
        directoryAdapter.notifyDataSetChanged();

        //check permission
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL);
            }
        } else {
            openDirectory(getRootPath(), false);
        }
    }

    private String getRootPath() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openDirectory(getRootPath(), false);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        // handle when drawer is closed
        if (historyPath.size() == 1) {
            super.onBackPressed();
        } else {
            // remove last
            historyPath.removeLast();

            // go to last path
            openDirectory(historyPath.getLast(), true);
        }
    }

    public void openDirectory(String openPath, boolean back) {

        if (openPath == null) {
            return;
        }

        if (back == false) {
            historyPath.addLast(openPath);
        }

        dirList.clear();
        fileList.clear();

        File sdFile = new File(openPath);
        File files[] = sdFile.listFiles();
        if (files == null) {
            return;
        }

        // add to list
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                dirList.add(inFile);
            } else {
                fileList.add(inFile);
            }
        }

        // sort the file / directory here
        Collections.sort(dirList);
        Collections.sort(fileList);

        directoryAdapter.setFileList(fileList);
        directoryAdapter.setDirList(dirList);
        directoryAdapter.notifyDataSetChanged();
    }
}
