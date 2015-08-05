package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.myapplication.db.MySqliteHelper;

import java.util.Random;

public class DatabaseActivity extends ActionBarActivity implements View.OnClickListener {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        MySqliteHelper helper = new MySqliteHelper(this);
        db = helper.getWritableDatabase();
        findViewById(R.id.button_add_product).setOnClickListener(this);
        findViewById(R.id.button_refresh).setOnClickListener(this);
        int version = db.getVersion();
        Log.i("DatabaseActivity.onCreate", "-----> " + version);
        initListView();
    }

    public void insertProduct(String name){
        ContentValues values = new ContentValues();
        values.put("id", new Random().nextInt(100000));
        values.put("name", name);
        long row = db.insert("Product", null, values);
        if(row >= 0){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add_product){
            insertProduct("Name " + System.currentTimeMillis());
        }else if(view.getId() == R.id.button_refresh){
            refresh();
        }
    }

    public void refresh(){
        if(cursor != null && !cursor.isClosed())
            cursor.close();
        initListView();
    }

    Cursor cursor;
    public void initListView(){
        ListView listView = (ListView) findViewById(R.id.listview);
        cursor = db.query("Product",
                        new String[]{"_id","name"},
                        null,
                        null,
                        null,
                        null,
                        null
                );

        CursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{"name"},
                new int[]{android.R.id.text1}

        );

        listView.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
