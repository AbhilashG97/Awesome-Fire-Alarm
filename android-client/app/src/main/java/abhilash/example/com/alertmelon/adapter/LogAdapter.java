package abhilash.example.com.alertmelon.adapter;

import java.util.ArrayList;

public class LogAdapter {

    private static LogAdapter logAdapter;
    private static ArrayList<String> logList = new ArrayList<>();

    private LogAdapter() {

    }

    public static LogAdapter getInstance() {
        if(logAdapter == null) {
            logAdapter = new LogAdapter();
            return logAdapter;
        }
        return logAdapter;
    }

    public ArrayList<String> getLogList() {
        return logList;
    }
}
