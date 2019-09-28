package abhilash.example.com.alertmelon.logrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abhilash.example.com.alertmelon.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogListViewHolder> {

    private ArrayList<String> temperatureList;

    public LogListAdapter() {
        //Default constructor
    }

    public LogListAdapter(ArrayList<String> temperatureList) {
        this.temperatureList = temperatureList;
    }

    @NonNull
    @Override
    public LogListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LogListViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.log_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogListViewHolder logListViewHolder, int position) {
        logListViewHolder.logData.setText(temperatureList.get(position));
    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    static class LogListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_log_data)
        TextView logData;


        public LogListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
