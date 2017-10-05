package stock.cryptodoc.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
import stock.cryptodoc.adapter.ForeignMarketAdapter;
import stock.cryptodoc.model.CryptoImage;
import stock.cryptodoc.model.ForeignMarket;
import stock.cryptodoc.utils.ApiInterface;
import stock.cryptodoc.utils.CoinsConstants;


public class DetailedActivity extends AppCompatActivity {
    String marketname = "";
     String data="";
    StringBuilder stringbuilder=new StringBuilder();
    ArrayList<CryptoImage> ar=new ArrayList<>();
    ArrayList<ForeignMarket> al = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList al2=new ArrayList();
    ForeignMarketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
al2=getIntent().getStringArrayListExtra("data");

        Log.d("asdada",""+al2);
        recyclerView = (RecyclerView) findViewById(R.id.formarketlist);
        marketname = getIntent().getStringExtra("marketname");
        getSupportActionBar().setTitle(marketname);
        Toast.makeText(this, "" + marketname, Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[0])) {
getBittrex();
            Toast.makeText(this, "Yes" + marketname, Toast.LENGTH_SHORT).show();

        }

        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[1])) {


            getBitfinex();
        }

        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[2])) {
            getBitStamp();
        }

        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[3])) {
getPoloniex();
        }

        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[4])) {
            getKraken();
        }

        if (marketname.equalsIgnoreCase(CoinsConstants.marketname[5])) {
getBTCE();
        }


    }

    private void getBittrex() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getBitrexData(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");

                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setMARKETNAME("BitTrex");
                    foreignmarket.setIMAGE(""+al2.get(0));


                    al.add(foreignmarket);

                    JSONObject jobj1 = jobject.getJSONObject("ETH");
                    String btcusd1 = jobj1.getString("USD");
                    ForeignMarket foreignmarket1 = new ForeignMarket("ETH", "$ " + btcusd1, "", "");
                    foreignmarket1.setMARKETNAME("BitTrex");

                    foreignmarket1.setIMAGE(""+al2.get(1));

                    al.add(foreignmarket1);
                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setMARKETNAME("BitTrex");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    al.add(foreignmarket2);
                    JSONObject jobj3 = jobject.getJSONObject("XRP");
                    String btcusd3 = jobj3.getString("USD");
                    ForeignMarket foreignmarket3 = new ForeignMarket("XRP", "$ " + btcusd3, "", "");
                    foreignmarket3.setMARKETNAME("BitTrex");
                    foreignmarket3.setIMAGE(""+al2.get(3));

                    al.add(foreignmarket3);
                    JSONObject jobj4 = jobject.getJSONObject("BCH");
                    String btcusd4 = jobj4.getString("USD");
                    ForeignMarket foreignmarket4 = new ForeignMarket("BCH", "$ " + btcusd4, "", "");
                    foreignmarket4.setMARKETNAME("BitTrex");
                    foreignmarket4.setIMAGE(""+al2.get(4));

                    al.add(foreignmarket4);

                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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





    private void getKraken() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getKraken(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");
                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setMARKETNAME("Kraken");
                    foreignmarket.setIMAGE(""+al2.get(0));
                    al.add(foreignmarket);

                    JSONObject jobj1 = jobject.getJSONObject("ETH");
                    String btcusd1 = jobj1.getString("USD");
                    ForeignMarket foreignmarket1 = new ForeignMarket("ETH", "$ " + btcusd1, "", "");
                    foreignmarket1.setMARKETNAME("Kraken");
                    foreignmarket1.setIMAGE(""+al2.get(1));

                    al.add(foreignmarket1);
                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setMARKETNAME("Kraken");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    al.add(foreignmarket2);
                    JSONObject jobj3 = jobject.getJSONObject("XRP");
                    String btcusd3 = jobj3.getString("USD");
                    ForeignMarket foreignmarket3 = new ForeignMarket("XRP", "$ " + btcusd3, "", "");
                    foreignmarket3.setMARKETNAME("Kraken");
                    foreignmarket3.setIMAGE(""+al2.get(3));

                    al.add(foreignmarket3);
                    JSONObject jobj4 = jobject.getJSONObject("BCH");
                    String btcusd4 = jobj4.getString("USD");
                    ForeignMarket foreignmarket4 = new ForeignMarket("BCH", "$ " + btcusd4, "", "");
                    foreignmarket4.setMARKETNAME("Kraken");
                    foreignmarket4.setIMAGE(""+al2.get(4));

                    al.add(foreignmarket4);

                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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
    private void getBitfinex() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getBitfinex(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");
                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setMARKETNAME("Bitfinex");
                    foreignmarket.setIMAGE(""+al2.get(0));

                    al.add(foreignmarket);

                    JSONObject jobj1 = jobject.getJSONObject("ETH");
                    String btcusd1 = jobj1.getString("USD");
                    ForeignMarket foreignmarket1 = new ForeignMarket("ETH", "$ " + btcusd1, "", "");
                    foreignmarket1.setMARKETNAME("Bitfinex");
                    foreignmarket1.setIMAGE(""+al2.get(1));

                    al.add(foreignmarket1);
                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    foreignmarket2.setMARKETNAME("Bitfinex");
                    al.add(foreignmarket2);
                    JSONObject jobj3 = jobject.getJSONObject("XRP");
                    String btcusd3 = jobj3.getString("USD");
                    ForeignMarket foreignmarket3 = new ForeignMarket("XRP", "$ " + btcusd3, "", "");
                    foreignmarket3.setIMAGE(""+al2.get(3));

                    foreignmarket3.setMARKETNAME("Bitfinex");
                    al.add(foreignmarket3);
                    JSONObject jobj4 = jobject.getJSONObject("BCH");
                    String btcusd4 = jobj4.getString("USD");
                    ForeignMarket foreignmarket4 = new ForeignMarket("BCH", "$ " + btcusd4, "", "");
                    foreignmarket4.setMARKETNAME("Bitfinex");
                    foreignmarket4.setIMAGE(""+al2.get(4));

                    al.add(foreignmarket4);

                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(DetailedActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getBTCE() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getBTCE(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");
                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setIMAGE(""+al2.get(0));
                    foreignmarket.setMARKETNAME("BTCE");
                    al.add(foreignmarket);

                    JSONObject jobj1 = jobject.getJSONObject("ETH");
                    String btcusd1 = jobj1.getString("USD");
                    ForeignMarket foreignmarket1 = new ForeignMarket("ETH", "$ " + btcusd1, "", "");
                    foreignmarket1.setMARKETNAME("BTCE");
                    foreignmarket1.setIMAGE(""+al2.get(1));


                    al.add(foreignmarket1);
                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setMARKETNAME("BTCE");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    al.add(foreignmarket2);


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    private void getPoloniex() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getPoloniex(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");
                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setMARKETNAME("Poloniex");
                    foreignmarket.setIMAGE(""+al2.get(0));

                    al.add(foreignmarket);

                    JSONObject jobj1 = jobject.getJSONObject("ETH");
                    String btcusd1 = jobj1.getString("USD");
                    ForeignMarket foreignmarket1 = new ForeignMarket("ETH", "$ " + btcusd1, "", "");
                    foreignmarket1.setMARKETNAME("Poloniex");
                    foreignmarket1.setIMAGE(""+al2.get(1));

                    al.add(foreignmarket1);
                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setMARKETNAME("Poloniex");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    al.add(foreignmarket2);
                    JSONObject jobj3 = jobject.getJSONObject("XRP");
                    String btcusd3 = jobj3.getString("USD");
                    ForeignMarket foreignmarket3 = new ForeignMarket("XRP", "$ " + btcusd3, "", "");
                    foreignmarket3.setMARKETNAME("Poloniex");
                    foreignmarket3.setIMAGE(""+al2.get(3));

                    al.add(foreignmarket3);
                    JSONObject jobj4 = jobject.getJSONObject("BCH");
                    String btcusd4 = jobj4.getString("USD");
                    ForeignMarket foreignmarket4 = new ForeignMarket("BCH", "$ " + btcusd4, "", "");
                    foreignmarket4.setMARKETNAME("Poloniex");
                    foreignmarket4.setIMAGE(""+al2.get(4));

                    al.add(foreignmarket4);

                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    private void getBitStamp() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://min-api.cryptocompare.com").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getBitstamp(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    Toast.makeText(DetailedActivity.this, "" + stringbuilder, Toast.LENGTH_SHORT).show();

                    JSONObject jobject = new JSONObject("" + stringbuilder);
                    JSONObject jobj = jobject.getJSONObject("BTC");
                    String btcusd = jobj.getString("USD");
                    Toast.makeText(DetailedActivity.this, "" + btcusd, Toast.LENGTH_SHORT).show();

                    ForeignMarket foreignmarket = new ForeignMarket("BTC", "$ " + btcusd, "", "");
                    foreignmarket.setIMAGE(""+al2.get(0));
                    foreignmarket.setMARKETNAME("Bitstamp");

                    al.add(foreignmarket);

                    JSONObject jobj2 = jobject.getJSONObject("LTC");
                    String btcusd2 = jobj2.getString("USD");
                    ForeignMarket foreignmarket2 = new ForeignMarket("LTC", "$ " + btcusd2, "", "");
                    foreignmarket2.setMARKETNAME("Bitstamp");
                    foreignmarket2.setIMAGE(""+al2.get(2));

                    al.add(foreignmarket2);
                    JSONObject jobj3 = jobject.getJSONObject("XRP");
                    String btcusd3 = jobj3.getString("USD");
                    ForeignMarket foreignmarket3 = new ForeignMarket("XRP", "$ " + btcusd3, "", "");
                    foreignmarket3.setMARKETNAME("Bitstamp");
                    foreignmarket3.setIMAGE(""+al2.get(3));

                    al.add(foreignmarket3);


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    private void getForeignData(String s) {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);

        myinterface.getBitcoinsForeignBTC("BTC", "USD", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonObject = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonObject.getJSONObject("Data");
                    JSONArray jsonarray = childobj.getJSONArray("Exchanges");
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject obj = jsonarray.getJSONObject(i);
                        String market = obj.getString("MARKET");
                        Log.d("naasdme", market + " " + jsonarray.length());
                        if (market.equalsIgnoreCase("Bitfinex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                        }

                        if (market.equalsIgnoreCase("Kraken")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("Kraken", market);
                        }
                        if (market.equalsIgnoreCase("Poloniex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);
                            Log.d("Poloniex", market);

                        }
                        if (market.equalsIgnoreCase("BTCE")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("helloa", "" + al.size());
                            Log.d("BTCE", market);

                        }


                    }


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


///////////////////////ETH


    private void getForeignDataETH() {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);

        myinterface.getBitcoinsForeignBTC("ETH", "USD", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonObject = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonObject.getJSONObject("Data");
                    JSONArray jsonarray = childobj.getJSONArray("Exchanges");
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject obj = jsonarray.getJSONObject(i);
                        String market = obj.getString("MARKET");
                        Log.d("naasdme", market + " " + jsonarray.length());
                        if (market.equalsIgnoreCase("Bitfinex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                        }

                        if (market.equalsIgnoreCase("Kraken")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("Kraken", market);
                        }
                        if (market.equalsIgnoreCase("Poloniex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);
                            Log.d("Poloniex", market);

                        }
                        if (market.equalsIgnoreCase("BTCE")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("helloa", "" + al.size());
                            Log.d("BTCE", market);

                        }


                    }


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    ////LTC
    private void getForeignDataLTC(String s) {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);

        myinterface.getBitcoinsForeignBTC("LTC", "USD", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonObject = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonObject.getJSONObject("Data");
                    JSONArray jsonarray = childobj.getJSONArray("Exchanges");
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject obj = jsonarray.getJSONObject(i);
                        String market = obj.getString("MARKET");
                        Log.d("naasdme", market + " " + jsonarray.length());
                        if (market.equalsIgnoreCase("Bitfinex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                        }

                        if (market.equalsIgnoreCase("Kraken")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("Kraken", market);
                        }
                        if (market.equalsIgnoreCase("Poloniex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);
                            Log.d("Poloniex", market);

                        }
                        if (market.equalsIgnoreCase("BTCE")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("helloa", "" + al.size());
                            Log.d("BTCE", market);

                        }


                    }


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


//////////BCH


    private void getForeignDataBCH(String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);

        myinterface.getBitcoinsForeignBTC("BCH", "USD", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonObject = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonObject.getJSONObject("Data");
                    JSONArray jsonarray = childobj.getJSONArray("Exchanges");
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject obj = jsonarray.getJSONObject(i);
                        String market = obj.getString("MARKET");
                        Log.d("naasdme", market + " " + jsonarray.length());
                        if (market.equalsIgnoreCase("Bitfinex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                        }

                        if (market.equalsIgnoreCase("Kraken")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("Kraken", market);
                        }
                        if (market.equalsIgnoreCase("Poloniex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);
                            Log.d("Poloniex", market);

                        }
                        if (market.equalsIgnoreCase("BTCE")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("helloa", "" + al.size());
                            Log.d("BTCE", market);

                        }


                    }


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    //XRP
    private void getForeignDataXRP(String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.cryptocompare.com/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);

        myinterface.getBitcoinsForeignBTC("XRP", "USD", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonObject = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonObject.getJSONObject("Data");
                    JSONArray jsonarray = childobj.getJSONArray("Exchanges");
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject obj = jsonarray.getJSONObject(i);
                        String market = obj.getString("MARKET");
                        Log.d("naasdme", market + " " + jsonarray.length());
                        if (market.equalsIgnoreCase("Bitfinex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                        }

                        if (market.equalsIgnoreCase("Kraken")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("Kraken", market);
                        }
                        if (market.equalsIgnoreCase("Poloniex")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);
                            Log.d("Poloniex", market);

                        }
                        if (market.equalsIgnoreCase("BTCE")) {
                            String PRICE = obj.getString("PRICE");
                            ForeignMarket foreignmarket = new ForeignMarket(market, "$ " + PRICE, "", "");
                            al.add(foreignmarket);

                            Log.d("helloa", "" + al.size());
                            Log.d("BTCE", market);

                        }


                    }


                    adapter = new ForeignMarketAdapter(al, DetailedActivity.this,ar);
                    recyclerView.setAdapter(adapter);


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}