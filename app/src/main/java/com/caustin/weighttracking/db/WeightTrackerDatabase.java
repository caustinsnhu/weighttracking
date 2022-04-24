package com.caustin.weighttracking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.caustin.weighttracking.models.Weight;

import java.util.ArrayList;
import java.util.List;

public class WeightTrackerDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "weight.db";
    private static final int VERSION = 1;


    public WeightTrackerDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User(name TEXT primary key, password TEXT);");
        db.execSQL("create table Goal(id INTEGER primary key autoincrement, target FLOAT);");
        db.execSQL("create table Weight(id INTEGER primary key autoincrement, date TEXT, value FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int currentVersion, int latestVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Goal");
        db.execSQL("drop table if exists Weight");

        onCreate(db);
    }

    public boolean insertUser(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("password", password);
        long result = db.insert("User", null, values);
        return result != -1;
    }

    public boolean insertWeight(float weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", weight);
        values.put("date", date);
        long result = db.insert("Weight", null, values);
        return result != -1;
    }

    private Cursor weightCursor(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Weight where id = ?", new String[] {String.valueOf(id)});
        return cursor;
    }

    public boolean updateWeight(int id, float newWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", newWeight);
        Cursor cursor = weightCursor(id);

        long result;
        if (cursor.getCount() > 0) {
            result = db.update("Weight", values, "id=?", new String[] {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public boolean deleteWeight(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = weightCursor(id);
        if (cursor.getCount() > 0) {
            long result = db.delete("Weight", "id=?", new String[] {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public boolean insertGoal(float target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("target", target);

        long result = db.insert("Goal", null, values);
        return result != -1;
    }

    public boolean updateGoal(int id, float target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("target", target);

        Cursor cursor = db.rawQuery("Select * from Goal where id = ?", new String[] {String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = db.update("Goal", values, "id=?", new String[] {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public List<Weight> getAllWeights() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Weight;", null);
        ArrayList<Weight> weights = new ArrayList<>();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String date = cursor.getString(1);
                    Float value = cursor.getFloat(2);
                    Weight weight = new Weight();
                    weight.setDateFromString(date);
                    weight.setWeight(value);
                    weight.setId(id);
                    weights.add(weight);
                    Log.d("Date", date);
                    Log.d("weight", String.valueOf(value));
                    Log.d("ID", String.valueOf(id));
                } while (cursor.moveToNext());
            }
        }
        return weights;
    }

    public Weight getWeight(Integer weightID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Weight where id=?;", new String[] {String.valueOf(weightID)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            Float value = cursor.getFloat(2);
            Weight weight = new Weight();
            weight.setDateFromString(date);
            weight.setWeight(value);
            weight.setId(id);

            return weight;
        } else {
            return null;
        }

    }
}
