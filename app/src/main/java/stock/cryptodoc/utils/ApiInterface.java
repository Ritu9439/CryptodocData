package stock.cryptodoc.utils;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit2.Call;
import stock.cryptodoc.model.Example;

/**
 * Created by Administrator on 16-09-2017.
 */

public interface ApiInterface {
    @GET("/v1/exchange/ticker")
    void getCoinsecureApi(Callback<Response> responseCallback);
    @GET("/api/v1/market/ticker/btc/inr")
    void getZebapi(Callback<Response> responseCallback);
    @GET("/api/ticker")
    void getEthexIndia(Callback<Response> responseCallback);
    @GET("/api/bitcoins/rates")
    void getBitxoxo(Callback<Response> responseCallback);
        @GET("/trade?all")
    void getUnocoinApi(Callback<Response> responseCallback);
    @GET("/buy-bitcoins-online/IN/india/.json")
    void getLocalbitcoins(Callback<Response> responseCallback);

    @GET("/sell-bitcoins-online/IN/india/.json")
    void getLocalbitcoinssell(Callback<Response> responseCallback);


    @GET("/data/pricemulti?fsyms=BTC,ETH,LTC,XRP,BCH&tsyms=USD&e=Bitfinex")
    void getBitfinex(Callback<Response> responseCallback);
    @GET("/data/pricemulti?fsyms=BTC,ETH,LTC,XRP,BCH&tsyms=USD&e=Kraken")
    void getKraken(Callback<Response> responseCallback);

    @GET("/data/pricemulti?fsyms=BTC,ETH,BCH,LTC,XRP&tsyms=USD&e=Poloniex")
    void getPoloniex(Callback<Response> responseCallback);

    @GET("/data/pricemulti?fsyms=BTC,ETH,LTC&tsyms=USD&e=BTCE")
    void getBTCE(Callback<Response> responseCallback);

    @GET("/data/pricemulti?fsyms=BTC,LTC,XRP&tsyms=USD&e=Bitstamp")
    void getBitstamp(Callback<Response> responseCallback);


    @GET("/api/data/coinsnapshot/")
    void getBitcoinsForeignBTC(@Query("fsym") String fsym, @Query("tsym") String tsym, Callback<Response> responseCallback);

//Bitfinex

    @retrofit2.http.GET("/data/histoday?fsym=BTC&tsym=USD&limit=90&e=Bitfinex")
    Call<Example> getChart();


    @retrofit2.http.GET("/data/histoday?fsym=ETH&tsym=USD&limit=90&e=Bitfinex")
    Call<Example> getChartETH();


    @retrofit2.http.GET("/data/histoday?fsym=XRP&tsym=USD&limit=90&e=Bitfinex")
    Call<Example> getChartXRP();
    @retrofit2.http.GET("/data/histoday?fsym=BCH&tsym=USD&limit=90&e=Bitfinex")
    Call<Example> getChartBCH();
    @retrofit2.http.GET("/data/histoday?fsym=LTC&tsym=USD&limit=90&e=Bitfinex")
    Call<Example> getChartLTC();



    @retrofit2.http.GET("/data/histoday")
    Call<Example> getChartLTCMonth(@retrofit2.http.Query("fsym") String fsym, @retrofit2.http.Query("tsym") String tsym, @retrofit2.http.Query("limit") String limit, @retrofit2.http.Query("e") String e);

    @retrofit2.http.GET("/data/histominute")
    Call<Example> getChart(@retrofit2.http.Query("fsym") String fsym, @retrofit2.http.Query("tsym") String tsym, @retrofit2.http.Query("limit") String limit, @retrofit2.http.Query("e") String e);
    @retrofit2.http.GET("/data/histominute?fsym=BTC&tsym=INR&limit=60&eETHexIndia")
    Call<Example> getChartsETH();
//Comments

    //add user
    @FormUrlEncoded
    @POST("/adduser.php")
    public void addUser(@Field("email") String email,
                        @Field("photouri") String photouri,Callback<Response> callback);


    @FormUrlEncoded
    @POST("/addcomments.php")
    public void addComments(@Field("email") String email,
                        @Field("c_message") String c_message, @Field("c_dateandtime") String c_dateandtime, @Field("marketname")String marketname, @Field("coinname")String coinname,Callback<Response> callback);


    @GET("/getcommentsbyuser.php")
    void getPost(@Query("marketname") String marketname,@Query("coinname") String coinname, Callback<Response> responseCallback);


    @GET("/api/data/coinsnapshot/?fsym=BTC&tsym=INR")
    void getDataCoins(Callback<Response> callback);

    @GET("/data/pricemulti?fsyms=BTC,ETH,LTC,XRP,BCH&tsyms=USD&e=BitTrex")
    void getBitrexData(Callback<Response> responseCallback);

    @GET("/getimages.php")
    void getImage(Callback<Response> responseCallback);

}
