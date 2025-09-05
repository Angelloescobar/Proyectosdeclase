package com.example.tarea2programacion;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea2programacion.databinding.ActivityReportBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        loadNationalities();

        binding.btnApplyFilter.setOnClickListener(v -> {
            String nationality = binding.spinnerNationalityFilter.getSelectedItem().toString();
            if ("Todas".equals(nationality)) {
                nationality = null;
            }
            String minTimeStr = binding.etMinTime.getText().toString();
            String maxTimeStr = binding.etMaxTime.getText().toString();

            double minTime = -1;
            if (!minTimeStr.isEmpty()) {
                minTime = Double.parseDouble(minTimeStr);
            }

            double maxTime = -1;
            if (!maxTimeStr.isEmpty()) {
                maxTime = Double.parseDouble(maxTimeStr);
            }

            generateReport(nationality, minTime, maxTime);
        });

        // Cargar reporte inicial sin filtros
        generateReport(null, -1, -1);
    }

    private void loadNationalities() {
        List<String> nationalities = new ArrayList<>();
        nationalities.add("Todas");
        nationalities.add("El Salvador");
        nationalities.add("Honduras");
        nationalities.add("Nicaragua");
        nationalities.add("Costa Rica");
        // No agregamos Guatemala ya que no se registra huella

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nationalities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNationalityFilter.setAdapter(adapter);
    }

    private void generateReport(String nationality, double minTime, double maxTime) {
        Cursor cursor = dbHelper.getRecords(nationality, minTime, maxTime);
        StringBuilder report = new StringBuilder();
        report.append("--- Reporte de Registros ---\n\n");

        if (cursor.getCount() == 0) {
            report.append("No se encontraron registros con los filtros aplicados.");
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NATIONALITY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));
                double time = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REGISTRATION_TIME));

                report.append("ID: ").append(id).append("\n");
                report.append("Nacionalidad: ").append(nat).append("\n");
                report.append("Estado: ").append(status).append("\n");
                if (time > 0) {
                    report.append("Tiempo: ").append(String.format(Locale.US, "%.2f", time)).append("s\n");
                }
                report.append("-----------------------------\n");
            }
        }
        cursor.close();
        binding.tvReportResults.setText(report.toString());
    }
}
