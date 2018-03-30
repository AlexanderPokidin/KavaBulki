package com.pokidin.a.kavabulki;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends AppCompatActivity {
    private static final String TAG = "TopLevelActivity";

    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    ListView listFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        Button button = (Button) findViewById(R.id.button_menu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopLevelActivity.this, KavaListActivity.class);
                startActivity(intent);
                Log.i(TAG, "Intent started");
            }
        });

        listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            new FavoriteListTask().execute();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(TopLevelActivity.this, KavaActivity.class);
                intent.putExtra(KavaActivity.EXTRA_KAVANO, (int) id);
                startActivity(intent);
            }
        });
    }

    private class FavoriteListTask extends AsyncTask<Void, Void, CursorAdapter> {

        @Override
        protected CursorAdapter doInBackground(Void... voids) {
            SQLiteOpenHelper kavaDatabaseHelper = new KavaDatabaseHelper(TopLevelActivity.this);
            db = kavaDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(TopLevelActivity.this,
                    android.R.layout.simple_list_item_1,
                    favoritesCursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            return favoriteAdapter;
        }

        @Override
        protected void onPostExecute(CursorAdapter favoriteAdapter) {
            listFavorites.setAdapter(favoriteAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            SQLiteOpenHelper kavaDatabaseHelper = new KavaDatabaseHelper(this);
            db = kavaDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                    null, null, null, null);
            ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            adapter.changeCursor(newCursor);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
