package com.example.tareaclaseprogra;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tareaclaseprogra.adapters.ProductoAdapter;
import com.example.tareaclaseprogra.db.DatabaseHelper;
import com.example.tareaclaseprogra.models.Producto;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReport;
    private ProductoAdapter adapter;
    private List<Producto> productoList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setTitle("Reporte de Productos");

        recyclerViewReport = findViewById(R.id.recycler_view_report);
        recyclerViewReport.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        String filtroEstado = getIntent().getStringExtra("filtro_estado");
        String filtroFecha = getIntent().getStringExtra("filtro_fecha");

        productoList = db.getProductosByFilter(filtroEstado, filtroFecha);

        adapter = new ProductoAdapter(productoList, producto -> {
            // No action on click in report
        });
        recyclerViewReport.setAdapter(adapter);
    }
}

