package stock.cryptodoc.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import stock.cryptodoc.R;

/**
 * Created by Administrator on 17-09-2017.
 */

public class CoinsConstants {
    StringBuilder stringBuilder=new StringBuilder();
    public static String marketname[]={"Bittrex","Bitfinex","Bitstamp ","Poloniex","Kraken","BTCE"};
    public static String coinmarket[]={"BTC","ETH","LTC ","XRP","BCH"};


}
