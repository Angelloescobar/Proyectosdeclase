package com.example.tareaclaseprogra;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tareaclaseprogra.db.DatabaseHelper;
import com.example.tareaclaseprogra.models.Producto;

import java.util.Calendar;

public class AddEditProductoActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private Spinner spinnerEstado;
    private Button btnFecha;
    private TextView textViewFecha;
    private Button btnGuardar;
    private DatabaseHelper db;
    private String selectedDate;
    private Producto productoToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_producto);

        editTextNombre = findViewById(R.id.edit_text_nombre);
        spinnerEstado = findViewById(R.id.spinner_estado_add_edit);
        btnFecha = findViewById(R.id.btn_fecha_add_edit);
        textViewFecha = findViewById(R.id.text_view_fecha_seleccionada);
        btnGuardar = findViewById(R.id.btn_guardar);
        db = new DatabaseHelper(this);

        // Setup Spinner
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Pendiente", "Comprado"});
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        // Check for existing product
        if (getIntent().hasExtra("producto_id")) {
            setTitle("Editar Producto");
            int productoId = getIntent().getIntExtra("producto_id", -1);
            // This is a simplified way. A better approach would be to fetch the product from DB by ID.
            // For now, we'll pass the whole object.
            String nombre = getIntent().getStringExtra("producto_nombre");
            String estado = getIntent().getStringExtra("producto_estado");
            String fecha = getIntent().getStringExtra("producto_fecha");
            productoToEdit = new Producto(productoId, nombre, estado, fecha);

            editTextNombre.setText(productoToEdit.getNombre());
            if (productoToEdit.getEstado().equals("Comprado")) {
                spinnerEstado.setSelection(1);
            } else {
                spinnerEstado.setSelection(0);
            }
            selectedDate = productoToEdit.getFecha();
            textViewFecha.setText(selectedDate);
        } else {
            setTitle("Agregar Producto");
        }


        btnFecha.setOnClickListener(v -> showDatePickerDialog());

        btnGuardar.setOnClickListener(v -> saveProducto());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    textViewFecha.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveProducto() {
        String nombre = editTextNombre.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem().toString();

        if (nombre.isEmpty() || selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productoToEdit != null) {
            productoToEdit.setNombre(nombre);
            productoToEdit.setEstado(estado);
            productoToEdit.setFecha(selectedDate);
            db.updateProducto(productoToEdit);
        } else {
            Producto newProducto = new Producto(0, nombre, estado, selectedDate);
            db.addProducto(newProducto);
        }

        setResult(RESULT_OK);
        finish();
    }
}

