package stock.cryptodoc.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import stock.cryptodoc.adapter.IndianMarketAdapter;
import stock.cryptodoc.model.IndianMarket;
import stock.cryptodoc.utils.ApiInterface;

import static android.os.Looper.getMainLooper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {
    ArrayList<IndianMarket> arrayList;
    ListView listView;


    Bundle argument;
    IndianMarketAdapter marketListAdapter;
    ArrayList<IndianMarket> arrayList2 = new ArrayList<>();;
    String url[] = {"https://api.coinsecure.in", "https://www.unocoin.com", "https://www.zebapi.com", "https://ethexindia.com/", "https://api.bitxoxo.com/", "https://localbitcoins.com/"};
    StringBuilder stringbuilder = new StringBuilder();
    StringBuilder stringbuilder2 = new StringBuilder();
    StringBuilder stringbuilder4 = new StringBuilder();
    StringBuilder stringbuilder3 = new StringBuilder();
    StringBuilder stringbuilder5 = new StringBuilder();
    StringBuilder stringbuilder6 = new StringBuilder();
    StringBuilder stringbuilder7 = new StringBuilder();
SwipeRefreshLayout swiperefresh;
    StringBuilder stri = new StringBuilder();

    ;
    IndianMarket local;
    Handler handler;
    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dash_board, container, false);
        listView = (ListView) v.findViewById(R.id.marketlist);
        argument=getArguments();
        arrayList = argument.getParcelableArrayList("queries");
        Log.d("ddddddd",""+arrayList);

        swiperefresh=v.findViewById(R.id.swiperefresh);
        if (arrayList!=null && !arrayList.isEmpty()){
            marketListAdapter = new IndianMarketAdapter(arrayList, getActivity());
            listView.setAdapter(marketListAdapter);

        }
        else {
callCoinsecure(url[0]);
        }
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callCoinsecure(url[0]);
            }
        });


        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (arrayList2!=null && !arrayList2.isEmpty()){
                    arrayList2.clear();
                    callCoinsecure(url[0]);
                    Log.d("asdad",""+arrayList);
                    someHandler.postDelayed(this, 20000);

                }

            }
        }, 10);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        arrayList = argument.getParcelableArrayList("queries");

        if (arrayList!=null && !arrayList.isEmpty()){
            marketListAdapter = new IndianMarketAdapter(arrayList, getActivity());
            listView.setAdapter(marketListAdapter);

        }
        else {
            callCoinsecure(url[0]);
        }
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
                        stri.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stri);
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

                                    local = new IndianMarket();
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

            }
        });
    }

    public void callCoinsecure(final String s) {


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getCoinsecureApi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder);
                    JSONObject childobj = jsonobiect.getJSONObject("message");


                    String buy = childobj.getString("ask");
                    String sell = childobj.getString("bid");

                    IndianMarket indianMarket = new IndianMarket("Coin Secure", buy.substring(0, buy.length() - 2), sell.substring(0, sell.length() - 2), url[0], "BTC");

                    arrayList2.add(indianMarket);
                    callZebapi(url[2]);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callZebapi(url[2]);
            }
        });
    }

    private void callZebapi(final String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getZebapi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder2.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder2);
                    String buy = jsonobiect.getString("buy");
                    String sell = jsonobiect.getString("sell");


                    IndianMarket marketData = new IndianMarket("Zebapi", buy, sell, s, "BTC");

                    arrayList2.add(marketData);
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
    }

    private void getEthexIndia(final String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getEthexIndia(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder4.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder4);
                    String buy = jsonobiect.getString("last_traded_price");
                    long last_traded_time = jsonobiect.getLong("last_traded_time_IST");
                    IndianMarket marketData = new IndianMarket("ETHEXIndia", buy, "-", s, "BTC");
                    arrayList2.add(marketData);
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
    }

    private void getBitxoxoIndia(final String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getBitxoxo(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder5.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder5);
                    String buy = jsonobiect.getString("buy");
                    String sell = jsonobiect.getString("sell");

                    IndianMarket marketData = new IndianMarket("BitXOXO", buy, sell, s, "BTC");
                    arrayList2.add(marketData);
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
    }

    private void callUnocoin(final String s) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(s).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getUnocoinApi(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder3.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder3);
                    String buy = jsonobiect.getString("buy");
                    String sell = jsonobiect.getString("sell");
                    IndianMarket marketData = new IndianMarket("Unocoin", buy, sell, s, "BTC");

                    arrayList2.add(marketData);
                    callData();
                    //
                    // getLocalbitCoinsbuy();


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

    private void getLocalbitCoinsbuy() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(url[5]).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getLocalbitcoins(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder6.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder6);
                    JSONObject childobj = jsonobiect.getJSONObject("data");
                    JSONArray jsonArray = childobj.getJSONArray("ad_list");

                    JSONObject jobj = jsonArray.getJSONObject(2);
                    JSONObject dataobj = jobj.getJSONObject("data");
                    String temp_price = dataobj.getString("temp_price");
                    local = new IndianMarket();
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
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(url[5]).build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getLocalbitcoinssell(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;

                    while ((output = buffer.readLine()) != null) {
                        stringbuilder7.append(output);
                    }
                    JSONObject jsonobiect = new JSONObject("" + stringbuilder7);
                    JSONObject childobj = jsonobiect.getJSONObject("data");
                    JSONArray jsonArray = childobj.getJSONArray("ad_list");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONObject dataobj = jobj.getJSONObject("data");
                    String temp_price = dataobj.getString("temp_price");

                    local.setSell(temp_price);
                    arrayList2.add(local);

                    if (arrayList!=null && arrayList.isEmpty())
                    {
                        arrayList.clear();
                        marketListAdapter = new IndianMarketAdapter(arrayList2, getActivity());
                        listView.setAdapter(marketListAdapter);
                    }
                    else {
                        marketListAdapter = new IndianMarketAdapter(arrayList2, getActivity());
                        listView.setAdapter(marketListAdapter);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (arrayList.size() > 0)
                {
                    arrayList.clear();
                    marketListAdapter = new IndianMarketAdapter(arrayList2, getActivity());
                    listView.setAdapter(marketListAdapter);
                }
            }
        });

    }

}
