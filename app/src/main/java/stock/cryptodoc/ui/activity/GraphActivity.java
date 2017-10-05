package stock.cryptodoc.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import stock.cryptodoc.R;
import stock.cryptodoc.SessionData.SessionManagement;
import stock.cryptodoc.adapter.CommentsAdapter;
import stock.cryptodoc.model.Comments;
import stock.cryptodoc.model.Datum;
import stock.cryptodoc.model.Example;
import stock.cryptodoc.utils.ApiClient;
import stock.cryptodoc.utils.ApiInterface;

public class GraphActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    CandleStickChart web;
    Toolbar toolbar;
    String coin,coinprice,market;
RecyclerView postdatalist;
RadioButton timeseries_hour;
    RadioGroup timeseries;
    private FirebaseAuth mAuth;
    ArrayList<Comments> arraylistcomments=new ArrayList<>();
    CommentsAdapter commentAdapter;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<CandleEntry> arrayList=new ArrayList<>();
    ArrayList<String> al=new ArrayList<>();
TextView cointv,coinpricetv,open,high,low,close,date;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=1;
    private SignInButton signInButton;
FloatingActionButton addNewPostFab;
    SessionManagement sessionManagement;
    LinearLayout newCommentContainer;
    String email="",photo="";
    EditText commentEditText;
    Button sendButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        toolbar= (Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        sessionManagement=new SessionManagement(GraphActivity.this);


        web= (CandleStickChart) findViewById(R.id.web);
        timeseries= (RadioGroup) findViewById(R.id.timeseries);
        addNewPostFab= (FloatingActionButton) findViewById(R.id.addNewPostFab);
        newCommentContainer= (LinearLayout) findViewById(R.id.newCommentContainer);
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
        coin=getIntent().getStringExtra("coin");
        coinprice=getIntent().getStringExtra("coinprice");
        market=getIntent().getStringExtra("market");
        coinpricetv.setText(coinprice);
        cointv.setText(coin);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GraphActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        postdatalist.setLayoutManager(layoutManager);
        if (sessionManagement.isLoggedIn()){
            HashMap<String,String> data=sessionManagement.getUserDetails();
            email=data.get(SessionManagement.KEY_EMAIL);
            photo=data.get(SessionManagement.KEY_PHOTOURI);
            getComments(market,coin);
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String    date = df.format(c.getTime());
                Toast.makeText(GraphActivity.this, "sdsa"+email, Toast.LENGTH_SHORT).show();
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://androidandme.in").build();
                ApiInterface myinterface = restAdapter.create(ApiInterface.class);
                myinterface.addComments(email, commentEditText.getText().toString(), date, market, coin, new retrofit.Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                        Toast.makeText(GraphActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                        getComments(market,coin);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });



                //add in database and recyclerview
                // hide
                newCommentContainer.setVisibility(View.GONE);
            }
        });
        addNewPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    newCommentContainer.setVisibility(View.VISIBLE);
                }else {
                    Intent intent = new Intent(GraphActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        Toast.makeText(this, ""+coin, Toast.LENGTH_SHORT).show();
        web.setOnChartValueSelectedListener(this);
        if(market != null && !market.isEmpty()) {
            if (market.equalsIgnoreCase("Bitfinex")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "Bitfinex");
                    timeseries_hour.setChecked(true);

                }
            }
            if (market.equalsIgnoreCase("Bitstamp")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "Bitstamp");
                    timeseries_hour.setChecked(true);
                }
            }
            if (market.equalsIgnoreCase("BitTrex")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "BitTrex");
                    timeseries_hour.setChecked(true);

                }
                if (coin.equalsIgnoreCase("ETH")) {
                    getChart("ETH", "USD", "60", "BitTrex");
                    timeseries_hour.setChecked(true);

                }
                if (coin.equalsIgnoreCase("XRP")) {
                    getChart("XRP", "USD", "60", "BitTrex");
                    timeseries_hour.setChecked(true);

                }
                if (coin.equalsIgnoreCase("LTC")) {
                    getChart("LTC", "USD", "60", "BitTrex");
                    timeseries_hour.setChecked(true);

                }
                if (coin.equalsIgnoreCase("ECH")) {
                    getChart("LTC", "USD", "60", "BitTrex");
                    timeseries_hour.setChecked(true);

                }
            }
            if (market.equalsIgnoreCase("Kraken")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "Kraken");
                    timeseries_hour.setChecked(true);
                }
            }
            if (market.equalsIgnoreCase("Poloniex")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "Poloniex");
                    timeseries_hour.setChecked(true);
                }
            }
            if (market.equalsIgnoreCase("BTCE")) {
                if (coin.equalsIgnoreCase("BTC")) {
                    getChart("BTC", "USD", "60", "BTCE");
                    timeseries_hour.setChecked(true);
                }
            }
        }


                    timeseries.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.timeseries_month:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "30", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "30", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "30", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "30", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bittrex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "30", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "30", "BitTrex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "30", "BitTrex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "30", "BitTrex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitstamp")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "30", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "30", "Bitstamp");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "30", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "30", "Bitstamp");

                                }
                            }
                            if (market.equalsIgnoreCase("Kraken")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "Kraken");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "30", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "30", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "30", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "30", "Kraken");

                                }
                            }
                            if (market.equalsIgnoreCase("Poloniex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "30", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "30", "Poloniex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {


                                    getBTCchartMonth("BCH", "USD", "30", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "30", "Poloniex");

                                }

                            }
                            if (market.equalsIgnoreCase("BTCE")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "30", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "30", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "30", "BTCE");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "30", "BTCE");


                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "30", "BTCE");


                                }

                            }
                        }
                            break;
                    case R.id.timeseries_three_month:
                        if(market != null && !market.isEmpty()) {

                            if (market.equalsIgnoreCase("Bittrex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "90", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "90", "BitTrex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "90", "BitTrex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "90", "BitTrex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "90", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "90", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "90", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "90", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitstamp")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "90", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "90", "Bitstamp");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "90", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "90", "Bitstamp");

                                }
                            }
                            if (market.equalsIgnoreCase("Kraken")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "Kraken");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "90", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "90", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "90", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "90", "Kraken");

                                }
                            }
                            if (market.equalsIgnoreCase("Poloniex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "90", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "90", "Poloniex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {


                                    getBTCchartMonth("BCH", "USD", "90", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "90", "Poloniex");

                                }

                            }
                            if (market.equalsIgnoreCase("BTCE")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "90", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "90", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "90", "BTCE");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "90", "BTCE");


                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "90", "BTCE");


                                }

                            }
                        }

                        break;
                    case R.id.timeseries_week:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("Bittrex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "7", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "7", "BitTrex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "7", "BitTrex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "7", "BitTrex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "7", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "7", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "7", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "7", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitstamp")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "7", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "7", "Bitstamp");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "7", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "7", "Bitstamp");

                                }
                            }
                            if (market.equalsIgnoreCase("Kraken")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "Kraken");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "7", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "7", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "7", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "7", "Kraken");

                                }
                            }
                            if (market.equalsIgnoreCase("Poloniex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "7", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "7", "Poloniex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {


                                    getBTCchartMonth("BCH", "USD", "7", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "7", "Poloniex");

                                }

                            }
                            if (market.equalsIgnoreCase("BTCE")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "7", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "7", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "7", "BTCE");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "7", "BTCE");


                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "7", "BTCE");


                                }

                            }
                        }
                        break;
                    case R.id.timeseries_day:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("Bittrex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "1", "BitTrex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "1", "BitTrex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "1", "BitTrex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "1", "BitTrex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "1", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "1", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "1", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("BCH", "USD", "1", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitstamp")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "1", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "1", "Bitstamp");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "1", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "1", "Bitstamp");

                                }
                            }
                            if (market.equalsIgnoreCase("Kraken")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "Kraken");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getBTCchartMonth("ETH", "USD", "1", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getBTCchartMonth("LTC", "USD", "1", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "1", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getBTCchartMonth("XRP", "USD", "1", "Kraken");

                                }
                            }
                            if (market.equalsIgnoreCase("Poloniex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "1", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "1", "Poloniex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {


                                    getBTCchartMonth("BCH", "USD", "1", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "1", "Poloniex");

                                }

                            }
                            if (market.equalsIgnoreCase("BTCE")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getBTCchartMonth("BTC", "USD", "1", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getBTCchartMonth("ETH", "USD", "1", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getBTCchartMonth("LTC", "USD", "1", "BTCE");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getBTCchartMonth("BCH", "USD", "1", "BTCE");


                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getBTCchartMonth("XRP", "USD", "1", "BTCE");


                                }

                            }
                        }
                        break;
                    case R.id.timeseries_hour:
                        if(market != null && !market.isEmpty()) {
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getChart("LTC", "USD", "60", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getChart("ETH", "USD", "60", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getChart("BCH", "USD", "60", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("Bitstamp")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getChart("ETH", "USD", "60", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getChart("LTC", "USD", "60", "Bitstamp");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "Bitstamp");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getChart("XRP", "USD", "60", "Bitstamp");

                                }
                            }
                            if (market.equalsIgnoreCase("Bitfinex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getChart("LTC", "USD", "60", "Bitfinex");


                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getChart("ETH", "USD", "60", "Bitfinex");
                                }

                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "Bitfinex");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getChart("BCH", "USD", "60", "Bitfinex");
                                }
                            }
                            if (market.equalsIgnoreCase("BitTrex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "BitTrex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getChart("ETH", "USD", "60", "BitTrex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getChart("LTC", "USD", "60", "BitTrex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "BitTrex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getChart("XRP", "USD", "60", "BitTrex");

                                }
                            }
                            if (market.equalsIgnoreCase("Kraken")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "Kraken");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {
                                    getChart("ETH", "USD", "60", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("LTC")) {
                                    getChart("LTC", "USD", "60", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "Kraken");
                                }
                                if (coin.equalsIgnoreCase("XRP")) {
                                    getChart("XRP", "USD", "60", "Kraken");

                                }
                            }
                            if (market.equalsIgnoreCase("Poloniex")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getChart("ETH", "USD", "60", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getChart("LTC", "USD", "60", "Poloniex");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {


                                    getChart("BCH", "USD", "60", "Poloniex");

                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getChart("XRP", "USD", "60", "Poloniex");

                                }

                            }
                            if (market.equalsIgnoreCase("BTCE")) {
                                if (coin.equalsIgnoreCase("BTC")) {
                                    getChart("BTC", "USD", "60", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("ETH")) {

                                    getChart("ETH", "USD", "60", "BTCE");

                                }
                                if (coin.equalsIgnoreCase("LTC")) {

                                    getChart("LTC", "USD", "60", "BTCE");
                                }
                                if (coin.equalsIgnoreCase("BCH")) {

                                    getChart("BCH", "USD", "60", "BTCE");


                                }
                                if (coin.equalsIgnoreCase("XRP")) {

                                    getChart("XRP", "USD", "60", "BTCE");


                                }

                            }
                        }
                        break;

                }


            }
        });
        //market null check


    }

    private void getComments(String market,String coin) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://androidandme.in").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getPost(market,coin,new retrofit.Callback<retrofit.client.Response>() {
            @Override
            public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                try {
                    BufferedReader buffered=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;
                    StringBuilder stringbuilder=new StringBuilder();

                    while ((output=buffered.readLine())!=null){
                        stringbuilder.append(output);

                    }
                    Log.d("asd",""+output);
                    Toast.makeText(GraphActivity.this, ""+output, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray=new JSONArray(""+stringbuilder);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObj=jsonArray.getJSONObject(i);
                        Comments comments=new Comments();
                        comments.setEmail(jsonObj.getString("email"));
                        comments.setMarketname(jsonObj.getString("marketname"));
                        comments.setMessage(jsonObj.getString("c_message"));
                        comments.setProfilepic(jsonObj.getString("photouri"));
                        comments.setDate(jsonObj.getString("c_dateandtime"));
                        arraylistcomments.add(comments);
                        commentAdapter = new CommentsAdapter(arraylistcomments, GraphActivity.this);
                        postdatalist.setAdapter(commentAdapter);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManagement.isLoggedIn()){
            HashMap<String,String> data=sessionManagement.getUserDetails();
            email=data.get(SessionManagement.KEY_EMAIL);
            photo=data.get(SessionManagement.KEY_PHOTOURI);
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
                Toast.makeText(GraphActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.userprofile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_user:
                if (sessionManagement.isLoggedIn()) {
                    Toast.makeText(this, "User Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GraphActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(GraphActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(GraphActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd/MM");
            Date netDate = (new Date(timeStamp*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        CandleEntry datum=arrayList.get(e.getXIndex());

        open.setText(String.valueOf(datum.getOpen()));
        low.setText(String.valueOf(datum.getLow()));
        high.setText(String.valueOf(datum.getHigh()));
        close.setText(String.valueOf(datum.getClose()));
        date.setText(String.valueOf(al.get(e.getXIndex())));
        //Toast.makeText(this, ""+e.getVal()+" "+datum.getHigh(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    ////////////////////Bitstamp

}
