package com.rafiif.apk_cuaca_uts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class pilih_kota extends AppCompatActivity {
    EditText namaKota;
    Button cekCuaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilihkota);  // pastikan layout ini ada

        namaKota = findViewById(R.id.nama_kota);
        cekCuaca = findViewById(R.id.cek_cuaca);

        cekCuaca.setOnClickListener(v -> {
            String kota = namaKota.getText().toString().trim();

            if (!kota.isEmpty()) {
                Intent intent = new Intent(pilih_kota.this, MainActivity.class);
                intent.putExtra("KOTA", kota);
                startActivity(intent);
                finish();  // supaya activity ini ditutup setelah pindah ke MainActivity
            } else {
                namaKota.setError("Masukkan nama kota");
            }
        });
    }
}
