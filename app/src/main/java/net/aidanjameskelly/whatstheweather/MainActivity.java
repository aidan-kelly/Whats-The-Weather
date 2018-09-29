package net.aidanjameskelly.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //api format = http://api.openweathermap.org/data/2.5/weather?q={CITY GOES HERE}&APIkey=8825bdb7a8d654ad71f08687bc7a7f2b&units={IMPERIAL OR METRIC}
    String firstHalf = "http://api.openweathermap.org/data/2.5/weather?q=";
    String secondHalf = "&APIkey=8825bdb7a8d654ad71f08687bc7a7f2b&units=metric";

    EditText cityEditText;
    TextView outputTextView;
    Button button;

    public class DownloadJSON extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try{

                url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    result += (char) data;
                    data = reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    outputTextView.setText(jsonPart.getString("main") + "\n" + jsonPart.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        outputTextView = findViewById(R.id.outputTextView);
        button = findViewById(R.id.button);

    }

    public void onClick(View view){
        String cityName = cityEditText.getText().toString();
        String ourURL = firstHalf + cityName + secondHalf;
        String ourURLNoSpaces = ourURL.replaceAll("\\s+", "");
        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute(ourURLNoSpaces);
        Log.i("OurURL", ourURLNoSpaces);
    }

}
