package abhilash.example.com.alertmelon.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import abhilash.example.com.alertmelon.R;
import abhilash.example.com.alertmelon.adapter.LogAdapter;
import abhilash.example.com.alertmelon.logrecyclerview.LogListAdapter;
import abhilash.example.com.alertmelon.utility.TinyDB;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LogsActivity extends AppCompatActivity {

    @BindView(R.id.rv_logs)
    RecyclerView logRecyclerView;

    private LogListAdapter logListAdapter;
    private LogAdapter logAdapter;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        logAdapter = LogAdapter.getInstance();

        /**
         * TODO: Deserialize logs from shared preferences and store it in temperatureList
         */

        if(logAdapter.getLogList().size() != 0) {
            tinyDB.putListString("Log list", logAdapter.getLogList());
            logListAdapter = new LogListAdapter(logAdapter.getLogList());
            logListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No Logs!", Toast.LENGTH_LONG).show();
        }

        logRecyclerView.setItemAnimator(new DefaultItemAnimator());

        logRecyclerView.setAdapter(logListAdapter);
    }

}
