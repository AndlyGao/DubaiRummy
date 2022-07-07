package com.gamegards.dubairummy._DragonTiger;

import static com.gamegards.dubairummy.Utils.Functions.ANIMATION_SPEED;
import static com.gamegards.dubairummy._AdharBahar.Fragments.GameFragment.MY_PREFS_NAME;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gamegards.dubairummy.BaseActivity;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Utils.Animations;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;
import com.gamegards.dubairummy.Utils.Variables;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DragonTiger_A extends BaseActivity implements View.OnClickListener {

    Activity context = this;

    TextView txtName,txtBallence,txt_gameId,txtGameRunning,txtGameBets,tvWine,tvLose;
    Button btGameAmount;
    ImageView imgpl1circle,ivTigerCard,ivDragonCard,ivGadhi,ivWine,ivLose;

    View rltTiger,rltDragon,rltTie;
    View rltTigerChips,rltDragonChips,rltTieChips;

    View ChipstoDealer,ChipstoUser;


    private final String TIGER = "tiger";
    private final String DRAGON = "dragon";
    private final String TIE = "tie";

    private String BET_ON = "";

    private int minGameAmt = 50;
    private int maxGameAmt = 500;
    private int GameAmount = 50;
    private int StepGameAmount = 50;
    private int _30second = 30000;
    private int GameTimer = 30000;
    private int timer_interval = 1000;

    private String game_id = "";
    CountDownTimer pleasewaintCountDown;
    View lnrButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_tiger);

        Initialization();

        setDataonUser();
    }

    private void Initialization() {
        lnrButtons = findViewById(R.id.lnrButtons);
//        lnrButtons.setVisibility(View.GONE);
        rl_AnimationView = ((RelativeLayout)findViewById(R.id.sticker_animation_layout));
        ChipstoDealer = findViewById(R.id.ChipstoDealer);
        ChipstoUser = findViewById(R.id.ChipstoUser);
        btGameAmount = findViewById(R.id.btGameAmount);

        txtName = findViewById(R.id.txtName);
        imgpl1circle = findViewById(R.id.imgpl1circle);

        ivDragonCard = findViewById(R.id.ivDragonCard);
        ivTigerCard = findViewById(R.id.ivTigerCard);
        ivGadhi = findViewById(R.id.ivGadhi);

        txtBallence = findViewById(R.id.txtBallence);
        txt_gameId = findViewById(R.id.txt_gameId);
        txtGameRunning = findViewById(R.id.txtGameRunning);
        txtGameBets = findViewById(R.id.txtGameBets);

        ivWine = findViewById(R.id.ivWine);
        ivLose = findViewById(R.id.ivlose);
        tvWine = findViewById(R.id.tvWine);
        tvLose = findViewById(R.id.tvlose);

        rltwinnersymble1=findViewById(R.id.rltwinnersymble1);
        rtllosesymble1=findViewById(R.id.rtllosesymble1);

        rltTiger=findViewById(R.id.rltTiger);
        rltDragon=findViewById(R.id.rltDragon);
        rltTie=findViewById(R.id.rltTie);

        rltTigerChips=findViewById(R.id.rltTigerChips);
        rltDragonChips=findViewById(R.id.rltDragonChips);
        rltTieChips=findViewById(R.id.rltTieChips);

        rltTiger.setOnClickListener(this::onClick);
        rltDragon.setOnClickListener(this::onClick);
        rltTie.setOnClickListener(this::onClick);
        findViewById(R.id.imgback).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1plus).setOnClickListener(this::onClick);
        findViewById(R.id.imgpl1minus).setOnClickListener(this::onClick);


        pleaswaitTimer();
        RestartGame(true);

        setDataonUser();

        startService();

        initiAnimation();

    }

    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(1000);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
        blinksAnimation.setStartOffset(700);
    }

    boolean isCardsDisribute = false;
    int timertime = 4000;
    Timer timerstatus;
    private void startService() {

        timerstatus = new Timer();
        timerstatus.scheduleAtFixedRate(new TimerTask() {

                                            @Override
                                            public void run() {

                                                // if (table_id.trim().length() > 0) {

                                                if (isCardsDisribute) {


                                                } else {

                                                    CALL_API_getGameStatus();

                                                }


                                                // }

                                            }

                                        },
//Set how long before to start calling the TimerTask (in milliseconds)
                200,
//Set the amount of time between each execution (in milliseconds)
                timertime);



    }

    CountDownTimer gameStartTime;
    boolean isGameTimerStarted = false;
    private void CardsDistruButeTimer(){

        if(isGameTimerStarted && getTextView(R.id.tvStartTimer).getVisibility() == View.VISIBLE)
            return;

        gameStartTime = new CountDownTimer((time_remaining * timer_interval),timer_interval) {
            @Override
            public void onTick(long millisUntilFinished) {

                isGameTimerStarted = true;
                float count = millisUntilFinished/timer_interval;

                getTextView(R.id.tvStartTimer).setVisibility(View.VISIBLE);
                getTextView(R.id.tvStartTimer).setText(count+"s");

                PlaySaund(R.raw.count_down_timmer);

            }

            @Override
            public void onFinish() {
                stopPlaying();
                isGameTimerStarted = false;
                getTextView(R.id.tvStartTimer).setVisibility(View.INVISIBLE);
            }
        };


        gameStartTime.start();

    }

    private void cancelStartGameTimmer(){
        if(gameStartTime != null)
        {
            gameStartTime.cancel();
            gameStartTime.onFinish();
        }
    }

    private TextView getTextView(int id){

        return ((TextView) findViewById(id));
    }

    @Override
    protected void onDestroy() {
        DestroyGames();
        super.onDestroy();
    }

    private void DestroyGames(){

        cancelStartGameTimmer();

        if (timerstatus !=null ){
            timerstatus.cancel();
        }

        stopPlaying();
    }

    public String main_card;
    public String status = "";
    public String winning;
    private String added_date;
    private String user_id,name,wallet;
    private String profile_pic;
    ArrayList<String> aaraycards  = new ArrayList<>();
    boolean isGameBegning = false;
    boolean isConfirm = false;
    String bet_id = "";
    String betplace = "";
    boolean canbet = false;
    String betvalue = "";
    CountDownTimer counttimerforstartgame;
    CountDownTimer counttimerforcards;
    int time_remaining;
    boolean isCardDistribute = false;
    private void CALL_API_getGameStatus() {

        HashMap params = new HashMap();

        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));

        params.put("room_id", "1");

        ApiRequest.Call_Api(context, Const.DragonTigerStatus, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if (resp != null)
                {

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");

                        if (code.equalsIgnoreCase("200")) {

                            JSONArray arraygame_dataa = jsonObject.getJSONArray("game_data");

                            for (int i = 0; i < arraygame_dataa.length() ; i++) {
                                JSONObject welcome_bonusObject = arraygame_dataa.getJSONObject(i);

                                //  GameStatus model = new GameStatus();
                                game_id  = welcome_bonusObject.getString("id");
                                txt_gameId.setText("GAME ID "+game_id);



                                main_card  = welcome_bonusObject.getString("main_card");
                                // txt_min_max.setText("Min-Max: "+main_card);
                                status  = welcome_bonusObject.getString("status");
                                winning  = welcome_bonusObject.getString("winning");
                                String end_datetime  = welcome_bonusObject.getString("end_datetime");
                                added_date  = welcome_bonusObject.getString("added_date");
                                time_remaining  = welcome_bonusObject.optInt("time_remaining");

                                //  updated_date  = welcome_bonusObject.getString("updated_date");

                            }
                            String onlineuSer = jsonObject.getString("online");
//                            txt_online.setText("Online User "+onlineuSer);
                            JSONArray arrayprofile = jsonObject.getJSONArray("profile");

                            for (int i = 0; i < arrayprofile.length() ; i++) {
                                JSONObject profileObject = arrayprofile.getJSONObject(i);

                                //  GameStatus model = new GameStatus();
                                user_id  = profileObject.getString("id");
                                user_id_player1 = user_id;
                                name  = profileObject.getString("name");
                                wallet  = profileObject.getString("wallet");

                                profile_pic  = profileObject.getString("profile_pic");

                                Picasso.with(context).load(Const.IMGAE_PATH + profile_pic).into(imgpl1circle);

                                //  txtBallence.setText(wallet);
                                txtName.setText(name);

                            }


                            JSONArray arraypgame_cards = jsonObject.getJSONArray("game_cards");

                            for (int i = 0; i < arraypgame_cards.length() ; i++) {
                                JSONObject cardsObject = arraypgame_cards.getJSONObject(i);

                                //  GameStatus model = new GameStatus();
                                String card  = cardsObject.getString("card");
                                aaraycards.add(card);

                            }
//New Game Started here ------------------------------------------------------------------------

                            if (status.equals("0") && !isGameBegning){


                                RestartGame(false);

                                if(time_remaining > 0)
                                {
                                    CardsDistruButeTimer();
                                }
                                else {
                                    cancelStartGameTimmer();
                                }

                            }else if (status.equals("0") && isGameBegning){

                            }

//Game Started here
                            if (status.equals("1") && !isGameBegning){
                                VisiblePleasewaitforNextRound(true);

                            }

                            if (status.equals("1") && isGameBegning){


                                isGameBegning = false;
                                Log.v("game" ,"stoped");
                                if (aaraycards.size() > 0){

                                    cancelStartGameTimmer();

                                    if (counttimerforcards != null){
                                        counttimerforcards.cancel();
                                    }


                                    counttimerforcards = new CountDownTimer(aaraycards.size()*1000, 1000) {

                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            isCardsDisribute = true;

                                            makeWinnertoPlayer("");
                                            makeLosstoPlayer("");


                                            if(aaraycards != null && aaraycards.size() >= 2 && !isCardDistribute)
                                            {
                                                CardAnimationUtils();
                                                isCardDistribute = true;
                                            }


                                        }

                                        @Override
                                        public void onFinish() {

//                                                getStatus();
                                            //secondtimestart(18);
                                            VisiblePleasewaitforNextRound(true);

                                            isCardsDisribute = false;

                                            if(betplace != null && !betplace.equalsIgnoreCase("") && betplace.equalsIgnoreCase(winning))
                                            {
                                                AnimationUtils(true);
                                            }
                                            else {

                                                if(betplace != null && !betplace.equalsIgnoreCase("") && !betplace.equalsIgnoreCase(winning))
                                                {
                                                    AnimationUtils(false);
//                                                    makeLosstoPlayer(SharePref.getU_id());
                                                }

                                            }


                                        }


                                    };

                                    counttimerforcards.start();


                                }


                            }

                        } else {
                            if (jsonObject.has("message")) {

                                Functions.showToast(context, message);


                            }


                        }

                        if (status.equals("1")) {
//                            VisiblePleasewaitforNextRound(true);
                            VisiblePleaseBetsAmount(false);
                        } else {
                            VisiblePleasewaitforNextRound(false);

                            if(!isConfirm)
                                VisiblePleaseBetsAmount(true);
                            makeWinnertoPlayer("");
                            makeLosstoPlayer("");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private void CardAnimationUtils() {

        final View[] fromView = {null};
        final View[] toView = {null};

        fromView[0] = ivGadhi;
        toView[0] = ivDragonCard;
        CardAnimationAnimations(fromView[0], toView[0],false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fromView[0] = ivGadhi;
                toView[0] = ivTigerCard;
                CardAnimationAnimations(fromView[0], toView[0],true);
            }
        },500);


    }

    int coins_count = 10;
    int cards_count = 2;
    boolean isbetonDragon = false;
    boolean isbetonTiger = false;
    boolean isbetTie = false;
    private void AnimationUtils(boolean iswin) {
        coins_count =10;
        isbetonDragon = false;
        isbetonTiger = false;
        isbetTie = false;

        isbetonDragon = BET_ON.equals(DRAGON) ? true : false;
        isbetonTiger = BET_ON.equals(TIGER) ? true : false;
        isbetTie = BET_ON.equals(TIE) ? true : false;

        View fromView = null;
        View toView = null;

        if(isbetonDragon)
        {
            fromView = rltDragonChips;
        }
        else if(isbetonTiger)
        {
            fromView = rltTigerChips;
        }
        else {
            fromView = rltTieChips;
        }

        if(iswin)
        {
            toView = ChipstoUser;
        }
        else {
            toView = ChipstoDealer;
        }

        View finalFromView = fromView;
        View finalToView = toView;
        new CountDownTimer(2000,200) {
            @Override
            public void onTick(long millisUntilFinished) {
                coins_count--;
                ChipsAnimations(finalFromView, finalToView,iswin);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void VisiblePleaseBetsAmount(boolean visible){

        txtGameBets.setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    private void VisiblePleasewaitforNextRound(boolean visible){

        if(blinksAnimation != null)
        {
            isBlinkStart = false;
            txtGameRunning.clearAnimation();
            blinksAnimation.cancel();
        }

        txtGameRunning.setVisibility(visible ? View.VISIBLE : View.GONE);

        if(visible)
        {
            if(!Functions.checkisStringValid(((TextView) findViewById(R.id.txtcountdown)).getText().toString().trim()))
                pleasewaintCountDown.start();

            BlinkAnimation(txtGameRunning);
        }
        else {
            pleasewaintCountDown.cancel();
            pleasewaintCountDown.onFinish();
        }


    }

    private void pleaswaitTimer(){
        pleasewaintCountDown = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long second = millisUntilFinished/1000;

                ((TextView) findViewById(R.id.txtcountdown)).setText(second+"s");

            }

            @Override
            public void onFinish() {
                ((TextView) findViewById(R.id.txtcountdown)).setText("");
            }
        };
    }

    Animation blinksAnimation;
    boolean isBlinkStart = false;
    private void BlinkAnimation(final View view) {

        if(isBlinkStart)
            return;

        isBlinkStart = true;
        view.startAnimation(blinksAnimation);
    }

    private void addCardsTiger(String image_name, int countvaue) {

        int path = 0;
        if(Functions.checkisStringValid(image_name))
            path = Functions.GetResourcePath(""+image_name,context);

        Glide.with(context)
                .load(path > 0 ? path : R.drawable.ic_dt_tiger_card)
                .placeholder(R.drawable.ic_dt_tiger_card)
                .into(ivTigerCard);

    }

    private void addCardDragon(String image_name, int countvaue) {
        int path = 0;
        if(Functions.checkisStringValid(image_name))
            path = Functions.GetResourcePath(""+image_name,context);

        Glide.with(context)
                .load(path > 0 ? path : R.drawable.ic_dt_dragon_card)
                .placeholder(R.drawable.ic_dt_dragon_card)
                .into(ivDragonCard);
    }


    private void putbet(final String type) {


        HashMap params = new HashMap();
        params.put("user_id", SharePref.getInstance().getString("user_id", ""));
        params.put("token", SharePref.getInstance().getString("token", ""));
        params.put("game_id", game_id);
        params.put("bet", type);
        params.put("amount", ""+GameAmount);

        ApiRequest.Call_Api(context, Const.DragonTigerPUTBET, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {

                    try {


                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");


                        if (code.equalsIgnoreCase("200")) {
                            bet_id = jsonObject.getString("bet_id");
                            wallet = jsonObject.getString("wallet");
                            txtBallence.setText(wallet);
                            Functions.showToast(context, "Bet has been added successfully!");

                            betvalue = "";
                            isConfirm = true;

                            VisiblePleaseBetsAmount(false);

                        } else {
                            RemoveChips();

                            if (jsonObject.has("message")) {

                                Functions.showToast(context, message);


                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        RemoveChips();
                    }
                }


            }
        });
    }

    private void cancelbet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.DragonTigerCENCEL_BET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")){

                                wallet = jsonObject.getString("wallet");
                                txtBallence.setText(wallet);


                            }
                            Functions.showToast(context, message);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));

                params.put("game_id", game_id);
                params.put("bet_id", bet_id);
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

    private void repeatBet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.DragonTigerREPEAT_BET,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Log.v("Repeat Responce" , response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("200")){

                                wallet = jsonObject.getString("wallet");
                                String  bet = jsonObject.getString("bet");
                                // bet_id = jsonObject.getString("bet_id");
                                String amount = jsonObject.getString("amount");
                                txtBallence.setText(wallet);
                                betvalue = amount;
                                betplace = bet;
                                if (bet.equals("0")){


                                }else {

                                }

                            }
                            Functions.showToast(context, message);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(context, "Something went wrong");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));

                params.put("game_id", game_id);
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

    private void setDataonUser() {

        txtName.setText(""+ SharePref.getInstance().getString(SharePref.u_name));
        txtBallence.setText(Variables.CURRENCY_SYMBOL+""+ SharePref.getInstance().getString(SharePref.wallet));

        Glide.with(context)
                .load(Const.IMGAE_PATH + SharePref.getInstance().getString(SharePref.u_pic))
                .placeholder(R.drawable.avatar)
                .into(imgpl1circle);


    }

    String user_id_player1 = "";
    RelativeLayout rltwinnersymble1;
    View rtllosesymble1;
    public void makeWinnertoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        addWinLoseImageonView();

        if (chaal_user_id.equals(user_id_player1)) {

            int sound_track =
                    isbetonDragon ?
                            R.raw.dragon_soundtrack
                            : isbetonTiger ? R.raw.tiger_roar_soundtrack
                            : R.raw.tpb_battle_won;

            PlaySaund(sound_track);
            rltwinnersymble1.setVisibility(View.VISIBLE);

        }

    }

    public void makeLosstoPlayer(String chaal_user_id) {

        rltwinnersymble1.setVisibility(View.GONE);
        rtllosesymble1.setVisibility(View.GONE);
        addWinLoseImageonView();

        if (chaal_user_id.equals(user_id_player1)) {

            int sound_track =
                    isbetonDragon? R.raw.tiger_roar_soundtrack :
                    isbetonTiger? R.raw.dragon_soundtrack :
                    R.raw.game_loos_track;

            PlaySaund(sound_track);
            rtllosesymble1.setVisibility(View.VISIBLE);

        }

    }

    public void addWinLoseImageonView(){

        ivWine.setImageDrawable(isbetonDragon ?
                Functions.getDrawable(context,R.drawable.ic_dt_dragon_win)
                : isbetonTiger ? Functions.getDrawable(context,R.drawable.ic_dt_tiger_win)
                : Functions.getDrawable(context,R.drawable.ic_dt_tiegame));

        ivLose.setImageDrawable(isbetonDragon ?
                Functions.getDrawable(context,R.drawable.ic_dt_dragon_win)
                : isbetonTiger ? Functions.getDrawable(context,R.drawable.ic_dt_tiger_win)
                : Functions.getDrawable(context,R.drawable.ic_dt_tiegame));

        tvWine.setText(isbetonDragon ? "Dragon Win"
                : isbetonTiger ? "Tiger Win"
                : "Tie Win");

        tvLose.setText(isbetonDragon ? "Dragon Lose"
                : isbetonTiger ? "Tiger Lose"
                : "Tie Lose");

    }

    private MediaPlayer mp;
    boolean isInPauseState = false;
    public void PlaySaund(int saund) {



        if (!isInPauseState) {
            stopPlaying();
            mp = MediaPlayer.create(this, saund);
            mp.start();

        }


    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInPauseState = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isInPauseState = true;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgback:
            {
                onBackPressed();
            }
            break;

            case R.id.rltTiger:
            {
                AddBet(TIGER);
            }
            break;

            case R.id.rltDragon:
            {
                AddBet(DRAGON);
            }
            break;

            case R.id.rltTie:
            {
//                Functions.showToast(context,"Comming Soon!");
                AddBet(TIE);
            }
            break;

            case R.id.imgpl1plus:
            {

                ChangeGameAmount(true);
            }
            break;

            case R.id.imgpl1minus:
            {
                ChangeGameAmount(false);
            }
            break;
        }
    }

    private void ChangeGameAmount(boolean isPlus){


        if (isConfirm) {
            return;

        }

        if(isPlus && GameAmount < maxGameAmt)
        {
            GameAmount = GameAmount + StepGameAmount ;
        }
        else if(!isPlus && GameAmount > minGameAmt)
        {
            GameAmount = GameAmount - StepGameAmount ;
        }

        btGameAmount.setText(Variables.CURRENCY_SYMBOL+""+GameAmount);
    }

    private void AddBet(String beton) {

        if(!betValidation())
            return;

        BET_ON = beton;

        betplace = beton.equals(DRAGON) ? "0": beton.equals(TIGER) ? "1" : "2";

        rltTigerChips.setVisibility(beton.equals(TIGER) ? View.VISIBLE : View.GONE);
        rltDragonChips.setVisibility(beton.equals(DRAGON) ? View.VISIBLE : View.GONE);
        rltTieChips.setVisibility(beton.equals(TIE) ? View.VISIBLE : View.GONE);

    }

    private void RestartGame(boolean isFromonCreate){


        RemoveChips();

        addCardDragon("0",0);
        addCardsTiger("0",1);

        VisiblePleasewaitforNextRound(false);

        cancelStartGameTimmer();

        isCardDistribute = false;

        txtBallence.setText(wallet);

        removeBet();
        aaraycards.clear();
        if(!isFromonCreate)
            isGameBegning = true;

    }

    private void removeBet(){
        canbet = true;
        isConfirm = false;
        bet_id = "";
        betplace="";
        betvalue = "";
    }

    private void RemoveChips(){
        BET_ON = "";
        rltTigerChips.setVisibility(View.GONE);
        rltDragonChips.setVisibility(View.GONE);
        rltTieChips.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        Functions.Dialog_CancelAppointment(context, "Confirmation", "Leave ?", new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                if(resp.equals("yes"))
                {
                    stopPlaying();
                    finish();
                }
            }
        });
    }

    boolean animationon = false;
    RelativeLayout rl_AnimationView;
    private void ChipsAnimations(View mfromView,View mtoView,boolean iswin){

        animationon = true;


        final View fromView, toView, shuttleView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl_AnimationView.setVisibility(View.VISIBLE);
//        rl_AnimationView.removeAllViews();
        final ImageView sticker = new ImageView(this);

        int stickerId = Functions.GetResourcePath("ic_dt_chips",context);

        int chips_size = (int) getResources().getDimension(R.dimen.chips_size);

        if(stickerId > 0)
            LoadImage().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(chips_size, chips_size));
        rl_AnimationView.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
                if(coins_count <= 0)
                {
                    RemoveChips();
                    rl_AnimationView.removeAllViews();
                    if(!iswin)
                        makeLosstoPlayer(SharePref.getU_id());
                    else
                        makeWinnertoPlayer(SharePref.getU_id());
                }

            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);

        Log.e("MainActivity","FromView : "+fromRect);
        Log.e("MainActivity","toView : "+toRect);

        PlaySaund(R.raw.teenpattichipstotable);


    }

    private void CardAnimationAnimations(View mfromView,View mtoView,boolean isTiger){

        animationon = true;


        final View fromView, toView, shuttleView;

        fromView = mfromView;
        toView = mtoView;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        rl_AnimationView.setVisibility(View.VISIBLE);
//        rl_AnimationView.removeAllViews();
        final ImageView sticker = new ImageView(this);

        int stickerId = R.drawable.backside_card;

        int cards_size = (int) getResources().getDimension(R.dimen.dt_card_width);

        if(stickerId > 0)
            LoadImage().load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(cards_size, cards_size));
        rl_AnimationView.addView(sticker);

        shuttleView = sticker;

        Animations anim = new Animations();
        Animation a = anim.fromAtoB(startX, startY, destX, destY, null, ANIMATION_SPEED, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                shuttleView.setVisibility(View.GONE);
                fromView.setVisibility(View.VISIBLE);
                animationon = false;
                sticker.setVisibility(View.GONE);
                rl_AnimationView.removeAllViews();

                if(!isTiger)
                {
                    addCardDragon(aaraycards.get(0),0);
                }
                else {
                    addCardsTiger(aaraycards.get(1),1);
                }

            }
        });
        sticker.setAnimation(a);
        a.startNow();


        Rect fromRect = new Rect();
        Rect toRect = new Rect();
        fromView.getGlobalVisibleRect(fromRect);
        toView.getGlobalVisibleRect(toRect);


        PlaySaund(R.raw.teenpatticardflip_android);


    }

    private RequestManager LoadImage()
    {
        return  Glide.with(context);
    }

    private boolean betValidation(){

        if (isConfirm) {

            Functions.showToast(context, "Bet Already Confirmed So Not Allowed to Put again");
            return false;

        } else if (!canbet) {
            Functions.showToast(context, "Game Already Started You can not Bet");
            return false;

        }

        return true;
    }

    public void confirmBooking(View view) {

        if(Functions.isStringValid(betplace) && betValidation())
            putbet(betplace);
    }


    public void cancelBooking(View view) {

        cancelbet();
        removeBet();
        RemoveChips();
    }
}