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

        insertDrink(db, "Espresso 30 ml", 12, R.drawable.espresso_item, R.string.espresso);

        insertDrink(db, "Americano 175 ml", 12, R.drawable.americano_item, R.string.americano);
        insertDrink(db, "Americano 270 ml", 20, R.drawable.americano_item, R.string.americano);
        insertDrink(db, "Americano 400 ml", 22, R.drawable.americano_item, R.string.americano);

        insertDrink(db, "Raf 175 ml", 25, R.drawable.raf_item, R.string.raf);
        insertDrink(db, "Raf 270 ml", 30, R.drawable.raf_item, R.string.raf);
        insertDrink(db, "Raf 400 ml", 40, R.drawable.raf_item, R.string.raf);

        insertDrink(db, "Latte 270 ml", 20, R.drawable.latte_item, R.string.latte);
        insertDrink(db, "Latte 400 ml", 22, R.drawable.latte_item, R.string.latte);
        insertDrink(db, "Latte 500 ml", 30, R.drawable.latte_item, R.string.latte);

        insertDrink(db, "Lavender latte 270 ml", 25, R.drawable.lavlate_item, R.string.lav_latte);
        insertDrink(db, "Lavender latte 400 ml", 30, R.drawable.lavlate_item, R.string.lav_latte);

        insertDrink(db, "Cappuccino 270 ml", 18, R.drawable.capuccino_item, R.string.capuccino);
        insertDrink(db, "Cappuccino 400 ml", 22, R.drawable.capuccino_item, R.string.capuccino);
        insertDrink(db, "Cappuccino 500 ml", 30, R.drawable.capuccino_item, R.string.capuccino);

        insertDrink(db, "Flat white 270 ml", 25, R.drawable.flatewhite_item, R.string.flat_white);

        insertDrink(db, "Cacao 270 ml", 20, R.drawable.cacao_item, R.string.cacao);
        insertDrink(db, "Cacao 400 ml", 22, R.drawable.cacao_item, R.string.cacao);
        insertDrink(db, "Cacao 500 ml", 30, R.drawable.cacao_item, R.string.cacao);

        insertDrink(db, "Hot chocolate 270 ml", 25, R.drawable.chocolate_item, R.string.chocolate);
        insertDrink(db, "Hot chocolate 400 ml", 35, R.drawable.chocolate_item, R.string.chocolate);

        insertDrink(db, "Coffee juice 270 ml", 25, R.drawable.kavajuice_item, R.string.juice);
        insertDrink(db, "Coffee juice 400 ml", 30, R.drawable.kavajuice_item, R.string.juice);
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
