package com.example.esquiv2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BDHelper extends SQLiteOpenHelper {

    private Context context;

    public BDHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String linea;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.bdestacionesesqui);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while((linea = br.readLine()) != null) {
                db.execSQL(linea);
            }

            br.close();
            is.close();
        } catch (Exception e) {
            Toast.makeText(context, "Error al leer fichero SQL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Eliminar la tabla si existe y crearla nuevamente
        db.execSQL("DROP TABLE IF EXISTS esqui"); // Corregido: Nombre de la tabla 'esqui'
        onCreate(db);
    }

}
