package com.gamegards.dubairummy.Details.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.gamegards.dubairummy.Activity.Homepage.MY_PREFS_NAME;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.Andhar_Bahar;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.DRAGON_TIGER;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.RUMMY;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.TEEN_PATTI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel;
import com.gamegards.dubairummy.MyAccountDetails.MyWinningAdapte;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.RedeemCoins.RedeemModel;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DialogGameHistory {

    Dialog alert;
    Context context;
    LinearLayout tabLayout;
    TextView nofound;
    ProgressBar progressBar;
    RecyclerView rec_winning;
    MyWinningAdapte myWinningAdapte;

    public DialogGameHistory(Context context) {
        this.context = context;
        alert = new Dialog(context,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        alert.setContentView(R.layout.dialog_historygame);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tabLayout = alert.findViewById(R.id.lnrTabs);
        nofound = alert.findViewById(R.id.txtnotfound);
        progressBar = alert.findViewById(R.id.progressBar);
        rec_winning = alert.findViewById(R.id.rec_winning);
        rec_winning.setLayoutManager(new LinearLayoutManager(context));

        CreateTabsLayout(0, R.string.teenpatti);
        CreateTabsLayout(1, R.string.rummy);
        CreateTabsLayout(2, R.string.andharbahar);
        CreateTabsLayout(3, R.string.dragontiger);

        alert.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void CreateTabsLayout(final int position, int name) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_gamehistory_tabs, null);
        String strtitle = context.getString(name);
        view.setTag("" + strtitle);

        TextView title = view.findViewById(R.id.tvGameRecord);

        title.setText(strtitle);


        if (position == 0) {
            title.setTextColor(context.getResources().getColor(R.color.white));
            title.setBackground(context.getResources().getDrawable(R.drawable.d_orange_corner));
            CALL_API_TEENPATII();
        } else {
            title.setTextColor(context.getResources().getColor(R.color.black));
            title.setBackground(context.getResources().getDrawable(R.drawable.d_white_corner));
        }

        if (view != null)
            tabLayout.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerPostion(strtitle);
            }
        });

    }

    private void setPagerPostion(String name) {
        for (int i = 0; i < 4 ; i++) {

            View view  = tabLayout.getChildAt(i);
            TextView title = view.findViewById(R.id.tvGameRecord);

            if(Functions.getStringFromTextView(title).equalsIgnoreCase(name))
            {
                title.setTextColor(context.getResources().getColor(R.color.white));
                title.setBackground(context.getResources().getDrawable(R.drawable.d_orange_corner));

                if(i == 0)
                {
                    CALL_API_TEENPATII();
                }
                else
                if(i == 1)
                {
                    CALL_API_RUMMY();
                }
                else
                if(i == 2)
                {
                    CALL_API_getANDHAR_BAHAR();
                }
                else if(i == 3)
                {
                    CALL_API_getDragonTigerHistory();
                }

            } else {
                title.setTextColor(context.getResources().getColor(R.color.black));
                title.setBackground(context.getResources().getDrawable(R.drawable.d_white_corner));
            }

        }
    }

    public void show(){alert.show();}
    public void dismiss(){alert.dismiss();}

    ArrayList<MyWinnigmodel> myWinnigmodelArrayList;

    private void CALL_API_TEENPATII(){

        HideProgressBar(false);
        NoDataVisible(false);
        myWinnigmodelArrayList = new ArrayList<>();

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));

        ApiRequest.Call_Api(context, Const.TEENPATTI_GAMELOG_HISTORY, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    String code = jsonObject.getString("code");

                    if(code.equals("200"))
                    {
                        JSONArray TeenPattiGameLog = jsonObject.optJSONArray("TeenPattiGameLog");
                        if(TeenPattiGameLog != null)
                        {
                            for (int i = 0; i < TeenPattiGameLog.length() ; i++) {

                                JSONObject ListObject= TeenPattiGameLog.getJSONObject(i);
                                MyWinnigmodel usermodel = new MyWinnigmodel();

                                usermodel.id = ListObject.optString("game_id");
                                usermodel.table_id = ListObject.optString("game_id");
                                usermodel.amount = ListObject.optString("winning_amount");
                                usermodel.invest = ListObject.optInt("invest",0);
                                usermodel.winner_id = ListObject.optString("winner_id");
                                usermodel.added_date = ListObject.optString("added_date");
                                usermodel.game_type = TEEN_PATTI;
                                usermodel.ViewType = RedeemModel.GAME_LIST;

                                myWinnigmodelArrayList.add(usermodel);
                            }
                        }

                        Collections.reverse(myWinnigmodelArrayList);
                    }
                    else {
                        NoDataVisible(true);
                    }

                    myWinningAdapte = new MyWinningAdapte(context,myWinnigmodelArrayList);
                    rec_winning.setAdapter(myWinningAdapte);



                } catch (JSONException e) {
                    e.printStackTrace();
                    NoDataVisible(true);
                }

                HideProgressBar(true);
            }
        });
    }

    private void CALL_API_RUMMY(){

        HideProgressBar(false);
        NoDataVisible(false);
        myWinnigmodelArrayList = new ArrayList<>();

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));

        ApiRequest.Call_Api(context, Const.RUMMY_GAMELOG_HISTORY, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    String code = jsonObject.getString("code");

                    if(code.equals("200"))
                    {
                        JSONArray RummyGameLog = jsonObject.optJSONArray("RummyGameLog");
                        if(RummyGameLog != null)
                        {
                            for (int i = 0; i < RummyGameLog.length() ; i++) {

                                JSONObject ListObject= RummyGameLog.getJSONObject(i);
                                MyWinnigmodel usermodel = new MyWinnigmodel();

                                usermodel.id = ListObject.optString("game_id");
                                usermodel.table_id = ListObject.optString("game_id");

                                int win_amount = ListObject.optInt("amount",0);
                                if(win_amount > 0)
                                    usermodel.amount = ListObject.optString("amount");

                                usermodel.invest = ListObject.optInt("amount",0);
                                usermodel.winner_id = ListObject.optString("winner_id");
                                usermodel.added_date = ListObject.optString("added_date");
                                usermodel.game_type = RUMMY;
                                usermodel.ViewType = RedeemModel.GAME_LIST;

                                myWinnigmodelArrayList.add(usermodel);
                            }
                        }

                        Collections.reverse(myWinnigmodelArrayList);
                    }
                    else {
                        NoDataVisible(true);
                    }

                    myWinningAdapte = new MyWinningAdapte(context,myWinnigmodelArrayList);
                    rec_winning.setAdapter(myWinningAdapte);



                } catch (JSONException e) {
                    e.printStackTrace();
                    NoDataVisible(true);
                }

                HideProgressBar(true);
            }
        });
    }

    private void CALL_API_getANDHAR_BAHAR() {

        HideProgressBar(false);
        NoDataVisible(false);
        myWinnigmodelArrayList.clear();

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));

        ApiRequest.Call_Api(context, Const.GETHISTORY, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    String code = jsonObject.getString("code");

                    if(code.equals("200"))
                    {

                        JSONArray arraygame_dataa = jsonObject.optJSONArray("GameLog");
                        if(arraygame_dataa != null)
                        {
                            for (int i = 0; i < arraygame_dataa.length(); i++) {
                                JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                MyWinnigmodel model = new MyWinnigmodel();
                                model.setId(welcome_bonusObject.getString("id"));
                                model.setAnder_baher_id(welcome_bonusObject.getString("ander_baher_id"));
                                model.setAdded_date(welcome_bonusObject.getString("added_date"));
                                model.setAmount(welcome_bonusObject.getString("amount"));
                                model.setWinning_amount(welcome_bonusObject.getString("winning_amount"));
                                model.amount = welcome_bonusObject.optString("winning_amount");
                                model.invest = welcome_bonusObject.optInt("amount",0);
                                model.setBet(welcome_bonusObject.getString("bet"));
                                model.setRoom_id(welcome_bonusObject.getString("room_id"));
                                model.game_type = Andhar_Bahar;
                                model.ViewType = RedeemModel.GAME_LIST;
                                myWinnigmodelArrayList.add(model);

                            }
                        }

                        Collections.reverse(myWinnigmodelArrayList);
                    }
                    else {
                        NoDataVisible(true);
                    }

                    myWinningAdapte = new MyWinningAdapte(context,myWinnigmodelArrayList);
                    rec_winning.setAdapter(myWinningAdapte);



                } catch (JSONException e) {
                    e.printStackTrace();
                    NoDataVisible(true);
                }

                HideProgressBar(true);
            }
        });


    }


    private void CALL_API_getDragonTigerHistory() {

        HideProgressBar(false);
        NoDataVisible(false);

        myWinnigmodelArrayList.clear();

        HashMap params = new HashMap();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id", prefs.getString("user_id", ""));
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.DRAGON_TIGER_HISTORY, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equalsIgnoreCase("200")) {


                            JSONArray arraygame_dataa = jsonObject.optJSONArray("GameLog");
                            if(arraygame_dataa != null)
                            {
                                for (int i = 0; i < arraygame_dataa.length(); i++) {
                                    JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                    MyWinnigmodel model = new MyWinnigmodel();
                                    model.setId(welcome_bonusObject.optString("id"));
                                    model.setAnder_baher_id(welcome_bonusObject.optString("ander_baher_id"));
                                    model.setAdded_date(welcome_bonusObject.optString("added_date"));
                                    model.setAmount(welcome_bonusObject.optString("amount"));
                                    model.setWinning_amount(welcome_bonusObject.optString("winning_amount"));
                                    model.amount = welcome_bonusObject.optString("winning_amount");
                                    model.invest = welcome_bonusObject.optInt("amount",0);
                                    model.setBet(welcome_bonusObject.optString("bet"));
                                    model.setRoom_id(welcome_bonusObject.optString("room_id"));
                                    model.game_type = DRAGON_TIGER;
                                    model.ViewType = RedeemModel.GAME_LIST;
                                    myWinnigmodelArrayList.add(model);

                                }
                            }


                        }

                        Collections.reverse(myWinnigmodelArrayList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    myWinningAdapte = new MyWinningAdapte(context,myWinnigmodelArrayList);
                    rec_winning.setAdapter(myWinningAdapte);

                    if(myWinnigmodelArrayList.size() <= 0)
                        NoDataVisible(true);
                    HideProgressBar(true);

                }

            }
        });

    }


    private void NoDataVisible(boolean visible){

        nofound.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void HideProgressBar(boolean gone){
        progressBar.setVisibility(!gone ? View.VISIBLE : View.GONE);
    }

}
