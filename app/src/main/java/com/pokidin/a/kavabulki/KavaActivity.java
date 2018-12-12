package com.pokidin.a.kavabulki;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class KavaActivity extends AppCompatActivity {
    public static final String EXTRA_KAVANO = "kavaNo";
    private static final String KEY_COUNT = "count";
    private static final String KEY_COST = "cost";

    private int count = 0;
    private int cost;
    private int priceNum;
    private String nameText;
    private int kavaNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kava);

        kavaNo = (int) Objects.requireNonNull(getIntent().getExtras()).get(EXTRA_KAVANO);

        try {
            new ReadDatabaseTask().execute(kavaNo);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt(KEY_COUNT);
            cost = savedInstanceState.getInt(KEY_COST);
        }

        displayCost(cost);
        displayCount(count);
    }

    public void onFavoriteClicked(View view) {
        new FavoriteDrinksTask().execute(kavaNo);
    }

    private void calculatePrice(int count) {
        cost = priceNum * count;
        displayCost(cost);
    }

    public void increment(View view) {
        if (count > 4) {
            Toast maxToast = Toast.makeText(getApplicationContext(),
                    "No more", Toast.LENGTH_SHORT);
            maxToast.show();
            return;
        }
        count++;
        displayCount(count);
        calculatePrice(count);
    }

    public void decrement(View view) {
        if (count < 2) {
            Toast minToast = Toast.makeText(getApplicationContext(),
                    "No less", Toast.LENGTH_SHORT);
            minToast.show();
            return;
        }
        count--;
        displayCount(count);
        calculatePrice(count);
    }

    private void displayCount(int count) {
        TextView number = (TextView) findViewById(R.id.item_count);
        number.setText("" + count);
    }

    private void displayCost(int cost) {
        TextView costView = (TextView) findViewById(R.id.cost_text);
        costView.setText("" + cost);
    }

    public void submitOrder(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:kavabulki@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, createOrderSubject());
        intent.putExtra(Intent.EXTRA_TEXT, createOrderMessage());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String createOrderSubject() {
        EditText editName = (EditText) findViewById(R.id.customer_name);
        String customer_Name = editName.getText().toString();
        String orderSubject = nameText;
        orderSubject += ", for " + customer_Name;
        return orderSubject;
    }

    private String createOrderMessage() {
        String orderMessage = nameText + "\n";
        orderMessage += "Price: " + priceNum + " UAH \n";
        orderMessage += "Count: " + count + "\n";
        orderMessage += "Cost: " + cost + " UAH";
        return orderMessage;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_COST, cost);
        outState.putInt(KEY_COUNT, count);
    }

    private class ReadDatabaseTask extends AsyncTask<Integer, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Integer... drinks) {
            int kavaNo = drinks[0];
            SQLiteOpenHelper kavaDatabaseHelper = new KavaDatabaseHelper(KavaActivity.this);
            SQLiteDatabase db = kavaDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "PRICE", "IMAGE_RESOURCE_ID", "DESCRIPTION_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(kavaNo)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                return cursor;
            }
            db.close();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            nameText = cursor.getString(0);
            priceNum = cursor.getInt(1);
            String priceText = Integer.toString(priceNum);
            int imageId = cursor.getInt(2);
            int descriptionId = cursor.getInt(3);
            boolean isFavorite = (cursor.getInt(4) == 1);

            TextView name = (TextView) findViewById(R.id.item_name);
            name.setText(nameText);

            TextView price = (TextView) findViewById(R.id.item_price);
            price.setText(priceText);

            ImageView photo = (ImageView) findViewById(R.id.item_view);
            photo.setImageResource(imageId);
            photo.setContentDescription(nameText);

            TextView description = (TextView) findViewById(R.id.description);
            description.setText(descriptionId);

            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            favorite.setChecked(isFavorite);

            cursor.close();
        }
    }

    private class FavoriteDrinksTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE", favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int kavaNo = drinks[0];
            SQLiteOpenHelper kavaDatabaseHelper = new KavaDatabaseHelper(KavaActivity.this);
            {
                try {
                    SQLiteDatabase db = kavaDatabaseHelper.getWritableDatabase();
                    db.update("DRINK", drinkValues, "_id = ?",
                            new String[]{Integer.toString(kavaNo)});
                    db.close();
                    return true;
                } catch (SQLiteException e) {
                    return false;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(KavaActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
