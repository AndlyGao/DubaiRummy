package com.gamegards.dubairummy.MyAccountDetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.RedeemCoins.RedeemModel;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.Andhar_Bahar;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.DRAGON_TIGER;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.RUMMY;
import static com.gamegards.dubairummy.MyAccountDetails.MyWinnigmodel.TEEN_PATTI;


public class MyWinningAcitivity extends AppCompatActivity {

    RecyclerView rec_winning;
    MyWinningAdapte myWinningAdapte;
    ArrayList<MyWinnigmodel> myWinnigmodelArrayList;
    private static final String MY_PREFS_NAME = "Login_data" ;
    TextView tb_name,nofound;

    ProgressBar progressBar;
    Activity context = this;

    TextView tvGameRecord;
    TextView tvPurchase;
    TextView tvGame;

    TextView tvRedeemhistory;

//     View image_arrow;
//     View image_arrow1;
//     View image_arrow2;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_winning_acitivity);

        progressBar = findViewById(R.id.progressBar);
        rec_winning = findViewById(R.id.recylerview_gifts);
        rec_winning.setLayoutManager(new LinearLayoutManager(this));

        tb_name = findViewById(R.id.txtheader);
        nofound = findViewById(R.id.txtnotfound);
        tb_name.setText("Game History");

        ((ImageView) findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        tvGameRecord = findViewById(R.id.tvGameRecord);
        tvPurchase = findViewById(R.id.tvPurchase);
        tvGame = findViewById(R.id.tvGame);

        tvRedeemhistory = findViewById(R.id.tvRedeemhistory);

//        image_arrow = findViewById(R.id.image_arrow);
//        image_arrow1 = findViewById(R.id.image_arrow1);
//        image_arrow2 = findViewById(R.id.image_arrow2);


        onClickView();

        if(!SharePref.getIsTeenpattiVisible())
        {
            findViewById(R.id.tvTeenpatti).setVisibility(View.GONE);
        }
        if(!SharePref.getIsRummyVisible())
        {
            findViewById(R.id.tvRummy).setVisibility(View.GONE);
        }
        if(!SharePref.getIsAndharBaharVisible())
        {
            findViewById(R.id.tvandharbahar).setVisibility(View.GONE);
        }
        if(!SharePref.getIsDragonTigerVisible())
        {
            findViewById(R.id.tvDragonTiger).setVisibility(View.GONE);
        }

        if(SharePref.getIsTeenpattiVisible())
            UserWinnigAPI(TEEN_PATTI);
        else if(!SharePref.getIsTeenpattiVisible() && SharePref.getIsRummyVisible())
            UserWinnigAPI(RUMMY);
        else if(!SharePref.getIsTeenpattiVisible()
                && !SharePref.getIsRummyVisible()
                && SharePref.getIsAndharBaharVisible())
            CALL_API_getANDHAR_BAHAR();

    }

    private void NoDataVisible(boolean visible){

        nofound.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void onClickView(){
        ((View) findViewById(R.id.tvRedeemhistory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManageTabs(RedeemModel.REDEEM_LIST);

                tvGame.setText("Redeem Number");


                UsersRedeemList();



            }
        });



        ((View) findViewById(R.id.tvGameRecord)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManageTabs(RedeemModel.GAME_LIST);


                tvGame.setText("Game");



                UserWinnigAPI(TEEN_PATTI);



            }
        });

        ((View) findViewById(R.id.tvPurchase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ManageTabs(RedeemModel.TRANSACTION_LIST);


                tvGame.setText("Pay");

//                image_arrow.setVisibility(View.GONE);
//                image_arrow1.setVisibility(View.VISIBLE);

                getPuchaseListAPI();
            }
        });

        ((View) findViewById(R.id.tvTeenpatti)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserWinnigAPI(TEEN_PATTI);

            }
        });

        ((View) findViewById(R.id.tvRummy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserWinnigAPI(RUMMY);


            }
        });

        ((View) findViewById(R.id.tvandharbahar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CALL_API_getANDHAR_BAHAR();

            }
        });


        ((View) findViewById(R.id.tvDragonTiger)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CALL_API_getDragonTigerHistory();

            }
        });
    }

    private void ManageGameType(String viewType){

        getTextView(R.id.tvTeenpatti).setTextColor(viewType.equalsIgnoreCase(TEEN_PATTI)
                ? Functions.getColor(context,R.color.red)
                : Functions.getColor(context,R.color.white)
        );

        getTextView(R.id.tvRummy).setTextColor(viewType.equalsIgnoreCase(RUMMY)
                ? Functions.getColor(context,R.color.red)
                : Functions.getColor(context,R.color.white)
        );

        getTextView(R.id.tvandharbahar).setTextColor(viewType.equalsIgnoreCase(Andhar_Bahar)
                ? Functions.getColor(context,R.color.red)
                : Functions.getColor(context,R.color.white)
        );

        getTextView(R.id.tvDragonTiger).setTextColor(viewType.equalsIgnoreCase(DRAGON_TIGER)
                ? Functions.getColor(context,R.color.red)
                : Functions.getColor(context,R.color.white)
        );



    }

    private TextView getTextView(int id){

        return ((TextView)findViewById(id));
    }

    private void ManageTabs(int viewType){

        findViewById(R.id.lnrGamesType).setVisibility(viewType == RedeemModel.GAME_LIST
                ? View.VISIBLE : View.GONE);


        tvGameRecord.setBackground(viewType == RedeemModel.GAME_LIST
                ? getDrawable(R.drawable.d_orange_corner)
                : getDrawable(R.drawable.d_white_corner));

        tvGameRecord.setTextColor(viewType == RedeemModel.GAME_LIST
                ? getResources().getColor(R.color.white)
                : getResources().getColor(R.color.black));

//        image_arrow.setVisibility(viewType == RedeemModel.GAME_LIST
//                ? View.VISIBLE
//                : View.GONE);



        tvPurchase.setBackground(viewType == RedeemModel.TRANSACTION_LIST
                ? getDrawable(R.drawable.d_orange_corner)
                : getDrawable(R.drawable.d_white_corner));

        tvPurchase.setTextColor(viewType == RedeemModel.TRANSACTION_LIST
                ? getResources().getColor(R.color.white)
                : getResources().getColor(R.color.black));

//        image_arrow1.setVisibility(viewType == RedeemModel.TRANSACTION_LIST
//                ? View.VISIBLE
//                : View.GONE);


        tvRedeemhistory.setBackground(viewType == RedeemModel.REDEEM_LIST
                ? getDrawable(R.drawable.d_orange_corner)
                : getDrawable(R.drawable.d_white_corner));

        tvRedeemhistory.setTextColor(viewType == RedeemModel.REDEEM_LIST
                ? getResources().getColor(R.color.white)
                : getResources().getColor(R.color.black));

//        image_arrow2.setVisibility(viewType == RedeemModel.REDEEM_LIST
//                ? View.VISIBLE
//                : View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void UsersRedeemList() {


        final ArrayList<RedeemModel> redeemModelArrayList = new ArrayList();
        NoDataVisible(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_Redeem_History_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Functions.LOGE("MyWinningActivity",""+Const.USER_Redeem_History_LIST+"\n"+response);

                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.d("response", "onResponse: " + response);

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("List");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    RedeemModel model = new RedeemModel();
                                    model.setId(jsonObject1.getString("id"));
                                    model.setCoin(jsonObject1.getString("coin"));
                                    model.setMobile(jsonObject1.getString("mobile"));
                                    model.setUser_name(jsonObject1.getString("user_name"));
                                    model.setUser_mobile(jsonObject1.getString("user_mobile"));
                                    model.setStatus(jsonObject1.getString("status"));
                                    model.setUpdated_date(jsonObject1.getString("updated_date"));
                                    model.ViewType = RedeemModel.REDEEM_LIST;


                                    redeemModelArrayList.add(model);
                                }

                                Collections.reverse(redeemModelArrayList);


                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);


                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                        if(redeemModelArrayList.size() <= 0)
                            NoDataVisible(true);

                        UserRedeemHistoryAdapter userWinnerAdapter = new UserRedeemHistoryAdapter(context, redeemModelArrayList);
                        rec_winning.setAdapter(userWinnerAdapter);




                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                NoDataVisible(true);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void UserWinnigAPI(final String type){

        ManageGameType(type);
        NoDataVisible(false);
        final ProgressDialog progressDialog = new ProgressDialog(MyWinningAcitivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Listing...");
        progressDialog.show();

        myWinnigmodelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WINNIG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Functions.LOGE("MyWinningActivity",""+Const.USER_WINNIG+"\n"+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");

                            if(code.equals("200"))
                            {


                                JSONArray ListArray = jsonObject.getJSONArray("GameWins");
                                if(ListArray.length() > 0)
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
                                            model.invest = welcome_bonusObject.optInt("invest",0);
                                            model.setBet(welcome_bonusObject.getString("bet"));
                                            model.setRoom_id(welcome_bonusObject.getString("room_id"));
                                            model.game_type = Andhar_Bahar;
                                            model.ViewType = RedeemModel.GAME_LIST;
                                            myWinnigmodelArrayList.add(model);

                                        }
                                    }

                                    if(type.equalsIgnoreCase(TEEN_PATTI))
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


                                    if(type.equalsIgnoreCase(RUMMY))
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

                                    }







                                }
                                else {
                                    NoDataVisible(true);
                                }

                            }
                            else {
                                NoDataVisible(true);
                            }







                        } catch (JSONException e) {
                            e.printStackTrace();
                            NoDataVisible(true);

                        }



                        myWinningAdapte = new MyWinningAdapte(MyWinningAcitivity.this,myWinnigmodelArrayList);
                        rec_winning.setAdapter(myWinningAdapte);


                        progressDialog.dismiss();
                        HideProgressBar(true);



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                HideProgressBar(true);
                Functions.showToast(MyWinningAcitivity.this, "Something went wrong");
                NoDataVisible(true);


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id",prefs.getString("user_id", ""));
//                params.put("user_id","54");
                Functions.LOGE("MyWinningActivity",""+Const.USER_WINNIG+"\n"+params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void CALL_API_getANDHAR_BAHAR() {

        NoDataVisible(false);
        ManageGameType(Andhar_Bahar);

        myWinnigmodelArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GETHISTORY,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();

                        Functions.LOGE("CALL_API_getANDHAR_BAHAR",""+Const.GETHISTORY+"\n"+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equalsIgnoreCase("200")) {


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


                            }

                            Collections.reverse(myWinnigmodelArrayList);

                            myWinningAdapte = new MyWinningAdapte(MyWinningAcitivity.this,myWinnigmodelArrayList);
                            rec_winning.setAdapter(myWinningAdapte);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(myWinnigmodelArrayList.size() <= 0)
                            NoDataVisible(true);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                NoDataVisible(true);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("room_id", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }


    private void getPuchaseListAPI(){


        final ProgressDialog progressDialog = new ProgressDialog(MyWinningAcitivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Listing...");
        progressDialog.show();

        final ArrayList<MyWinnigmodel> redeemModelArrayList = new ArrayList();
        NoDataVisible(false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_WINNIG,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Functions.LOGE("MyWinningActivity",""+Const.USER_WINNIG+"\n"+response);

                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.d("response", "onResponse: " + response);

                            if (code.equalsIgnoreCase("200")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("AllPurchase");
//                                JSONArray WalletLog = jsonObject.getJSONArray("WalletLog");
//
//                                for (int i = 0; i < WalletLog.length(); i++) {
//                                    JSONObject jsonObject1 = WalletLog.getJSONObject(i);
//
//                                    MyWinnigmodel model = new MyWinnigmodel();
//                                    model.setId(jsonObject1.optString("id"));
//                                    model.setCoin(jsonObject1.optString("coin","-"));
//                                    model.price = jsonObject1.optString("price","admin");
//                                    model.added_date = jsonObject1.optString("added_date","-");
//                                    model.setUpdated_date(jsonObject1.optString("added_date","-"));
//                                    model.ViewType = RedeemModel.TRANSACTION_LIST;
//
//
////                                    redeemModelArrayList.add(model);
//                                }


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    MyWinnigmodel model = new MyWinnigmodel();
                                    model.setId(jsonObject1.optString("id"));
                                    model.setCoin(jsonObject1.optString("coin","-"));
                                    model.price = jsonObject1.optString("price","admin");
                                    model.added_date = jsonObject1.optString("updated_date");
                                    model.setUpdated_date(jsonObject1.optString("updated_date"));
                                    model.ViewType = RedeemModel.TRANSACTION_LIST;


                                    redeemModelArrayList.add(model);
                                }

                                myWinningAdapte = new MyWinningAdapte(MyWinningAcitivity.this,redeemModelArrayList);
                                rec_winning.setAdapter(myWinningAdapte);
                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(context, message);


                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                        progressDialog.dismiss();

                        if(redeemModelArrayList.size() <= 0)
                            NoDataVisible(true);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");
                progressDialog.dismiss();
                NoDataVisible(true);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Const.TOKEN);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void CALL_API_getDragonTigerHistory() {

        NoDataVisible(false);
        ManageGameType(DRAGON_TIGER);

        myWinnigmodelArrayList.clear();

        HashMap params = new HashMap();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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


                    myWinningAdapte = new MyWinningAdapte(MyWinningAcitivity.this,myWinnigmodelArrayList);
                    rec_winning.setAdapter(myWinningAdapte);

                    if(myWinnigmodelArrayList.size() <= 0)
                        NoDataVisible(true);

                }

            }
        });

    }

    LinearLayout lnrTaps;
    private void createGamesDetailsTabs(String text,int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_boards_heading,null);
        lnrTaps.addView(view);

        TextView tvGameRecord = view.findViewById(R.id.tvGameRecord);

        tvGameRecord.setText(""+text);
        tvGameRecord.setTag(""+text);

        tvGameRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMainTabsClick(text);
            }
        });


    }

    private void onMainTabsClick(String selected_tab){

        for (int i = 0; i < lnrTaps.getChildCount() ; i++) {

            View view = lnrTaps.getChildAt(i);

            TextView tvGameRecord = view.findViewById(R.id.tvGameRecord);

            if(tvGameRecord.getText().toString().equalsIgnoreCase(selected_tab))
            {

                tvGameRecord.setBackground(getDrawable(R.drawable.d_orange_corner));
                tvGameRecord.setTextColor(getResources().getColor(R.color.white));

                ManageTabsClick(selected_tab);


            }
            else {
                tvGameRecord.setBackground(getDrawable(R.drawable.d_white_corner));
                tvGameRecord.setTextColor(getResources().getColor(R.color.black));
            }


        }

    }

    private final String GAMES = "Games";
    private final String REDEEM = "Redeem";
    private final String TRANSACTIONS = "Transactions";
    private final String REFER_AND_EARN = "Referred List";
    private void ManageTabsClick(String viewType) {

        findViewById(R.id.lnrGamesType).setVisibility(viewType == GAMES
                ? View.VISIBLE : View.GONE);

        getTextView(R.id.tvCash).setText("Cash");

        if(viewType.equalsIgnoreCase(GAMES))
        {

            tvGame.setText("Game");

            UserWinnigAPI(TEEN_PATTI);
        }
        else if(viewType.equalsIgnoreCase(REDEEM))
        {
            tvGame.setText("Redeem Number");
            UsersRedeemList();
        }
        else if(viewType.equalsIgnoreCase(TRANSACTIONS))
        {
            tvGame.setText("Pay");
//                image_arrow.setVisibility(View.GONE);
//                image_arrow1.setVisibility(View.VISIBLE);
            getPuchaseListAPI();
        }
        else if(viewType.equalsIgnoreCase(REFER_AND_EARN))
        {
            tvGame.setText("Referred Name");
            getTextView(R.id.tvCash).setText("Referred Number");

            UserWinnigAPI(REFER_AND_EARN);
        }
    }



    private void HideProgressBar(boolean visible){
        progressBar.setVisibility(!visible ? View.VISIBLE : View.GONE);
    }
}
