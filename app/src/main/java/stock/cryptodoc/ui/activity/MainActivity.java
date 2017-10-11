package stock.cryptodoc.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import stock.cryptodoc.R;
import stock.cryptodoc.model.IndianMarket;
import stock.cryptodoc.utils.ApiInterface;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    Animation animFadeIn;
    RelativeLayout realtive;
    TextView txtprogress;
    ArrayList<IndianMarket> arrayList=new ArrayList<>();
    String url[]={"https://api.coinsecure.in","https://www.unocoin.com","https://www.zebapi.com","https://ethexindia.com/","https://api.bitxoxo.com/","https://localbitcoins.com/"};
    StringBuilder stringbuilder=new StringBuilder();
    StringBuilder stringbuilder2=new StringBuilder();
    StringBuilder stringbuilder4=new StringBuilder();
    StringBuilder stringbuilder3=new StringBuilder();
    StringBuilder stringbuilder5=new StringBuilder();
    StringBuilder stringbuilder6 =new StringBuilder();;
    StringBuilder stringbuilder7 =new StringBuilder();
    IndianMarket local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        local = new IndianMarket();

        txtprogress= (TextView) findViewById(R.id.txtprogress);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        // set animation listener
        animFadeIn.setAnimationListener(this);
        // animation for image
        realtive = (RelativeLayout) findViewById(R.id.relativelayout);
        // start the animation
        realtive.setVisibility(View.VISIBLE);
        realtive.startAnimation(animFadeIn);
        txtprogress.setText("1%");


        int[] color = {Color.DKGRAY, Color.CYAN};

        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR; // or TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 50,color,position, tile_mode);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    public void callCoinsecure(final String s) {

        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getCoinsecureApi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {

                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder);
                    JSONObject childobj=jsonobiect.getJSONObject("message");


                    String buy=childobj.getString("ask");
                    String sell=childobj.getString("bid");

                    IndianMarket indianMarket=new IndianMarket("Coin Secure",buy.substring(0, buy.length() - 2),sell.substring(0, sell.length() - 2),url[0],"BTC");
                    arrayList.add(indianMarket);
                    txtprogress.setText("50%");
                    callZebapi(url[2]);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    callZebapi(url[2]);

                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callZebapi(url[2]);

            }
        });
    }
    private void callZebapi(final String s){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getZebapi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder2.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder2);
                    String buy=jsonobiect.getString("buy");
                    String sell=jsonobiect.getString("sell");


                    IndianMarket marketData=new IndianMarket("Zebapi",buy,sell,s,"BTC");

                    arrayList.add(marketData);
                    txtprogress.setText("75%");
                    getEthexIndia(url[3]);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                getEthexIndia(url[3]);
            }
        });
    } private void getEthexIndia(final String s){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getEthexIndia(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder4.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder4);
                    String buy=jsonobiect.getString("last_traded_price");
                    long last_traded_time=jsonobiect.getLong("last_traded_time_IST");
                    IndianMarket marketData=new IndianMarket("ETHEXIndia",buy,"-",s,"BTC");
                    arrayList.add(marketData);
                    txtprogress.setText("78%");
                    getBitxoxoIndia(url[4]);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                getBitxoxoIndia(url[4]);
            }
        });
    }private void getBitxoxoIndia(final String s){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getBitxoxo(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder5.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder5);
                    String buy=jsonobiect.getString("buy");
                    String sell=jsonobiect.getString("sell");

                    IndianMarket marketData=new IndianMarket("BitXOXO",buy,sell,s,"BTC");
                    arrayList.add(marketData);
                    txtprogress.setText("80%");
                    callUnocoin(url[1]);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callUnocoin(url[1]);
            }
        });
    }private void callUnocoin(final String s){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getUnocoinApi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder3.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder3);
                    String buy=jsonobiect.getString("buy");
                    String sell=jsonobiect.getString("sell");
                    IndianMarket marketData=new IndianMarket("Unocoin",buy,sell,s,"BTC");

                    arrayList.add(marketData);
                    txtprogress.setText("85%");
                   // getLocalbitCoinsbuy();
                    callData();


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callData();
            }
        });
    }
    public void callData(){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getDataCoins(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;
                    while ((output = buffer.readLine()) != null) {
                        stringbuilder6.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder6);
                    String resp=jsonobiect.getString("Response");
                    if (resp!=null && !resp.isEmpty())
                    {
                        JSONObject childjson=jsonobiect.getJSONObject("Data");
                        JSONArray jsonArray=childjson.getJSONArray("Exchanges");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String localbitcoins=object.getString("MARKET");
                            if (localbitcoins!=null && !localbitcoins.isEmpty()){
                                if (localbitcoins.equalsIgnoreCase("LocalBitcoins")) {
                                    String localprice = object.getString("PRICE");


                                    local.setBuy(localprice);
                                    local.setCoin("BTC");
                                    local.setMarket("LocalBitcoins");
                                    local.setUrl(url[5]);
                                    getLocalbitCoinssell();
                                }
                            }

                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                getLocalbitCoinssell();
            }
        });
    }

    private void getLocalbitCoinsbuy(){
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(url[5]).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getLocalbitcoins(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder6.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder6);
                    JSONObject childobj=jsonobiect.getJSONObject("data");
                    JSONArray jsonArray=childobj.getJSONArray("ad_list");

                    JSONObject jobj=jsonArray.getJSONObject(2);
                    JSONObject dataobj=jobj.getJSONObject("data");
                    String temp_price=dataobj.getString("temp_price");

                    txtprogress.setText("95%");
                    local.setBuy(temp_price);
                    local.setCoin("BTC");
                    local.setMarket("Local Bitcoins");
                    local.setUrl(url[5]);
                    getLocalbitCoinssell();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                getLocalbitCoinssell();
            }
        });
    }
    private void getLocalbitCoinssell() {
        RestAdapter restAdapter=new RestAdapter.Builder().setEndpoint(url[5]).build();
        ApiInterface myinterface=restAdapter.create(ApiInterface.class);
        myinterface.getLocalbitcoinssell(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer=new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output=null;

                    while ((output=buffer.readLine())!=null){
                        stringbuilder7.append(output);
                    }
                    JSONObject jsonobiect=new JSONObject(""+stringbuilder7);
                    JSONObject childobj=jsonobiect.getJSONObject("data");
                    JSONArray jsonArray=childobj.getJSONArray("ad_list");
                    JSONObject jobj=jsonArray.getJSONObject(0);
                    JSONObject dataobj=jobj.getJSONObject("data");
                    String temp_price=dataobj.getString("temp_price");

                    local.setSell(temp_price);
                    arrayList.add(local);
                    txtprogress.setText("100%");
                    realtive.clearAnimation();

                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    intent.putParcelableArrayListExtra("queries", arrayList);
                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                intent.putParcelableArrayListExtra("queries", arrayList);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (arrayList.isEmpty()){
            callCoinsecure(url[0]);
        }

    }
}
