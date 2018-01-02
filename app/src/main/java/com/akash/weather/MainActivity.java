package com.akash.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    Button btnWeather;
    TextView tvSearchWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        tvSearchWeather = (TextView) findViewById(R.id.tvSearchWeather);

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected()))
                {
                    Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    String placeName = etName.getText().toString();
                    if (placeName.length() == 0) {
                        Toast.makeText(MainActivity.this, "Please enter Place Name", Toast.LENGTH_SHORT).show();
                        etName.requestFocus();
                        return;
                    }

                    Task1 t1 = new Task1();
                    t1.execute("http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + placeName + "&appid=" + "c6e315d09197cec231495138183954bd");
                }
            }
        });

    }

    class Task1 extends AsyncTask<String, Void, Double>
    {
        String jsonStr = "";
        String line = "";
        String searchResults = "";
        double temperature = 0.0;

        @Override
        protected Double doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader =  new BufferedReader(new InputStreamReader(is));

                while((line = reader.readLine()) !=null)
                {
                    jsonStr += line + "\n";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject quote = jsonObject.getJSONObject("main");
                    temperature = quote.getDouble("temp");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return temperature;
        }

        @Override
        protected void onPostExecute(Double s) {
            super.onPostExecute(s);
            tvSearchWeather.setText("" + s);

        }
    }
}
