package com.gamegards.dubairummy.MyAccountDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.RedeemCoins.RedeemModel;
import com.gamegards.dubairummy.Utils.Variables;

import java.util.ArrayList;

public class UserRedeemHistoryAdapter extends RecyclerView.Adapter<UserRedeemHistoryAdapter.myholder> {

    Context homepage;
    ArrayList<RedeemModel> winnigmodelArrayList;

    public UserRedeemHistoryAdapter(Context homepage, ArrayList<RedeemModel> winnigmodelArrayList) {
        this.homepage = homepage;
        this.winnigmodelArrayList = winnigmodelArrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(homepage).inflate(R.layout.redeem_itemview, parent, false);

        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        //Picasso.with(homepage).load(Const.IMGAE_PATH + winnigmodelArrayList.get(position).getUserimage()).into(holder.imgprofile);

        View view = holder.itemView;
        RedeemModel model = winnigmodelArrayList.get(position);

        getTextView(view,R.id.tvSerial).setText(""+(position + 1));
        getTextView(view,R.id.tvDates).setText(""+model.getUpdated_date());
        getTextView(view,R.id.tvMobile).setText(""+model.getMobile());
        getTextView(view,R.id.tvCoins).setText(""+ Variables.CURRENCY_SYMBOL+model.getCoin());



    }

    private TextView getTextView(View view,int id){
        return ((TextView) view.findViewById(id));
    }

    @Override
    public int getItemCount() {
        return winnigmodelArrayList.size();
    }

    class myholder extends RecyclerView.ViewHolder {

        public myholder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
