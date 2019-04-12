package com.example.generic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class Main2Activity extends AppCompatActivity {

    private static final String[][] dataToShow = { { "This", "is", "a", "test" },
            { "and", "a", "second", "test" } };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TableView Tableview = ( TableView ) findViewById ( R.id.Tableview);
        Tableview.setDataAdapter(new SimpleTableDataAdapter(this, dataToShow));
    }
}
