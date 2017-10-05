package stock.cryptodoc.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import stock.cryptodoc.R;
import stock.cryptodoc.SessionData.SessionManagement;
import stock.cryptodoc.model.Datum;
import stock.cryptodoc.model.Example;
import stock.cryptodoc.utils.ApiClient;
import stock.cryptodoc.utils.ApiInterface;

public class IndianGraphActivity extends AppCompatActivity {
    CandleStickChart web;
    Toolbar toolbar;
    RecyclerView postdatalist;
    RadioButton timeseries_hour;
    RadioGroup timeseries;
    ArrayList<CandleEntry> arrayList=new ArrayList<>();
    ArrayList<String> al=new ArrayList<>();
    TextView cointv,coinpricetv,open,high,low,close,date;
    FloatingActionButton addNewPostFab;
    SessionManagement sessionManagement;
    LinearLayout newCommentContainer;
    String email="",photo="";
    String market;
    EditText commentEditText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_graph);
        toolbar= (Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        sessionManagement=new SessionManagement(IndianGraphActivity.this);


        web= (CandleStickChart) findViewById(R.id.web);
        timeseries= (RadioGroup) findViewById(R.id.timeseries);
        timeseries_hour= (RadioButton) findViewById(R.id.timeseries_hour);
        cointv= (TextView) findViewById(R.id.cointv);
        postdatalist= (RecyclerView) findViewById(R.id.postdatalist);
        coinpricetv= (TextView) findViewById(R.id.coinpricetv);
        commentEditText= (EditText) findViewById(R.id.commentEditText);
        sendButton= (Button) findViewById(R.id.sendButton);
        open= (TextView) findViewById(R.id.open);
        low= (TextView) findViewById(R.id.low);
        high= (TextView) findViewById(R.id.high);
        close= (TextView) findViewById(R.id.close);
        date= (TextView) findViewById(R.id.date);
        market=getIntent().getStringExtra("market");
        timeseries.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.timeseries_month:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("LocalBitcoins")) {

                                    getBTCchartMonth("BTC", "INR", "30", "LocalBitcoins");



                            }

                            if (market.equalsIgnoreCase("Unocoin")) {

                                getBTCchartMonth("BTC", "INR", "30", "Unocoin");



                            }
                            if (market.equalsIgnoreCase("EthexIndia")) {

                                getBTCchartMonth("ETH", "INR", "30", "EthexIndia");



                            }
                        }
                        break;
                    case R.id.timeseries_three_month:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("LocalBitcoins")) {

                                getBTCchartMonth("BTC", "INR", "90", "LocalBitcoins");



                            }
                            if (market.equalsIgnoreCase("Unocoin")) {

                                getBTCchartMonth("BTC", "INR", "90", "Unocoin");



                            }
                            if (market.equalsIgnoreCase("EthexIndia")) {

                                getBTCchartMonth("ETH", "INR", "90", "EthexIndia");



                            }
                        }
                        break;
                    case R.id.timeseries_week:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("LocalBitcoins")) {

                                getBTCchartMonth("BTC", "INR", "7", "LocalBitcoins");



                            }
                            if (market.equalsIgnoreCase("Unocoin")) {

                                getBTCchartMonth("BTC", "INR", "7", "Unocoin");



                            }
                            if (market.equalsIgnoreCase("EthexIndia")) {

                                getBTCchartMonth("ETH", "INR", "7", "EthexIndia");



                            }
                        }
                        break;
                    case R.id.timeseries_hour:
                        if(market != null && !market.isEmpty()) {
                                if (market.equalsIgnoreCase("LocalBitcoins")) {
                                    getChart("BTC", "USD", "60", "LocalBitcoins");


                                }
                            if (market.equalsIgnoreCase("Unocoin")) {

                                getBTCchartMonth("BTC", "INR", "60", "Unocoin");



                            }
                            if (market.equalsIgnoreCase("EthexIndia")) {

                                getBTCchartMonth("ETH", "INR", "60", "EthexIndia");



                            }
                            }
                            break;
                    case R.id.timeseries_day:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("LocalBitcoins")) {

                                getBTCchartMonth("BTC", "USD", "1", "LocalBitcoins");


                            }
                            if (market.equalsIgnoreCase("Unocoin")) {

                                getBTCchartMonth("BTC", "INR", "1", "Unocoin");



                            }
                            if (market.equalsIgnoreCase("EthexIndia")) {

                                getBTCchartMonth("ETH", "INR", "1", "EthexIndia");



                            }
                        }
                                break;
                }
            }
        });


    }
    private void getChart(String btc, String usd, String s, String bitfinex) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        if (al!=null) {
            al.clear();
        }
        if (arrayList!=null) {
            arrayList.clear();
        }
        Call<Example> responseBodyCall=apiInterface.getChart(btc,usd,s,bitfinex);
        responseBodyCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                List<Datum> data=response.body().getData();
                for (int i=0;i<data.size();i++) {
                    Datum d = data.get(i);

                    al.add(""+getDateandTime(d.getTime()));

                    Log.d("sdfs", "" + d.getClose());
                }

                for (int i=0;i<data.size();i++) {
                    Datum d = data.get(i);
                    CandleEntry entry=new CandleEntry(i,Float.parseFloat(String.valueOf(d.getHigh())),Float.parseFloat(String.valueOf(d.getLow())),Float.parseFloat(String.valueOf(d.getOpen())),Float.parseFloat(String.valueOf(d.getClose())));
                    arrayList.add(entry);
                    Log.d("sdfs", "" + d.getClose());
                }
                CandleDataSet dataset = new CandleDataSet(arrayList,"");
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                CandleData datas = new CandleData(al, dataset);
                dataset.setValueTextColor(Color.TRANSPARENT);
                web.setData(datas);
                web.invalidate();

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });


    }
    private String getDateandTime(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            Date netDate = (new Date(timeStamp*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    private void getBTCchartMonth(String btc, String usd, String s, String bitfinex) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        if (al!=null) {
            al.clear();
        }
        if (arrayList!=null) {
            arrayList.clear();
        }
        Call<Example> responseBodyCall=apiInterface.getChartLTCMonth(btc,usd,s,bitfinex);
        responseBodyCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                List<Datum> data=response.body().getData();
                for (int i=0;i<data.size();i++) {
                    Datum d = data.get(i);

                    al.add(""+getDate(d.getTime()));

                    Log.d("sdfs", "" + d.getClose());
                }

                for (int i=0;i<data.size();i++) {
                    Datum d = data.get(i);
                    CandleEntry entry=new CandleEntry(i,Float.parseFloat(String.valueOf(d.getHigh())),Float.parseFloat(String.valueOf(d.getLow())),Float.parseFloat(String.valueOf(d.getOpen())),Float.parseFloat(String.valueOf(d.getClose())));
                    arrayList.add(entry);
                    Log.d("sdfs", "" + d.getClose());
                }
                CandleDataSet dataset = new CandleDataSet(arrayList,"");
                dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                CandleData datas = new CandleData(al, dataset);
                dataset.setValueTextColor(Color.TRANSPARENT);
                web.setData(datas);
                web.invalidate();

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
            }
        });


    } private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd/MM");
            Date netDate = (new Date(timeStamp*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
