package abhilash.example.com.alertmelon.adapter;

import java.util.ArrayList;

import abhilash.example.com.alertmelon.logrecyclerview.Temperature;

public class TemperatureAdapter {

    private static TemperatureAdapter temperatureAdapter;
    private static ArrayList<Temperature> temperatureList = new ArrayList<>();

    private TemperatureAdapter() {

    }

    public static TemperatureAdapter getInstance() {
        if(temperatureAdapter == null) {
            temperatureAdapter = new TemperatureAdapter();
            return temperatureAdapter;
        }
        return temperatureAdapter;
    }

    public ArrayList<Temperature> getTemperatureList() {
        return temperatureList;
    }

}
