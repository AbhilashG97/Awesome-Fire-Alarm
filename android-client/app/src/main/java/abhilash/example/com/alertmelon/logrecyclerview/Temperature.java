package abhilash.example.com.alertmelon.logrecyclerview;

import androidx.annotation.NonNull;

public class Temperature {

    private String logData;

    public Temperature() {
        //Default constructor
    }

    public Temperature(String logData) {
        this.logData = logData;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }


    @NonNull
    @Override
    public String toString() {
        return logData;
    }
}
