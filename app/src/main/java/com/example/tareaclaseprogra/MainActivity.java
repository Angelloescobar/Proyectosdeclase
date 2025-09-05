package com.example.tareaclaseprogra;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaclaseprogra.adapters.ProductoAdapter;
import com.example.tareaclaseprogra.db.DatabaseHelper;
import com.example.tareaclaseprogra.models.Producto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_EDIT_PRODUCTO_REQUEST = 1;

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productoList;
    private DatabaseHelper db;
    private Spinner spinnerEstado;
    private Button btnFecha;
    private Button btnReporte;
    private FloatingActionButton fabAdd;

    private String filtroEstado = "";
    private String filtroFecha = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        productoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductoAdapter(productoList, producto -> {
            Intent intent = new Intent(MainActivity.this, AddEditProductoActivity.class);
            intent.putExtra("producto_id", producto.getId());
            intent.putExtra("producto_nombre", producto.getNombre());
            intent.putExtra("producto_estado", producto.getEstado());
            intent.putExtra("producto_fecha", producto.getFecha());
            startActivityForResult(intent, ADD_EDIT_PRODUCTO_REQUEST);
        });
        recyclerView.setAdapter(adapter);

        spinnerEstado = findViewById(R.id.spinner_estado);
        btnFecha = findViewById(R.id.btn_fecha);
        btnReporte = findViewById(R.id.btn_reporte);
        fabAdd = findViewById(R.id.fab_add);

        setupFilters();
        loadProductos();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditProductoActivity.class);
            startActivityForResult(intent, ADD_EDIT_PRODUCTO_REQUEST);
        });

        btnReporte.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra("filtro_estado", filtroEstado);
            intent.putExtra("filtro_fecha", filtroFecha);
            startActivity(intent);
        });
    }

    private void setupFilters() {
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Todos", "Pendiente", "Comprado"});
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("Todos")) {
                    filtroEstado = "";
                } else {
                    filtroEstado = selected;
                }
                loadProductos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filtroEstado = "";
                loadProductos();
            }
        });

        btnFecha.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    filtroFecha = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    Toast.makeText(this, "Fecha seleccionada: " + filtroFecha, Toast.LENGTH_SHORT).show();
                    loadProductos();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadProductos() {
        productoList.clear();
        productoList.addAll(db.getProductosByFilter(filtroEstado, filtroFecha));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_PRODUCTO_REQUEST && resultCode == RESULT_OK) {
            loadProductos();
        }
    }
}