package com.example.tarea2programacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea2programacion.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private String nationality;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        nationality = getIntent().getStringExtra("nationality");

        binding.tvNationality.setText("Registrando: " + nationality);

        binding.chronometer.start();

        binding.btnPlaceFingerprint.setOnClickListener(v -> {
            binding.chronometer.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - binding.chronometer.getBase();
            double seconds = elapsedMillis / 1000.0;

            dbHelper.addRecord(nationality, "Registrado", seconds);

            // Regresar a MainActivity
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
