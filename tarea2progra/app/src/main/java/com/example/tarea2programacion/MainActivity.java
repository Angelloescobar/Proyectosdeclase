package com.example.tarea2programacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea2programacion.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        loadNationalities();

        binding.btnStartRegistration.setOnClickListener(v -> {
            String selectedNationality = binding.spinnerNationality.getSelectedItem().toString();
            if ("Guatemala".equals(selectedNationality)) {
                Toast.makeText(MainActivity.this, "Guatemala está exenta de registro de huella.", Toast.LENGTH_LONG).show();
                dbHelper.addRecord(selectedNationality, "Exento", 0);
            } else {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                intent.putExtra("nationality", selectedNationality);
                startActivity(intent);
            }
        });

        binding.btnViewReports.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });
    }

    private void loadNationalities() {
        List<String> nationalities = new ArrayList<>();
        nationalities.add("Guatemala");
        nationalities.add("El Salvador");
        nationalities.add("Honduras");
        nationalities.add("Nicaragua");
        nationalities.add("Costa Rica");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nationalities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNationality.setAdapter(adapter);
    }
}