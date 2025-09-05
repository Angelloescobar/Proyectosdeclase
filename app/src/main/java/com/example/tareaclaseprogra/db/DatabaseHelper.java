package com.example.tareaclaseprogra.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tareaclaseprogra.models.Producto;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tienda.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "productos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_ESTADO = "estado";
    public static final String COLUMN_FECHA = "fecha";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_ESTADO + " TEXT, " +
                    COLUMN_FECHA + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    public void addProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, producto.getNombre());
        values.put(COLUMN_ESTADO, producto.getEstado());
        values.put(COLUMN_FECHA, producto.getFecha());
        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                productos.add(new Producto(id, nombre, estado, fecha));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productos;
    }

    public List<Producto> getProductosByFilter(String estadoFiltro, String fechaFiltro) {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "";
        List<String> selectionArgs = new ArrayList<>();

        if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
            selection += COLUMN_ESTADO + " = ?";
            selectionArgs.add(estadoFiltro);
        }

        if (fechaFiltro != null && !fechaFiltro.isEmpty()) {
            if (!selection.isEmpty()) {
                selection += " AND ";
            }
            selection += COLUMN_FECHA + " = ?";
            selectionArgs.add(fechaFiltro);
        }

        Cursor cursor = db.query(TABLE_PRODUCTOS, null, selection, selectionArgs.toArray(new String[0]), null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                productos.add(new Producto(id, nombre, estado, fecha));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productos;
    }
     public void updateProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, producto.getNombre());
        values.put(COLUMN_ESTADO, producto.getEstado());
        values.put(COLUMN_FECHA, producto.getFecha());
        db.update(TABLE_PRODUCTOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(producto.getId())});
        db.close();
    }
}

