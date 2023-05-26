package an.mobile.weatherreport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class WRVollyActivity extends AppCompatActivity {

    private Button bt_back;
    private TextView tv_kota, tv_suhu, tv_cuaca, tv_kecAngin, tv_longitude, tv_latitude, tv_library, tv_tglNow;
    private TextView tv_tanggal1, tv_tanggal2, tv_tanggal3, tv_tanggal4, tv_tanggal5, tv_tanggal6;
    private TextView tv_kodeCuaca1, tv_kodeCuaca2, tv_kodeCuaca3, tv_kodeCuaca4, tv_kodeCuaca5, tv_kodeCuaca6;
    private ImageView iv_ramalan1, iv_ramalan2, iv_ramalan3, iv_ramalan4, iv_ramalan5, iv_ramalan6;
    private ImageView iconCuaca;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);
        bt_back = findViewById(R.id.btBack);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WRVollyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tv_kota = findViewById(R.id.tvKota);
        tv_suhu = findViewById(R.id.tvSuhu);
        tv_cuaca = findViewById(R.id.tvCuaca);
        tv_kecAngin = findViewById(R.id.tvKecAngin);
        tv_latitude = findViewById(R.id.tvLatitude);
        tv_longitude = findViewById(R.id.tvLongitude);
        iconCuaca = findViewById(R.id.iconCuaca);
        tv_library = findViewById(R.id.tvLibrary);
        tv_tglNow = findViewById(R.id.tvTglNow);

        tv_tanggal1 = findViewById(R.id.tvTanggal1);
        tv_tanggal2 = findViewById(R.id.tvTanggal2);
        tv_tanggal3 = findViewById(R.id.tvTanggal3);
        tv_tanggal4 = findViewById(R.id.tvTanggal4);
        tv_tanggal5 = findViewById(R.id.tvTanggal5);
        tv_tanggal6 = findViewById(R.id.tvTanggal6);

        iv_ramalan1 = findViewById(R.id.ivCuaca1);
        iv_ramalan2 = findViewById(R.id.ivCuaca2);
        iv_ramalan3 = findViewById(R.id.ivCuaca3);
        iv_ramalan4 = findViewById(R.id.ivCuaca4);
        iv_ramalan5 = findViewById(R.id.ivCuaca5);
        iv_ramalan6 = findViewById(R.id.ivCuaca6);

        tv_kodeCuaca1 = findViewById(R.id.tvKodeCuaca1);
        tv_kodeCuaca2 = findViewById(R.id.tvKodeCuaca2);
        tv_kodeCuaca3 = findViewById(R.id.tvkodeCuaca3);
        tv_kodeCuaca4 = findViewById(R.id.tvKodeCuaca4);
        tv_kodeCuaca5 = findViewById(R.id.tvKodeCuaca5);
        tv_kodeCuaca6 = findViewById(R.id.tvKodeCuaca6);

        fetchWeatherData();
    }

    private void fetchWeatherData() {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=-7.98&longitude=112.63&daily=weathercode&current_weather=true&timezone=auto";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String derajat = "\u00B0";
                            String koordinat_latitude = response.getString("latitude");
                            String koordinat_longitude = response.getString("longitude");
                            JSONObject current_weather = response.getJSONObject("current_weather");
                            String temperature = current_weather.getString("temperature");
                            String windspeed = current_weather.getString("windspeed");
                            String weathercode = current_weather.getString("weathercode");
                            String tanggal_sekarang = current_weather.getString("time");

                            tv_suhu.setText(temperature + derajat + "C");
                            tv_kecAngin.setText(windspeed + " knot");
                            tv_latitude.setText(koordinat_latitude);
                            tv_longitude.setText(koordinat_longitude);
                            tv_library.setText("Library by Volley");
                            tv_tglNow.setText(tanggal_sekarang.substring(0,10));

                            handleIcon(weathercode, 0);
                            JSONObject daily = response.getJSONObject("daily");
                            JSONArray tanggal = daily.getJSONArray("time");
                            JSONArray kode_weather = daily.getJSONArray("weathercode");

                            for (int i = 1; i <= kode_weather.length(); i++) {
                                String code = kode_weather.getString(i);
                                handleIcon(code, i);
                                String time = tanggal.getString(i);
                                // Process the retrieved time as needed
                                switch (i){
                                    case 1:
                                        tv_tanggal1.setText(time);
                                    case 2:
                                        tv_tanggal2.setText(time);
                                    case 3:
                                        tv_tanggal3.setText(time);
                                    case 4:
                                        tv_tanggal4.setText(time);
                                    case 5:
                                        tv_tanggal5.setText(time);
                                    case 6:
                                        tv_tanggal6.setText(time);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }



    private void handleIcon(String code, int i){
        List<String> hujan = Arrays.asList("51", "53", "55", "56", "57", "61", "63", "65", "66", "67");
        List<String> berawan = Arrays.asList("45","48");
        List<String> badai = Arrays.asList("80", "81" , "82", "95", "96", "99");

        if (berawan.contains(code)){
            switch (i){
                case 0:
                    tv_cuaca.setText("Berawan");
                    iconCuaca.setImageResource(R.drawable.fog_45_48);
                case 1:
                    tv_kodeCuaca1.setText("Berawan");
                    iv_ramalan1.setImageResource(R.drawable.fog_45_48);
                case 2:
                    tv_kodeCuaca2.setText("Berawan");
                    iv_ramalan2.setImageResource(R.drawable.fog_45_48);
                case 3:
                    tv_kodeCuaca3.setText("Berawan");
                    iv_ramalan3.setImageResource(R.drawable.fog_45_48);
                case 4:
                    tv_kodeCuaca4.setText("Berawan");
                    iv_ramalan4.setImageResource(R.drawable.fog_45_48);
                case 5:
                    tv_kodeCuaca5.setText("Berawan");
                    iv_ramalan5.setImageResource(R.drawable.fog_45_48);
                case 6:
                    tv_kodeCuaca6.setText("Berawan");
                    iv_ramalan6.setImageResource(R.drawable.fog_45_48);
            }
        } else  if (code.equals("2") || code.equals("3") ){
            switch (i){
                case 0:
                    tv_cuaca.setText("Cerah");
                    iconCuaca.setImageResource(R.drawable.partly_cloud_2);
                case 1:
                    tv_kodeCuaca1.setText("Cerah");
                    iv_ramalan1.setImageResource(R.drawable.partly_cloud_2);
                case 2:
                    tv_kodeCuaca2.setText("Cerah ");
                    iv_ramalan2.setImageResource(R.drawable.partly_cloud_2);
                case 3:
                    tv_kodeCuaca3.setText("Cerah ");
                    iv_ramalan3.setImageResource(R.drawable.partly_cloud_2);
                case 4:
                    tv_kodeCuaca4.setText("Cerah ");
                    iv_ramalan4.setImageResource(R.drawable.partly_cloud_2);
                case 5:
                    tv_kodeCuaca5.setText("Cerah ");
                    iv_ramalan5.setImageResource(R.drawable.partly_cloud_2);
                case 6:
                    tv_kodeCuaca6.setText("Cerah ");
                    iv_ramalan6.setImageResource(R.drawable.partly_cloud_2);
            }
        } else  if (code.equals("1") || code.equals("0") ){
            switch (i){
                case 0:
                    tv_cuaca.setText("Cerah bgt");
                    iconCuaca.setImageResource(R.drawable.mainly_clear_1);
                case 1:
                    tv_kodeCuaca1.setText("Cerah bgt");
                    iv_ramalan1.setImageResource(R.drawable.mainly_clear_1);
                case 2:
                    tv_kodeCuaca2.setText("Cerah bgt ");
                    iv_ramalan2.setImageResource(R.drawable.mainly_clear_1);
                case 3:
                    tv_kodeCuaca3.setText("Cerah bgt ");
                    iv_ramalan3.setImageResource(R.drawable.mainly_clear_1);
                case 4:
                    tv_kodeCuaca4.setText("Cerah bgt ");
                    iv_ramalan4.setImageResource(R.drawable.mainly_clear_1);
                case 5:
                    tv_kodeCuaca5.setText("Cerah bgt ");
                    iv_ramalan5.setImageResource(R.drawable.mainly_clear_1);
                case 6:
                    tv_kodeCuaca6.setText("Cerah bgt ");
                    iv_ramalan6.setImageResource(R.drawable.mainly_clear_1);
            }
        }else  if (hujan.contains(code)){
            switch (i){
                case 0:
                    tv_cuaca.setText("Hujan");
                    iconCuaca.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 1:
                    tv_kodeCuaca1.setText("Hujan");
                    iv_ramalan1.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 2:
                    tv_kodeCuaca2.setText("Hujan");
                    iv_ramalan2.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 3:
                    tv_kodeCuaca3.setText("Hujan");
                    iv_ramalan3.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 4:
                    tv_kodeCuaca4.setText("Hujan");
                    iv_ramalan4.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 5:
                    tv_kodeCuaca5.setText("Hujan");
                    iv_ramalan5.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
                case 6:
                    tv_kodeCuaca6.setText("Hujan");
                    iv_ramalan6.setImageResource(R.drawable.rain_51_53_55_56_57_61_63_65_66_67);
            }
        }else  if (badai.contains(code)){
            switch (i){
                case 0:
                    tv_cuaca.setText("Badai");
                    iconCuaca.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 1:
                    tv_kodeCuaca1.setText("Badai");
                    iv_ramalan1.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 2:
                    tv_kodeCuaca2.setText("Badai");
                    iv_ramalan2.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 3:
                    tv_kodeCuaca3.setText("Badai");
                    iv_ramalan3.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 4:
                    tv_kodeCuaca4.setText("Badai");
                    iv_ramalan4.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 5:
                    tv_kodeCuaca5.setText("Badai");
                    iv_ramalan5.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
                case 6:
                    tv_kodeCuaca6.setText("Badai");
                    iv_ramalan6.setImageResource(R.drawable.thunder_80_81_82_95_96_99);
            }
        } else {
            switch (i){
                case 0:
                    tv_cuaca.setText("Salju");
                    iconCuaca.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 1:
                    tv_kodeCuaca1.setText("Salju");
                    iv_ramalan1.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 2:
                    tv_kodeCuaca2.setText("Salju");
                    iv_ramalan2.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 3:
                    tv_kodeCuaca3.setText("Salju");
                    iv_ramalan3.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 4:
                    tv_kodeCuaca4.setText("Salju");
                    iv_ramalan4.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 5:
                    tv_kodeCuaca5.setText("Salju");
                    iv_ramalan5.setImageResource(R.drawable.snow_71_73_75_77_85_86);
                case 6:
                    tv_kodeCuaca6.setText("Salju");
                    iv_ramalan6.setImageResource(R.drawable.snow_71_73_75_77_85_86);
            }
        }
    }







}
