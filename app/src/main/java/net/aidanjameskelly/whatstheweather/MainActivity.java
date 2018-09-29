package net.aidanjameskelly.whatstheweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //api format = http://api.openweathermap.org/data/2.5/weather?q={CITY GOES HERE}&APIkey=8825bdb7a8d654ad71f08687bc7a7f2b&units={IMPERIAL OR METRIC}
    String apiKey = "8825bdb7a8d654ad71f08687bc7a7f2b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
