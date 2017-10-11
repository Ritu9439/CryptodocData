package stock.cryptodoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import stock.cryptodoc.R;
import stock.cryptodoc.model.CryptoImage;
import stock.cryptodoc.model.ForeignMarket;
import stock.cryptodoc.ui.activity.GraphActivity;


/**
 * Created by sai on 29/8/17.
 */

public class ForeignMarketAdapter extends RecyclerView.Adapter<ForeignMarketAdapter.MyViewHolder> {

    ArrayList<ForeignMarket> arrayList;
    ArrayList<CryptoImage> arrayList2;

    Context context;

    public ForeignMarketAdapter(ArrayList<ForeignMarket> arrayList, Context context,ArrayList<CryptoImage> arrayList2) {
        this.arrayList = arrayList;
        this.arrayList2 = arrayList2;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we call inflator over here...
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.foreignmarketitem, parent, false);

        return new MyViewHolder(v, context,arrayList);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

       /* if (position == 0) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        else if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.DKGRAY);
        }
        else if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }*/
        ForeignMarket foreignMarket=arrayList.get(position);
        holder.name.setText(foreignMarket.getLASTMARKET());
        holder.price.setText(foreignMarket.getPRICE());
            Glide.with(context).load(foreignMarket.getIMAGE()).error(R.mipmap.ic_launcher).into(holder.coinimage);



Log.d("sdfsdfw",foreignMarket.getIMAGE());







    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,volumepct,price,changepct;
        ImageView coinimage;

        Context ctx;
        ArrayList<ForeignMarket> al;


        public MyViewHolder(View itemView, Context context,ArrayList<ForeignMarket> arrayList) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.marketname);

            coinimage = (ImageView) itemView.findViewById(R.id.coinimage);
            price = (TextView) itemView.findViewById(R.id.price);


al=arrayList;

            ctx = context;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

           ForeignMarket foreignMarket=al.get(position);

            Intent intent=new Intent(ctx, GraphActivity.class);

            intent.putExtra("coin",foreignMarket.getLASTMARKET());
            intent.putExtra("coinprice",foreignMarket.getPRICE());
            intent.putExtra("market",foreignMarket.getMARKETNAME());
            ctx.startActivity(intent);


        }
    }

}