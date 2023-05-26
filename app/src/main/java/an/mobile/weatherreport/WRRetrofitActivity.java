package an.mobile.weatherreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class WRRetrofitActivity extends AppCompatActivity {
    private Button bt_back;
    private TextView tv_kota, tv_suhu, tv_cuaca, tv_kecAngin, tv_longitude, tv_latitude, tv_library, tv_tglNow;
    private TextView tv_tanggal1, tv_tanggal2, tv_tanggal3, tv_tanggal4, tv_tanggal5, tv_tanggal6;
    private TextView tv_kodeCuaca1, tv_kodeCuaca2, tv_kodeCuaca3, tv_kodeCuaca4, tv_kodeCuaca5, tv_kodeCuaca6;
    private ImageView iv_ramalan1, iv_ramalan2, iv_ramalan3, iv_ramalan4, iv_ramalan5, iv_ramalan6;
    private ImageView iconCuaca;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);
        bt_back = findViewById(R.id.btBack);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WRRetrofitActivity.this, MainActivity.class);
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

        getWeather();

    }
    public class WeatherData {

        @SerializedName("current_weather")
        private CurrentWeather currentweather;
        @SerializedName("daily")
        private Daily daily;
        private String latitude, windspeed;
        private String longitude;
        private int[] weathercode;

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude () {
            return longitude;
        }

        public String getWindspeed(){
            return windspeed;
        }

        public CurrentWeather getCurrent_weather() {
            return currentweather;
        }

        public Daily getDaily() {
            return daily;
        }

    }
    public class Daily {
        @SerializedName("time")
        private List<String> time;

        @SerializedName("weathercode")
        private List<String> wheatercode;
        public List<String> getTime() {
            return time;
        }

        public List<String> getWheatercode() {
            return wheatercode;
        }
    }
    public class CurrentWeather {
        @SerializedName("windspeed")
        private String windspeed;

        @SerializedName("temperature")
        private String temperature;

        @SerializedName("weathercode")
        private String weathercode;

        public String getWindspeed() {
            return windspeed;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getWeathercode() {
            return weathercode;
        }
    }
    public interface WeatherService {
        @GET("forecast")
        Call<WeatherData> getWeatherData(@Query("latitude") double latitude, @Query("longitude") double longitude,
                                         @Query("daily") String daily, @Query("current_weather") boolean currentWeather,
                                         @Query("timezone") String timezone);
    }
    public void getWeather(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        double latitude = -7.98;
        double longitude = 112.63;
        String daily = "weathercode";
        boolean currentWeather = true;
        String timezone = "auto";
        Call<WeatherData> call = service.getWeatherData(latitude, longitude, daily, currentWeather, timezone);

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    WeatherData weatherData = response.body();
                    // Menyimpan data cuaca ke dalam TextView
                    tv_latitude.setText(response.body().getLatitude());
                    tv_longitude.setText(response.body().getLongitude());
                    tv_kecAngin.setText(response.body().getCurrent_weather().getWindspeed() + " knot");
                    tv_suhu.setText(response.body().getCurrent_weather().getTemperature() + "\u00B0" + "C");
                    tv_tglNow.setText(response.body().getDaily().getTime().get(0));
                    handleIcon(response.body().getCurrent_weather().getWeathercode(),0);

                    for (int i = 1; i <= 6; i++) {
                        String code = response.body().getDaily().getWheatercode().get(i);
                        handleIcon(code, i);
                        String time = response.body().getDaily().getTime().get(i);
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
                    tv_library.setText("Library by Retrofit");

                } else {

                    tv_longitude.setText("String.valueOf(latitude)");
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
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
