package stock.cryptodoc.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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


public class IndianGraphActivity extends AppCompatActivity implements OnChartValueSelectedListener,GoogleApiClient.OnConnectionFailedListener  {
    CandleStickChart web;
    Toolbar toolbar;
    ArrayList<Comments> arraylistcomments=new ArrayList<>();
    CommentsAdapter commentAdapter;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
TextView txt_logout;
    String name;
    RecyclerView postdatalist;
    RadioButton timeseries_hour;
    RadioGroup timeseries;
    ArrayList<CandleEntry> arrayList=new ArrayList<>();
    ArrayList<String> al=new ArrayList<>();
    TextView cointv,coinpricetv,open,high,low,close,date;
    FloatingActionButton addNewPostFab;
    SessionManagement sessionManagement;
    LinearLayout newCommentContainer;
    SignInButton txt_signin;
    private int RC_SIGN_INN=9001;

    String email="",photo="",coin="BTC";
    String market;
    EditText commentEditText;
    Button sendButton;
    private Uri photoUrl;
    Dialog mBottomSheetDialog;
 View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_graph);
        toolbar= (Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        sessionManagement=new SessionManagement(IndianGraphActivity.this);

        addNewPostFab= (FloatingActionButton) findViewById(R.id.addNewPostFab);
        newCommentContainer= (LinearLayout) findViewById(R.id.newCommentContainer);
        sendButton= (Button) findViewById(R.id.sendButton);
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
        if (sessionManagement.isLoggedIn()){
            HashMap<String,String> data=sessionManagement.getUserDetails();
            email=data.get(SessionManagement.KEY_EMAIL);
            photo=data.get(SessionManagement.KEY_PHOTOURI);
            getComments(market,coin);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(IndianGraphActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String    date = df.format(c.getTime());
                Toast.makeText(IndianGraphActivity.this, "sdsa"+email, Toast.LENGTH_SHORT).show();
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://androidandme.in").build();
                ApiInterface myinterface = restAdapter.create(ApiInterface.class);
                myinterface.addComments(email, commentEditText.getText().toString(), date, market, coin, new retrofit.Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                        Toast.makeText(IndianGraphActivity.this, "Posted", Toast.LENGTH_SHORT).show();
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
                    /*Intent intent = new Intent(GraphActivity.this, LoginActivity.class);
                    startActivity(intent);*/


                    View v = getLayoutInflater ().inflate (R.layout.bottom_sheet, null);
                    mBottomSheetDialog = new Dialog(IndianGraphActivity.this, R.style.MaterialDialogSheet);
                    mBottomSheetDialog.setContentView (v);
                    txt_logout = (TextView)v.findViewById(R.id.txt_logout);
                    txt_signin = (SignInButton) v.findViewById(R.id.txt_signin);


                    mBottomSheetDialog.setCancelable (true);
                    mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
                    mBottomSheetDialog.show ();

                    txt_logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signOut();
                        }
                    });

                    txt_signin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signIn();

                        }
                    });

                }
            }
        });
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
    private void signOut() {

        // Firebase sign out
        mAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        sessionManagement.logoutUser();

                    }
                });
        // txt_logout.setVisibility(View.GONE);
/*
        tvname.setText(null);
*/
        // txt_signin.setVisibility(View.VISIBLE);

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_INN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManagement.isLoggedIn()){
            HashMap<String,String> data=sessionManagement.getUserDetails();
            email=data.get(SessionManagement.KEY_EMAIL);
            photo=data.get(SessionManagement.KEY_PHOTOURI);
           /* coin="BTC";
            market="Bitfinex";*/
        }
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
                    Toast.makeText(IndianGraphActivity.this, ""+output, Toast.LENGTH_SHORT).show();
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
                        commentAdapter = new CommentsAdapter(arraylistcomments, IndianGraphActivity.this);
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


                    signOut();

                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_INN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Name, email address, and profile photo Url
                            name = user.getEmail();
                            //  String email = user.getEmail();
                            photoUrl = user.getPhotoUrl();

                            // The user's ID, unique to the Firebase project. Do NOT use this value to
                            // authenticate with your backend server, if you have one. Use
                            // FirebaseUser.getToken() instead.
                            String uid = user.getUid();
                        }

                        if (task.isSuccessful()){
                            txt_logout.setVisibility(View.VISIBLE);
                            txt_signin.setVisibility(View.GONE);
                            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://androidandme.in").build();
                            ApiInterface myinterface = restAdapter.create(ApiInterface.class);
                            myinterface.addUser(name, String.valueOf(photoUrl), new retrofit.Callback<retrofit.client.Response>() {
                                @Override
                                public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                                    sessionManagement.createLoginSession(name,String.valueOf(photoUrl));
                                    mBottomSheetDialog.dismiss();
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });

                            /*
                            tvname.setText("Welcome "+name);
*/

                        }else {
                        }
                    }
                });
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
