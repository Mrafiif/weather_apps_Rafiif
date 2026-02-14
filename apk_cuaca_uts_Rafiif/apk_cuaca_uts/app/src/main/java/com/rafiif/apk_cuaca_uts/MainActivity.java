package com.rafiif.apk_cuaca_uts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView kotaText, hariText, keteranganCuaca, infoSuhu;
    TextView[] cuacaHarian = new TextView[7];
    ImageView gambarHujan;
    Button buttonPilihKota;
    String apiKey = "ca2d51fa49714e619c0121345252505";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kotaText = findViewById(R.id.kota);
        hariText = findViewById(R.id.hari);
        keteranganCuaca = findViewById(R.id.keterangan_cuaca);
        infoSuhu = findViewById(R.id.info_suhu);
        gambarHujan = findViewById(R.id.gambar_hujan);
        buttonPilihKota = findViewById(R.id.buttonPilihKota); // Pastikan ID di XML sama

        // Inisialisasi array TextView cuaca harian dari layout XML
        cuacaHarian[0] = findViewById(R.id.Kondisi_cuaca_hari1);
        cuacaHarian[1] = findViewById(R.id.Kondisi_cuaca_hari2);
        cuacaHarian[2] = findViewById(R.id.Kondisi_cuaca_hari3);
        cuacaHarian[3] = findViewById(R.id.Kondisi_cuaca_hari4);
        cuacaHarian[4] = findViewById(R.id.Kondisi_cuaca_hari5);
        cuacaHarian[5] = findViewById(R.id.Kondisi_cuaca_hari6);
        cuacaHarian[6] = findViewById(R.id.Kondisi_cuaca_hari7);

        // Ambil nama kota dari Intent
        String kota = getIntent().getStringExtra("KOTA");
        if (kota == null || kota.isEmpty()) {
            kota = "Jakarta";
        }
        kotaText.setText(kota);

        // Ambil data cuaca dari API
        getWeatherData(kota);

        // Event tombol kembali ke halaman pilih kota
        buttonPilihKota.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, pilih_kota.class);
            startActivity(intent);
            finish(); // opsional untuk menutup MainActivity
        });
    }

    private void getWeatherData(String kota) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey +
                "&q=" + kota + "&days=7&aqi=no&alerts=no";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject current = response.getJSONObject("current");
                            JSONObject condition = current.getJSONObject("condition");

                            infoSuhu.setText(current.getString("temp_c") + "°C");
                            keteranganCuaca.setText(condition.getString("text"));

                            JSONArray forecastday = response.getJSONObject("forecast").getJSONArray("forecastday");

                            for (int i = 0; i < 7; i++) {
                                JSONObject dayObj = forecastday.getJSONObject(i).getJSONObject("day");
                                String text = dayObj.getJSONObject("condition").getString("text");
                                double avgTemp = dayObj.getDouble("avgtemp_c");
                                cuacaHarian[i].setText(text + " | " + avgTemp + "°C");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}


