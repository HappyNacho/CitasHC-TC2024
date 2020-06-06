package com.happynacho.citashc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class CitaDBMS extends SQLiteOpenHelper {

    private static final String DB_FILE = "id13441212_dbbbDhgf";
    private static final String TABLE = "Citas";
    private static final String FIELD_PATIENT_ID = "patient_id";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_DESCRIPTION = "description";

    public CitaDBMS(Context context){
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE+"");

        String query = "CREATE TABLE " + TABLE + "(" +
                FIELD_PATIENT_ID + " TEXT NOT NULL, " +
                FIELD_DATE + " TEXT NOT NULL, " +
                FIELD_DESCRIPTION + " TEXT NOT NULL, "+
                "PRIMARY KEY ("+FIELD_PATIENT_ID+", "+FIELD_DATE+"))" ;

        db.execSQL(query);
        db.execSQL("delete from "+ TABLE+"");
    }



    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE;
        String[] params = {TABLE};
        db.execSQL(query, params);
    }
    public boolean insertCita(Cita cita)
    {
        SQLiteDatabase database = this.getWritableDatabase(); // We obtain an instance of our database
        ContentValues contentValues = new ContentValues(); // We insert the data using one variable of type ContentValues
        contentValues.put("patient_id", cita.getId());
        contentValues.put("date", cita.getDate());
        contentValues.put("description", cita.getDescription());
        database.insert("Citas",null,contentValues); //We insert

        return true;
    }
    public ArrayList<Cita> getAllCitas()
    {
        // This arraylist defines a personalized list containing objects of type "User"
        // Managing the instance of Table as a Java Class allows to have control of
        // data from table.

        ArrayList<Cita> citas_list = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor result = database.rawQuery("SELECT * FROM "+TABLE, null);
        Log.e(null ,"SELECT * FROM "+TABLE);
        Log.e(null ,"Resultados: "+result.getCount());

        if(result.moveToFirst()) {
            Log.e(null, "We have data, OHH YEAAAH!!");
            Log.e(null, result.getString(result.getColumnIndex(FIELD_DATE)));

            do  {
                Cita current_cita = new Cita(
                        result.getString(result.getColumnIndex(FIELD_PATIENT_ID)),
                        result.getString(result.getColumnIndex(FIELD_DATE)),
                        result.getString(result.getColumnIndex(FIELD_DESCRIPTION))
                );
                citas_list.add(current_cita);
            } while(result.moveToNext());
        }
        else
            Log.e(null, "Oh no, no data!!");

        return citas_list;
    }
}
