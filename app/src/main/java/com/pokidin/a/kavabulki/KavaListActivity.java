package com.pokidin.a.kavabulki;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class KavaListActivity extends ListActivity {
    private static final String TAG = "KavaListActivity";
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listDrinks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "List started to create");
        listDrinks = getListView();

        try {
            new ReadDatabaseTask().execute();
            listDrinks.setBackgroundResource(R.color.backgroundColor);
        } catch (SQLiteException e) {
            Log.i(TAG, "DB Exception");
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(KavaListActivity.this, KavaActivity.class);
        intent.putExtra(KavaActivity.EXTRA_KAVANO, (int) id);
        startActivity(intent);
        Log.i(TAG, "Intent started");
    }

    private class ReadDatabaseTask extends AsyncTask<Void, Void, CursorAdapter> {

        @Override
        protected CursorAdapter doInBackground(Void... voids) {
            SQLiteOpenHelper kavaDatabaseHelper = new KavaDatabaseHelper(KavaListActivity.this);
            db = kavaDatabaseHelper.getReadableDatabase();
            cursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);
            return new SimpleCursorAdapter(KavaListActivity.this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
        }

        @Override
        protected void onPostExecute(CursorAdapter listAdapter) {
            listDrinks.setAdapter(listAdapter);
            Log.i(TAG, "List created");
        }
    }
}
