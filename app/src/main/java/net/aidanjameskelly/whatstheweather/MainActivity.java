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

    //first and second half of api calls
    String firstHalf = "http://api.openweathermap.org/data/2.5/weather?q=";
    String secondHalf = "&APIkey=8825bdb7a8d654ad71f08687bc7a7f2b&units=metric";

    //set up our ui elements
    EditText cityEditText;
    TextView outputTextView;
    Button button;

    public class DownloadJSON extends AsyncTask<String, Void, String>{

        //downloads the json and makes processes it
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try{

                //sets up the connection
                url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                //downloads the json as a string
                while(data != -1){
                    result += (char) data;
                    data = reader.read();
                }

                //return the json
                return result;

            }catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }

        //after the download
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                //make a jsonObject of what we downloaded
                JSONObject jsonObject = new JSONObject(s);

                //grab the weather info
                String weatherInfo = jsonObject.getString("weather");

                //grab the temperature
                JSONObject mainObject = jsonObject.getJSONObject("main");
                String temp = mainObject.getString("temp");

                //add the temp to our output string
                String toOutput =  temp + " degrees Celsius.\n";

                //the weather info is an array so we make a jsonArray
                JSONArray jsonArray = new JSONArray(weatherInfo);

                //loop through that array and grab the weather info
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    toOutput += (jsonPart.getString("main") + ": " + jsonPart.getString("description") + "\n");
                }

                //update the textView with the weather
                outputTextView.setText(toOutput);

            } catch (Exception e) {
                e.printStackTrace();
                outputTextView.setText("City not Found.");
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up our UI elements
        cityEditText = findViewById(R.id.cityEditText);
        outputTextView = findViewById(R.id.outputTextView);
        button = findViewById(R.id.button);

    }

    public void onClick(View view){

        //grab our city name
        String cityName = cityEditText.getText().toString();

        //create our api call and remove any whitespace
        String ourURL = firstHalf + cityName + secondHalf;
        String ourURLNoSpaces = ourURL.replaceAll("\\s+", "");

        //download and process the json
        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute(ourURLNoSpaces);
    }

}
