package com.pokidin.a.kavabulki;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KavaDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "kavabulki";
    private static final int DB_VERSION = 1;

    public KavaDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DRINK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "PRICE INTEGER, "
                + "IMAGE_RESOURCE_ID INTEGER, "
                + "DESCRIPTION_RESOURCE_ID INTEGER, "
                + "FAVORITE NUMERIC);");

        insertDrink(db, "Cacao 270 ml", 20, R.drawable.cacao_item, R.string.cacao);
        insertDrink(db, "Cacao 400 ml", 22, R.drawable.cacao_item, R.string.cacao);
        insertDrink(db, "Cacao 500 ml", 30, R.drawable.cacao_item, R.string.cacao);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void insertDrink(SQLiteDatabase db, String name, int price,
                                    int imageResId, int descriptionResId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("PRICE", price);
        drinkValues.put("IMAGE_RESOURCE_ID", imageResId);
        drinkValues.put("DESCRIPTION_RESOURCE_ID", descriptionResId);
        db.insert("DRINK", null, drinkValues);
    }
}
