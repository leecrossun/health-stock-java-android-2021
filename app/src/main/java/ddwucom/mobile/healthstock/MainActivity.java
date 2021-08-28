package ddwucom.mobile.healthstock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ddwucom.mobile.healthstock.DemoBase;
import ddwucom.mobile.healthstock.dao.HealthStocksDAO;
import ddwucom.mobile.healthstock.dto.Exercise;
import ddwucom.mobile.healthstock.dto.Position;
import ddwucom.mobile.healthstock.dto.Stocks;
import ddwucom.mobile.healthstock.dto.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends DemoBase {
    private TextView myStock;
    private TextView userName;

    private String TAG = "MainActivity";

    private CombinedChart chart;
    private final int count = 5;

    private SQLiteDatabase db;
    private Cursor cursor;
    private HealthStocksDAO dao;

    private ArrayList<Stocks> stocksList = new ArrayList<>();
    private ArrayList<Position> positionList = null;
    private ArrayList<Exercise> exerciseList = null;
    private UserInfo userInfo = new UserInfo("name2", 500, 155, 50);

    private final int Walk_Result = 100;
    private final int Position_Result = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // 프로필 클릭시 사용자 페이지로 이동
        ImageView user = (ImageView) findViewById(R.id.userProfile);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"사용자 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
                userIntent.putExtra("position", String.valueOf(positionList.get(count-1).getPrice()));
                userIntent.putExtra("exercise", String.valueOf(exerciseList.get(count-1).getPrice()));
                userIntent.putExtra("userName", userName.getText().toString());
                userIntent.putExtra("stock", myStock.getText().toString());
                startActivity(userIntent);
            }
        });

        myStock = (TextView)findViewById(R.id.myStock);
        userName = (TextView)findViewById(R.id.main_userName);

        setTitle("CombinedChartActivity");

        //리스트 설정
        dao = new HealthStocksDAO(MainActivity.this);
//        setLists();

        chart = findViewById(R.id.chart1);
//        drawGraph();
    }

    protected void setLists() {
        stocksList = dao.getAllStocks(userInfo.getUserName());
        exerciseList = new ArrayList<>();
        positionList = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            int sId = stocksList.get(i).getStocksId();

            Position p = dao.getPosition(sId);
            positionList.add(p);

            Exercise e = dao.getExercise(sId);
            exerciseList.add(e);
        }

        //차트 날짜 설정
        ArrayList<Date> dateList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dateList.add(stocksList.get(i).getDate());
        }
        initString(dateList);
    }

    protected void drawGraph() {
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter
                () {
            @Override
            public String getFormattedValue(float value) {
                Log.d("CombinedChartActivity", "Value: " + value);
                return dates[(int) value % dates.length];
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        data.setValueTypeface(tfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        chart.setData(data);
        chart.invalidate();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_heart:
                Toast.makeText(MainActivity.this,"걸음 측정화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent walk_intent = new Intent(MainActivity.this, WalkActivity.class);
                startActivityForResult(walk_intent, Walk_Result);
                break;
            case R.id.btn_position:
                Toast.makeText(MainActivity.this,"자세 측정화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent position_intent = new Intent(MainActivity.this, PositionActivity.class);
                startActivityForResult(position_intent, Position_Result);
                break;
        }
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, stocksList.get(index).getSharePrice()));

        LineDataSet set = new LineDataSet(entries, "건강 주가");
        set.setColor(Color.rgb(160, 155, 183));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(160, 155, 183));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(160, 155, 183));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(160, 155, 183));

        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>(); //운동
        ArrayList<BarEntry> entries2 = new ArrayList<>(); //자세

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(0, exerciseList.get(index).getPrice()));
            entries2.add(new BarEntry(0, positionList.get(index).getPrice()));
        }

        //운동
        BarDataSet set1 = new BarDataSet(entries1, "운동값");
        set1.setColor(Color.rgb(254, 206, 209));
        set1.setValueTextColor(Color.rgb(254, 206, 209));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        //자세
        BarDataSet set2 = new BarDataSet(entries2, "자세값");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(Color.rgb(207, 232, 227));
        set2.setValueTextColor(Color.rgb(207, 232, 227));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    @Override
    protected void saveToGallery() { /* Intentionally left empty */ }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (requestCode){
            case Walk_Result:
                if (resultCode == RESULT_OK){
                    int steps = data.getExtras().getInt("steps");
                    int steps_to_point = data.getExtras().getInt("steps_to_point");
                    int minute = data.getExtras().getInt("minute");

                    Log.d(TAG, "dialog");

                    // dialog
                    String walk_msg = "총 [ " + steps + " ] 걸음으로 건강주식이 [ " + steps_to_point + " ] 원 상승하였습니다.";
                    builder.setMessage(walk_msg)
                            .setTitle("운동 측정 결과")
                            .show();


                    Log.d(TAG, "db");
                    //db에 걸음수 저장
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                    int date = Integer.parseInt(sdf.format(cal.getTime()));
                    int stocksId = dao.getTodayStockId(userInfo.getUserName(), date);

                    Exercise exercise = dao.getExercise(stocksId);
                    Exercise new_exercise = new Exercise(steps_to_point + exercise.getPrice(), stocksId, minute + exercise.getMinute());
                    dao.saveOrUpdate(new_exercise, steps_to_point);
                }
                break;
            case Position_Result:
                if (resultCode == RESULT_OK){
                    int position_to_point = data.getExtras().getInt("position");
                    int minute = data.getExtras().getInt("minute");

                    // dialog
                    String walk_msg = "건강주식이 [ " + position_to_point + " ] 원 상승하였습니다.";
                    builder.setMessage(walk_msg)
                            .setTitle("운동 측정 결과")
                            .show();

                    //db에 데이터 저장
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                    int date = Integer.parseInt(sdf.format(cal.getTime()));
                    int stocksId = dao.getTodayStockId(userInfo.getUserName(), date);

                    Position position = dao.getPosition(stocksId);
                    Position new_position = new Position(position_to_point + position.getPrice(), stocksId, minute + position.getMinute());
                    dao.saveOrUpdate(new_position, position_to_point);

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLists();
        drawGraph();
        myStock.setText(stocksList.get(count-1).getSharePrice() + "원");
        userName.setText(userInfo.getUserName());

    }
}