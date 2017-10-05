package stock.cryptodoc.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import stock.cryptodoc.R;
import stock.cryptodoc.model.ForeignMarketListAdapter;
import stock.cryptodoc.ui.activity.DetailedActivity;
import stock.cryptodoc.utils.ApiInterface;
import stock.cryptodoc.utils.CoinsConstants;
import stock.cryptodoc.utils.RecyclerTouchListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForeignFragment extends Fragment {
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList al2=new ArrayList();

    RecyclerView recyclerView;
    ForeignMarketListAdapter foreignMarketListAdapter;
    public ForeignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_foreign, container, false);
        recyclerView= (RecyclerView) v.findViewById(R.id.marketlist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getImageData();

        foreignMarketListAdapter=new ForeignMarketListAdapter(getActivity());
        recyclerView.setAdapter(foreignMarketListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Toast.makeText(getActivity(), ""+ CoinsConstants.marketname[position], Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),DetailedActivity.class);
                intent.putExtra("marketname",""+ CoinsConstants.marketname[position]);
                intent.putExtra("data",al2);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return v;

    }
    public  void getImageData(){

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://androidandme.in/").build();
        ApiInterface myinterface = restAdapter.create(ApiInterface.class);
        myinterface.getImage(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody().in()));
                    String output = null;


                    while ((output = buffer.readLine()) != null) {
                        stringBuilder.append(output);
                    }

                    JSONArray jsonArray=new JSONArray(""+stringBuilder);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        al2.add(jsonArray.getString(i));

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

}
