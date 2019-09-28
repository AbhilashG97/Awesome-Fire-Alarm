package abhilash.example.com.alertmelon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import abhilash.example.com.alertmelon.activities.LogsActivity;
import abhilash.example.com.alertmelon.activities.ToolsActivity;
import abhilash.example.com.alertmelon.adapter.TemperatureAdapter;
import abhilash.example.com.alertmelon.services.FireAlertService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chart_statistics)
    LineChart lineChart;

    private TemperatureAdapter temperatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        drawChart();
        startService(new Intent(this, FireAlertService.class));

        temperatureAdapter = TemperatureAdapter.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    public void drawChart() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(60, 0));
        entries.add(new Entry(48, 1));
        entries.add(new Entry(70.5f, 2));
        entries.add(new Entry(100, 3));
        entries.add(new Entry(180.9f, 4));

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(entries, "Live Temperature");
        set1.setFillAlpha(110);

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the datasets


        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        Description description = new Description();
        description.setText("Live Temperature");
        lineChart.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lineChart.setDefaultFocusHighlightEnabled(true);
        }
        lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        lineChart.setDrawGridBackground(false);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Quit")
                    .setIcon(R.drawable.ic_notification_warning)
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_statistics:
                // Default activity when the app is opened
                break;
            case R.id.nav_manage:
                // Open tools activity
                startActivity(new Intent(MainActivity.this, ToolsActivity.class));
                break;
            case R.id.nav_log:
                startActivity(new Intent(MainActivity.this, LogsActivity.class));
                break;
            case R.id.nav_about_page:
                // Show about page
                createAlertDialog();
            default:
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createAlertDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is an android client for the Fire Alarm project\n");
        alertDialogBuilder.setIcon(R.drawable.ic_menu_about);
        alertDialogBuilder.setTitle("About");
                alertDialogBuilder.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
