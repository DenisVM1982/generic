package com.example.generic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final boolean fromExternalSource = false;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listView = (ListView) findViewById(R.id.listView);

        // Request for permission to read external storage
        if (fromExternalSource && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            showQuotes();
        }
    }

    private void showQuotes() {
        DatabaseAccess databaseAccess;
        if (fromExternalSource) {
            // Check the external database file. External database must be available for the first time deployment.
            String externalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";
            File dbFile = new File(externalDirectory, DatabaseOpenHelper.DATABASE_NAME);
            if (!dbFile.exists()) {
                return;
            }
            // If external database is avaliable, deploy it
            databaseAccess = DatabaseAccess.getInstance(this, externalDirectory);
        } else {
            // From assets
            databaseAccess = DatabaseAccess.getInstance(this, null);
        }

        databaseAccess.open();
        ArrayList<String> quotes1 = (ArrayList<String>) databaseAccess.getQuotes1();

        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quotes1);
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(id==0) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showQuotes();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the quotes", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
