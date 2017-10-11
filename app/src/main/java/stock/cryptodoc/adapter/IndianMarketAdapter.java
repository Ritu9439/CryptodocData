package stock.cryptodoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import stock.cryptodoc.R;
import stock.cryptodoc.model.IndianMarket;
import stock.cryptodoc.ui.activity.IndianGraphActivity;


/**
 * Created by Administrator on 16-09-2017.
 */

public class IndianMarketAdapter extends BaseAdapter {

    ArrayList<IndianMarket> marketDatas;
    Context context;
    int color[]={Color.WHITE,Color.WHITE};

    public IndianMarketAdapter(ArrayList<IndianMarket> marketDatas, Context context) {
        this.marketDatas = marketDatas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return marketDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return marketDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        view=layoutInflater.inflate(R.layout.indianitemlist,null);


      /*  if (i == 0) {
            view.setBackgroundColor(Color.GRAY);
        }
        else if (i % 2 == 1) {
            view.setBackgroundColor(Color.DKGRAY);
        }
        else if (i % 2 == 0) {
            view.setBackgroundColor(Color.GRAY);
        }*/
        TextView tvmarket,tvbuy,tvsell;
        ImageView img;
        img=view.findViewById(R.id.img);
        tvmarket=view.findViewById(R.id.tvmarket);
        tvbuy=view.findViewById(R.id.tvbuy);
        tvsell=view.findViewById(R.id.tvsell);
        IndianMarket marketData=marketDatas.get(i);



        tvmarket.setText(marketData.getMarket());
        tvbuy.setText(marketData.getBuy());
        tvsell.setText(marketData.getSell());

view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        IndianMarket marketData=marketDatas.get(i);
        Intent intent=new Intent(context, IndianGraphActivity.class);
        intent.putExtra("market",marketData.getMarket());


        context.startActivity(intent);
        Toast.makeText(context, ""+marketData.getMarket(), Toast.LENGTH_SHORT).show();

    }
});


        return view;
    }

}
