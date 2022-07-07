package com.gamegards.dubairummy.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamegards.dubairummy.Adapter.WelcomeRewardAdapter;
import com.gamegards.dubairummy.BaseActivity;
import com.gamegards.dubairummy.Comman.DialogSettingOption;
import com.gamegards.dubairummy.Details.GameDetails_A;
import com.gamegards.dubairummy.Utils.Sound;
import com.gamegards.dubairummy._DragonTiger.DragonTiger_A;
import com.gamegards.dubairummy.Fragments.ActiveTables_BF;
import com.gamegards.dubairummy.Fragments.UserInformation_BT;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.RedeemCoins.RedeemActivity;
import com.gamegards.dubairummy.RedeemCoins.RedeemModel;
import com.gamegards.dubairummy.MyAccountDetails.UserRedeemHistoryAdapter;
import com.gamegards.dubairummy.Utils.SharePref;
import com.gamegards.dubairummy.Utils.Variables;
import com.gamegards.dubairummy._AdharBahar.AndharBahar_Home_A;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.Utils.Animations;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy._LuckJackpot.LuckJackPot_A;
import com.gamegards.dubairummy._RummyDeal.Fragments.RummyDealActiveTables_BF;
import com.gamegards.dubairummy._RummyPull.Fragments.RummyActiveTables_BF;
import com.gamegards.dubairummy._SevenUpGames.Sevenup_A;
import com.gamegards.dubairummy._TeenPatti.PublicTable_New;
import com.gamegards.dubairummy.model.WelcomeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.gamegards.dubairummy.Utils.Functions.SetBackgroundImageAsDisplaySize;
import static com.gamegards.dubairummy.Utils.Functions.convertDpToPixel;

public class Homepage extends BaseActivity {

    Animation animBounce, animBlink;
    public static final String MY_PREFS_NAME = "Login_data";
    ImageView imgLogout, imgshare, imaprofile;
    ImageView imgpublicGame, imgPrivategame, ImgCustomePage,iv_andher;
    private String user_id, name, mobile, profile_pic, referral_code, wallet, game_played, bank_detail, adhar_card, upi;
    private TextView txtName, txtwallet, txtproname;
    LinearLayout lnrbuychips, lnrinvite, lnrmail, lnrsetting,lnrvideo;
    Typeface helvatikaboldround, helvatikanormal;
    public String token = "";
    private String game_for_private,app_version;
    SeekBar sBar;
    SeekBar sBarpri;
    ImageView imgCreatetable,imgCreatetablecustom,imgclosetoppri,imgclosetop;
    int pval = 1;
    int pvalpri = 1;
    Button btnCreatetable;
    Button btnCreatetablepri;
    TextView txtStart, txtLimit,txtwalletchips,
            txtwalletchipspri, txtBootamount, txtPotLimit, txtNumberofBlind,
            txtMaximumbetvalue;
    TextView txtStartpri, txtLimitpri, txtBootamountpri, txtPotLimitpri, txtNumberofBlindpri, txtMaximumbetvaluepri;
    RelativeLayout rltimageptofile;
    int version = 0 ;

    RelativeLayout rootView ;


    String base_64 = "";
    ProgressDialog progressDialog;
    Activity context = Homepage.this;

    String REFERRAL_AMOUNT = "referral_amount";
    String JOINING_AMOUNT = "joining_amount";



    Random random = new Random();

    private void emitBubbles() {
        // It will create a thread and attach it to
        // the main thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                int size = random.nextInt( 250);
//                bubbleEmitter.emitBubble(size);
//                bubbleEmitter.setColors(android.R.color.transparent,
//                        android.R.color.holo_blue_light,
//                        android.R.color.holo_blue_bright);
//                emitBubbles();
            }
        }, random.nextInt(500));
    }


//    BubbleEmitterView bubbleEmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        soundMedia = new Sound();
        initSoundPool();
        SharePref.getInstance().init(this);

        findViewById(R.id.rltDragonTiger).setVisibility(SharePref.getIsDragonTigerVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltTeenpatti).setVisibility(SharePref.getIsTeenpattiVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltPrivate).setVisibility(SharePref.getIsPrivateVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltCustom).setVisibility(SharePref.getIsCustomVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltAndharbhar).setVisibility(SharePref.getIsAndharBaharVisible()
                ? View.VISIBLE : View.GONE);

        findViewById(R.id.rltRummy).setVisibility(SharePref.getIsRummyVisible()
                ? View.VISIBLE : View.GONE);


        imgLogout = findViewById(R.id.imgLogout);


        lnrbuychips = findViewById(R.id.lnrbuychips);
        lnrmail = findViewById(R.id.lnrmail);
        lnrinvite = findViewById(R.id.lnrinvite);
        lnrsetting = findViewById(R.id.lnrsetting);
        lnrvideo = findViewById(R.id.lnrvideo);


        imaprofile = findViewById(R.id.imaprofile);


        emitBubbles();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");




        FrameLayout home_container = findViewById(R.id.home_container);
        home_container.setVisibility(View.VISIBLE);


        rootView = findViewById(R.id.rlt_animation_layout);
        RelativeLayout relativeLayout = findViewById(R.id.rlt_parent);
//        SetBackgroundImageAsDisplaySize(this,relativeLayout,R.drawable.splash);


//        BonusView();



        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        helvatikaboldround = Typeface.createFromAsset(getAssets(),
                "fonts/helvetica-rounded-bold-5871d05ead8de.otf");

        helvatikanormal = Typeface.createFromAsset(getAssets(),
                "fonts/Helvetica.ttf");

        rltimageptofile = findViewById(R.id.rltimageptofile);

        rltimageptofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserInformation_BT userInformation_bt = new UserInformation_BT(new Callback() {
                    @Override
                    public void Responce(String resp, String type, Bundle bundle) {
                        UserProfile();
                    }
                });
                userInformation_bt.setCancelable(false);
                userInformation_bt.show(getSupportFragmentManager(),userInformation_bt.getTag());
            }
        });

        imgpublicGame = (ImageView) findViewById(R.id.imgpublicGame);
        imgPrivategame = (ImageView) findViewById(R.id.imgPrivategame);
        ImgCustomePage = (ImageView) findViewById(R.id.ImgCustomePage);
        iv_andher = (ImageView) findViewById(R.id.iv_andher);
        txtName = findViewById(R.id.txtName);
        txtName.setTypeface(helvatikaboldround);
        txtwallet = findViewById(R.id.txtwallet);
        txtwallet.setTypeface(helvatikanormal);
        txtproname = findViewById(R.id.txtproname);
        txtproname.setTypeface(helvatikaboldround);
        TextView txtMail = findViewById(R.id.txtMail);
        TextView txtInvite = findViewById(R.id.txtInvite);




        // load the animation
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        imgpublicGame.startAnimation(animBlink);
        imgpublicGame.startAnimation(animBounce);


        imgPrivategame.startAnimation(animBlink);
        imgPrivategame.startAnimation(animBounce);


        ImgCustomePage.startAnimation(animBlink);
        ImgCustomePage.startAnimation(animBounce);
        ImgCustomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialoagonBack();
            }
        });

        clickTask();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        // Log.d(TAG, msg);
                        // Funtions.showToast(Homepage.this, token);
                        UserProfile();
                    }
                });


        rotation_animation(((ImageView) findViewById(R.id.imgsetting)),R.anim.rotationback_animation);


        findViewById(R.id.lnr_redeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RedeemActivity.class));

//                LoadFragment(new WalletFragment());
            }
        });

        findViewById(R.id.lnrhistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(context,MyWinningAcitivity.class));
                startActivity(new Intent(context, GameDetails_A.class));

//                showRedeemDailog();
            }
        });


        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile();
            }
        });





    }

    private void BonusView(){

        if(SharePref.getInstance().getBoolean(SharePref.isBonusShow))
            lnrmail.setVisibility(View.VISIBLE);
        else
            lnrmail.setVisibility(View.GONE);

            Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String datevalue = prefs.getString("cur_date4", "12/06/2020");



        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate1 = df1.format(c);
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"), datevalue,formattedDate1);


        if (dateDifference > 0) {
            // catalog_outdated = 1;

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("cur_date4", formattedDate);
            editor.apply();

            if(SharePref.getInstance().getBoolean(SharePref.isBonusShow))
                showDailyWinCoins();

        }else {

            System.out.println("");


        }

        lnrmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getApplicationContext(), MaiUserListingActivity.class);
//                startActivity(intent);
//                Funtions.showToast(Homepage.this, "Coming Soon",
//                        Toast.LENGTH_LONG).show();
                showDailyWinCoins();

            }
        });
    }

    private void LoadFragment(Fragment fragment)
    {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.home_container,fragment).
                addToBackStack(null).
                commit();
    }

    private void BlinkAnimation(final View view) {
        view.setVisibility(View.VISIBLE);
        final Animation animationUtils = AnimationUtils.loadAnimation(context,R.anim.blink);
        animationUtils.setDuration(1000);
        animationUtils.setRepeatCount(1);
        animationUtils.setStartOffset(700);
        view.startAnimation(animationUtils);

        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void shineAnimation(final View view) {
        final Animation animationUtils = AnimationUtils.loadAnimation(context,R.anim.left_to_righ);
        animationUtils.setDuration(1500);
        view.startAnimation(animationUtils);

        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(animationUtils);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }




    LinearLayout lnr_2player,lnr_5player;
    TextView tv_2player,tv_5player;
    int selected_type = 5;
    public void Dialog_SelectPlayer() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select_palyer);
         lnr_2player = dialog.findViewById(R.id.lnr_2player);
         lnr_5player = dialog.findViewById(R.id.lnr_5player);
       tv_2player =  (TextView) dialog.findViewById(R.id.tv_2player);
       tv_5player =  (TextView) dialog.findViewById(R.id.tv_5player);

        Button btn_player = dialog.findViewById(R.id.btn_play);

        ChangeTextviewColorChange(5);

        lnr_2player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(2);
            }
        });

        lnr_5player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTextviewColorChange(5);
            }
        });

        btn_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(selected_type == 2)
                {
                    LoadTableFragments(ActiveTables_BF.RUMMY2);

                }
                else {
                    LoadTableFragments(ActiveTables_BF.RUMMY5);


                }


            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


    }

    private void LoadTableFragments(String TAG){
        ActiveTables_BF activeTables_bf = new ActiveTables_BF(TAG);
        activeTables_bf.show(getSupportFragmentManager(),activeTables_bf.getTag());
    }

    private void LoadPullRummyActiveTable(String TAG){
        RummyActiveTables_BF rummyActiveTables_bf = new RummyActiveTables_BF(TAG);
        rummyActiveTables_bf.show(getSupportFragmentManager(),rummyActiveTables_bf.getTag());
    }

    private void LoadDealRummyActiveTable(String TAG){
        RummyDealActiveTables_BF rummyDealActiveTables_bf = new RummyDealActiveTables_BF(TAG);
        rummyDealActiveTables_bf.show(getSupportFragmentManager(),rummyDealActiveTables_bf.getTag());
    }

    private void ChangeTextviewColorChange(int colortype){

        selected_type = colortype;

        if(colortype == 2)
        {
            tv_2player.setTextColor(getResources().getColor(R.color.white));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            tv_5player.setTextColor(getResources().getColor(R.color.black));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.white));

        }
        else {
            tv_2player.setTextColor(getResources().getColor(R.color.black));
            lnr_2player.setBackgroundColor(getResources().getColor(R.color.white));

            tv_5player.setTextColor(getResources().getColor(R.color.white));
            lnr_5player.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }


    }



    private void rotation_animation(View view,int animation){

        Animation circle =  Functions.AnimationListner(this, animation, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });
        view.setAnimation(circle);
        circle.startNow();

    }

    int Counts = 100;
    int postion = -100;
    private void CenterAnimationView(String imagename,View imgcards,int Home_Page_Animation){


        int AnimationSpeed = Counts + Home_Page_Animation;
        Counts += 300;

        final View fromView, toView;
        rootView.setVisibility(View.VISIBLE);
//        rootView.removeAllViews();
//        View ivpickcard = findViewById(R.id.view_center);

        fromView = rootView;
        toView = imgcards;


        int fromLoc[] = new int[2];
        fromView.getLocationOnScreen(fromLoc);
        float startX = fromLoc[0];
        float startY = fromLoc[1];

        int toLoc[] = new int[2];
        toView.getLocationOnScreen(toLoc);
        float destX = toLoc[0];
        float destY = toLoc[1];

        final ImageView sticker = new ImageView(this);

        int stickerId = Functions.GetResourcePath(imagename,this);

        int card_width = (int) getResources().getDimension(R.dimen.home_card_width);
        int card_hieght = (int) getResources().getDimension(R.dimen.home_card_height);

        Picasso.with(this).load(stickerId).into(sticker);

        sticker.setLayoutParams(new ViewGroup.LayoutParams(card_width, card_hieght));
        rootView.addView(sticker);


        Animations anim = new Animations();
        Animation a = anim.fromAtoB(0, 0, convertDpToPixel(postion,this), 0, null, AnimationSpeed, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                fromView.setVisibility(View.VISIBLE);
                toView.setVisibility(View.VISIBLE);
                sticker.setVisibility(View.GONE);
            }
        });
        sticker.setAnimation(a);
        a.startNow();

        postion += 100;

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserProfile();
        GameLeave();
    }

    public void clickTask() {

        imgPrivategame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialoag();
                float gamecount = 0;
                if(game_played != null && !game_played.equals(""))
                {
                   gamecount  = Float.parseFloat(game_played);
                }
                float game_for_privatetemp = Float.parseFloat(game_for_private);
                if (gamecount > game_for_privatetemp ){

                    showDialoagPrivettable();

                }else {

                    Functions.showToast(Homepage.this, "To Unblock Private Table you have to Play at least "+game_for_privatetemp+
                                    " Games.");

                }

            }
        });

        lnrsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogSettingOption dialogSettingOption = new DialogSettingOption(context)
                {
                    public void playstopSound() {

                        playSound(R.raw.game_soundtrack,true);

                    }
                };

                dialogSettingOption.showDialogSetting();
            }
        });

        lnrvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.showToast(Homepage.this, "Coming Soon");
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new AlertDialog.Builder(Homepage.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Logout")
//                        .setMessage("Do you want to Logout?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                                editor.putString("user_id", "");
//                                editor.putString("name", "");
//                                editor.putString("mobile", "");
//                                editor.putString("token", "");
//                                editor.apply();
//                                Intent intent = new Intent(Homepage.this, LoginScreen.class);
//                                startActivity(intent);
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();


                new SmartDialogBuilder(Homepage.this)
                        .setTitle("Do you want to Logout?")
                        //.setSubTitle("This is the alert dialog to showing alert to user")
                        .setCancalable(true)
                        //.setTitleFont("Do you want to Logout?") //set title font
                       // .setSubTitleFont(subTitleFont) //set sub title font
                        .setNegativeButtonHide(true) //hide cancel button
                        .setPositiveButton("Logout", new SmartDialogClickListener() {
                            @Override
                            public void onClick(SmartDialog smartDialog) {
                               // Funtions.showToast(context,"Ok button Click",Toast.LENGTH_SHORT)
                                // .show();
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", "");
                                editor.putString("name", "");
                                editor.putString("mobile", "");
                                editor.putString("token", "");
                                editor.apply();
                                Intent intent = new Intent(Homepage.this, LoginScreen.class);
                                startActivity(intent);
                                finish();

                                smartDialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                       // Funtions.showToast(context,"Cancel button Click");
                        smartDialog.dismiss();

                    }
                }).build().show();

//                new SweetAlertDialog(Homepage.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Are you sure?")
//                        .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("Yes,delete it!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
            }
        });

        lnrinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showDialogInvite();

            }
        });
    }

    public void openBuyChipsActivity(View view){
        Intent intent = new Intent(Homepage.this, BuyChipsList.class);
        intent.putExtra("homepage","homepage");
        startActivity(intent);
    }



    private void UserProfile() {

        Functions.showLoader(context,false,false);

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("id", prefs.getString("user_id", ""));
        params.put("fcm", token);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        params.put("app_version", version + "");
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.PROFILE, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp != null)
                {
                    ParseResponse(resp);
                }

            }
        });

    }

    private void ParseResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            if (code.equalsIgnoreCase("200")) {
                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                user_id = jsonObject0.getString("id");
                name = jsonObject0.optString("name");
                mobile = jsonObject0.optString("mobile");
                profile_pic = jsonObject0.optString("profile_pic");
                referral_code = jsonObject0.optString("referral_code");
                wallet = jsonObject0.optString("wallet");
               String winning_wallet = jsonObject0.optString("winning_wallet");
                game_played = jsonObject0.optString("game_played");
                bank_detail = jsonObject0.optString("bank_detail");
                upi = jsonObject0.optString("upi");
                adhar_card = jsonObject0.optString("adhar_card");

                // txtName.setText("Welcome Back "+name);

                if(Functions.checkStringisValid(wallet))
                {
                    float f_wallet = Float.parseFloat(wallet);
//                    long numberamount =  Float.parseFloat(f_wallet);
//                    long numberamount = (long) f_wallet;
//                    txtwallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(f_wallet));
                    txtwallet.setText("" + f_wallet);
                }


                txtproname.setText(name);
                Picasso.with(Homepage.this).load(Const.IMGAE_PATH + profile_pic).into(imaprofile);


                String setting = jsonObject.optString("setting");
                JSONObject jsonObjectSetting = new JSONObject(setting);
                game_for_private = jsonObjectSetting.optString("game_for_private");
                app_version = jsonObjectSetting.optString("app_version");
                String  referral_amount = jsonObjectSetting.optString("referral_amount");
                String  referral_link = jsonObjectSetting.optString("referral_link");
                String  joining_amount = jsonObjectSetting.optString("joining_amount");
                 String whats_no = jsonObjectSetting.optString("whats_no");

                 // bonus = 0=no , 1=yes
                 // payment_gateway = 0=payment , 1=whatsapp
                 // symbol = 0=coin , 1=rupees,2 = dollar

                 String symbol = jsonObjectSetting.optString("symbol");
                 String payment_gateway = jsonObjectSetting.optString("payment_gateway");
                 String bonus = jsonObjectSetting.optString("bonus");
                String razor_api_key = jsonObjectSetting.optString("razor_api_key");

                String cashfree_client_id = jsonObjectSetting.optString("cashfree_client_id");
                String cashfree_stage = jsonObjectSetting.optString("cashfree_stage");
                String paytm_mercent_id = jsonObjectSetting.optString("paytm_mercent_id");
                String payumoney_key = jsonObjectSetting.optString("payumoney_key");

                String share_text = jsonObjectSetting.optString("share_text");

                if(Functions.checkStringisValid(bonus) && !bonus.equalsIgnoreCase("0"))
                    SharePref.getInstance().putBoolean(SharePref.isBonusShow,true);
                else
                    SharePref.getInstance().putBoolean(SharePref.isBonusShow,false);

                if(Functions.checkStringisValid(payment_gateway)
                        && !payment_gateway.equalsIgnoreCase("0")
                        && !payment_gateway.equalsIgnoreCase("2")
                        && !payment_gateway.equalsIgnoreCase("3"))
                    SharePref.getInstance().putBoolean(SharePref.isPaymentGateShow,false);
                else
                    SharePref.getInstance().putBoolean(SharePref.isPaymentGateShow,true);



                String payment_type= payment_gateway.equals("0") ? Variables.RAZOR_PAY
                        : payment_gateway.equals("1") ? Variables.WHATS_APP
                        : payment_gateway.equals("2") ? Variables.CASH_FREE
                        :  Variables.PAYTM;

                SharePref.getInstance().putString(SharePref.PaymentType,payment_type);



                if(Functions.checkStringisValid(symbol) && symbol.equalsIgnoreCase("0"))
                    Variables.CURRENCY_SYMBOL = Variables.COINS;
                else if(Functions.checkStringisValid(symbol) && symbol.equalsIgnoreCase("1"))
                {
                    Variables.CURRENCY_SYMBOL = Variables.RUPEES;
                }
                else if(Functions.checkStringisValid(symbol) && symbol.equalsIgnoreCase("2"))
                {
                    Variables.CURRENCY_SYMBOL = Variables.DOLLAR;
                }



                BonusView();

                SharePref.getInstance().putString(SharePref.SYMBOL_TYPE,Variables.CURRENCY_SYMBOL);
                SharePref.getInstance().putString(SharePref.RAZOR_PAY_KEY,razor_api_key);
                SharePref.getInstance().putString(SharePref.CASHFREE_CLIENT_ID,cashfree_client_id);
                SharePref.getInstance().putString(SharePref.CASHFREE_STAGE,cashfree_stage);
                SharePref.getInstance().putString(SharePref.PAYTM_MERCENT_ID,paytm_mercent_id);
                SharePref.getInstance().putString(SharePref.PAYU_MERCENT_KEY,payumoney_key);
                SharePref.getInstance().putString(SharePref.referral_link,referral_link);

                SharePref.getInstance().putString(SharePref.SHARE_APP_TEXT,share_text);

                ((ImageView) findViewById(R.id.imgicon)).setImageDrawable(
                        Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.RUPEES) ? Functions.getDrawable(context,R.drawable.ic_currency_rupee) :
                        Variables.CURRENCY_SYMBOL.equalsIgnoreCase(Variables.DOLLAR) ? Functions.getDrawable(context,R.drawable.ic_currency_dollar) :
                                Functions.getDrawable(context,R.drawable.ic_currency_chip));

                try {

//                    int app_versionint = Integer.parseInt(app_version);
//
//                    //if (version > app_versionint){
//                if (app_versionint > version) {
//
//                    showAppUpdateDialog("Update");
//
//                } else {
//
//
//                }
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }





                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("profile_pic", profile_pic);
                editor.putString("bank_detail", bank_detail);
                editor.putString("upi", upi);
                editor.putString("adhar_card", adhar_card);
                editor.putString("mobile", mobile);
                editor.putString("referal_code", referral_code);
                editor.putString("img_name", profile_pic);
                editor.putString("winning_wallet", winning_wallet);
                editor.putString("wallet", wallet);
                editor.putString("game_for_private", game_for_private);
                editor.putString("app_version", app_version);
                editor.putString("whats_no", whats_no);
                editor.putString(REFERRAL_AMOUNT, referral_amount);
                editor.putString(JOINING_AMOUNT, joining_amount);
                editor.apply();


            } else if (code.equals("411")) {

                Intent intent = new Intent(Homepage.this, LoginScreen.class);
                startActivity(intent);
                finishAffinity();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", "");
                editor.putString("referal_code", "");
                editor.putString("img_name", "");
                editor.putString("game_for_private", "");
                editor.putString("app_version", "");
                editor.apply();

                Functions.showToast(Homepage.this, "You are Logged in from another " +
                                "device.");


            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(Homepage.this, message);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.dismissLoader();

    }

    public void PlaySaund(int sound) {

        final MediaPlayer mp = MediaPlayer.create(this,
                sound);
        mp.start();
    }

    public void showDialoagPrivettable() {

        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBarpri = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBarpri.setProgress(0);
        sBarpri.incrementProgressBy(10);
        sBarpri.setMax(100);
        txtStartpri = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimitpri = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchipspri = (TextView) dialog.findViewById(R.id.txtwalletchips);
        float numberamount = Float.parseFloat(wallet);
        txtwalletchipspri.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));

        // txtwalletchipspri.setText(wallet);
        txtBootamountpri = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimitpri = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlindpri = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvaluepri = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        imgclosetoppri = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetoppri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetable = (ImageView) dialog.findViewById(R.id.imgCreatetable);
        imgCreatetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Homepage.this, PrivateTablev2.class);
                Intent intent = new Intent(Homepage.this, PublicTable_New.class);
                intent.putExtra("gametype", PublicTable_New.PRIVATE_TABLE);
                intent.putExtra("bootvalue", pvalpri + "");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBarpri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                if (progress == 1) {

                    pvalpri = 50;

                } else if (progress == 2) {
                    pvalpri = 100;
                } else if (progress == 3) {

                    pvalpri = 500;
                } else if (progress == 4) {

                    pvalpri = 1000;

                } else if (progress == 5) {

                    pvalpri = 5000;

                } else if (progress == 6) {

                    pvalpri = 10000;
                } else if (progress == 7) {

                    pvalpri = 25000;
                } else if (progress == 8) {

                    pvalpri = 50000;
                } else if (progress == 9) {

                    pvalpri = 100000;
                } else if (progress == 10) {

                    pvalpri = 250000;
                }

                //progress = progress * 10;
                // pvalpri = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamountpri.setText("Boot amount : " + kvalue(pvalpri) + "");

                int valueforpot = pvalpri * 1024;
                int valueformaxi = pvalpri * 128;

                //long valueforpotlong= valueforpot;

                txtPotLimitpri.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvaluepri.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlindpri.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
    }

    public void showDialoagonBack() {

        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_custon_boot);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sBar = (SeekBar) dialog.findViewById(R.id.seekBar1);
        sBar.setProgress(0);
        sBar.incrementProgressBy(10);
        sBar.setMax(100);
        txtStart = (TextView) dialog.findViewById(R.id.txtStart);
        txtLimit = (TextView) dialog.findViewById(R.id.txtLimit);
        txtwalletchips = (TextView) dialog.findViewById(R.id.txtwalletchips);
        float numberamount = Float.parseFloat(wallet);
        txtwalletchips.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
        txtBootamount = (TextView) dialog.findViewById(R.id.txtBootamount);
        txtPotLimit = (TextView) dialog.findViewById(R.id.txtPotLimit);
        txtNumberofBlind = (TextView) dialog.findViewById(R.id.txtNumberofBlind);
        txtMaximumbetvalue = (TextView) dialog.findViewById(R.id.txtMaximumbetvalue);
        imgclosetop = (ImageView) dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imgCreatetablecustom = (ImageView) dialog.findViewById(R.id.imgCreatetable);
        imgCreatetablecustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Homepage.this, CustomisedTablev2.class);
                Intent intent = new Intent(Homepage.this, PublicTable_New.class);
                intent.putExtra("gametype", PublicTable_New.CUSTOME_TABLE);
                intent.putExtra("bootvalue", pval + "");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        // tView.setText(sBar.getProgress() + "/" + sBar.getMax());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                progress = progress / 10;
                if (progress == 1) {

                    pval = 50;

                } else if (progress == 2) {
                    pval = 100;
                } else if (progress == 3) {

                    pval = 500;
                } else if (progress == 4) {

                    pval = 1000;

                } else if (progress == 5) {

                    pval = 5000;

                } else if (progress == 6) {

                    pval = 10000;
                } else if (progress == 7) {

                    pval = 25000;
                } else if (progress == 8) {

                    pval = 50000;
                } else if (progress == 9) {

                    pval = 100000;
                } else if (progress == 10) {

                    pval = 250000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtBootamount.setText("Boot amount : " + kvalue(pval) + "");

                int valueforpot = pval * 1024;
                int valueformaxi = pval * 128;
                txtPotLimit.setText("Pot limit : " + kvalue(valueforpot) + "");
                txtMaximumbetvalue.setText("Maximumbet balue : " + kvalue(valueformaxi) + "");
                txtNumberofBlind.setText("Number of Blinds : 4");
                //tView.setText(pval + "/" + seekBar.getMax());
            }
        });


        dialog.show();
    }

    public void showRedeemDailog() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txttitle = dialog.findViewById(R.id.txtheader);

        txttitle.setText("Redeem History");

        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
        recyclerView_gifts.setLayoutManager(new LinearLayoutManager(Homepage.this));

        UsersRedeemList(recyclerView_gifts, dialog);

        dialog.show();
    }

    private void UsersRedeemList(final RecyclerView recyclerView, final Dialog dialog) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<RedeemModel> redeemModelArrayList = new ArrayList();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_Redeem_History_LIST,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
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


                                    redeemModelArrayList.add(model);
                                }

                                UserRedeemHistoryAdapter userWinnerAdapter = new UserRedeemHistoryAdapter(Homepage.this, redeemModelArrayList);
                                recyclerView.setAdapter(userWinnerAdapter);
                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(Homepage.this, message);
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(Homepage.this, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

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


    private void GameLeave() {

//        final ProgressDialog progressDialog = new ProgressDialog(Homepage.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Logging in..");
//        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GAME_TABLE_LEAVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        System.out.println("" + response);
                        // finish();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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


    public void showDialogInvite() {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog_invite);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclosetop);
        TextView txtshare = (TextView) dialog.findViewById(R.id.txtshare);

        TextView txtchipsbelow = (TextView) dialog.findViewById(R.id.txtchipsbelow);
        TextView tvInviteCoins = (TextView) dialog.findViewById(R.id.tvInviteCoins);

        tvInviteCoins.setText(Variables.CURRENCY_SYMBOL+SharePref.getInstance().getString(SharePref.referral_amount));
        txtchipsbelow.setText(Variables.CURRENCY_SYMBOL+SharePref.getInstance().getString(SharePref.referral_amount));

        TextView txtReferalcode = (TextView) dialog.findViewById(R.id.txtReferalcode);
        txtReferalcode.setText(referral_code);
        TextView txtAnd = (TextView) dialog.findViewById(R.id.txtAnd);
        ImageView imgfb = (ImageView) dialog.findViewById(R.id.imgfb);
        ImageView imgwhats = (ImageView) dialog.findViewById(R.id.imgwhats);
        ImageView imgmail = (ImageView) dialog.findViewById(R.id.imgmail);
        imgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.google.android.gm");



                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });

        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.facebook.katana");

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, getReferralMessage());

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });
        TextView txtyourfrind = (TextView) dialog.findViewById(R.id.txtyourfrind);
        TextView txtFooter = (TextView) dialog.findViewById(R.id.txtFooter);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }

    private String getReferralMessage() {
        final String appPackageName = getPackageName();
        String applink = Variables.invite_link;

        if(applink.contains("google"))
            applink = applink + appPackageName;

        String refferal_link = SharePref.getInstance().getString(SharePref.referral_link);
        if(Functions.checkisStringValid(refferal_link))
            applink = refferal_link;

        String shareMessage = getString(R.string.share_message);

        String apiMessgae = SharePref.getInstance().getString(SharePref.SHARE_APP_TEXT);

//        if(Functions.checkisStringValid(apiMessgae))
//            shareMessage  = apiMessgae;

        String referral_message = shareMessage+ " Use the referral code    " +
                referral_code + " Download the App now. Link:-" +applink;

        return referral_message;
    }


    private void AddViewToLanguage(ViewGroup viewGroup,String text){

        View view = Functions.CreateDynamicViews(R.layout.item_language,viewGroup,context);

        TextView textView = view.findViewById(R.id.tv_language);
        textView.setText(""+text);
        textView.setTag(text);


    }

    public void openLuckJackpotActivity(View view) {
        startActivity(new Intent(this, LuckJackPot_A.class));
    }

    public void openSevenup(View view) {
        startActivity(new Intent(this, Sevenup_A.class));
    }

    public void openPublicTeenpatti(View view) {
        PlaySaund(R.raw.buttontouchsound);
        LoadTableFragments(ActiveTables_BF.TEENPATTI);
    }

    public void openRummyGame(View view) {
        Dialog_SelectPlayer();
    }

    public void openRummyPullGame(View view) {
        LoadPullRummyActiveTable(ActiveTables_BF.RUMMY5);
    }

    public void openRummyDealGame(View view) {
        LoadDealRummyActiveTable(ActiveTables_BF.RUMMY5);
    }

    public void openAndharbahar(View view) {
        startActivity(new Intent(context, AndharBahar_Home_A.class));
    }

    public void openDragonTiger(View view) {
        startActivity(new Intent(context, DragonTiger_A.class));
    }

    public interface itemClick{
        void OnDailyClick(String id);
    }
    TextView txtnotfound;


    public void showDailyWinCoins() {

        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_dailyreward);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RecyclerView Reward_rec;

        txtnotfound = dialog.findViewById(R.id.txtnotfound);

        RelativeLayout relativeLayout = dialog.findViewById(R.id.rlt_parent);
        SetBackgroundImageAsDisplaySize(this,relativeLayout,R.drawable.bghome);


        imgclosetop = dialog.findViewById(R.id.imgclosetop);
        imgclosetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View)dialog.findViewById(R.id.btnCollect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectWelcomeBonus(dialog);

            }
        });


        Reward_rec = dialog.findViewById(R.id.reward_rec);

        itemClick itemClick = new itemClick() {
            @Override
            public void OnDailyClick(String id) {

//                CollectWelcomeBonus();
            }
        };

        ArrayList<Integer> CoinsList = new ArrayList<>();
        CoinsList.add(R.drawable.day1);
        CoinsList.add(R.drawable.day2);
        CoinsList.add(R.drawable.day3);
        CoinsList.add(R.drawable.day4);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);
        CoinsList.add(R.drawable.day5);

        GetUserWelcomeBonus(Reward_rec,itemClick,dialog,CoinsList);

        Reward_rec.setLayoutManager(new LinearLayoutManager(Homepage.this,RecyclerView.HORIZONTAL,false));



        dialog.show();
    }


    public void showAppUpdateDialog(String tag) {
        // custom dialog
        final Dialog dialog = new Dialog(Homepage.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_app_update);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
//        ((ImageView)dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });


        TextView txtheader = dialog.findViewById(R.id.txtheader);
        RelativeLayout rltUpdate = dialog.findViewById(R.id.rltUpdate);
        txtheader.setText(""+tag);
        rltUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void GetUserWelcomeBonus(final RecyclerView reward_rec, final itemClick itemClick, Dialog dialog, final ArrayList<Integer> coinsList) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<WelcomeModel> welcomeList = new ArrayList();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.WELCOME_BONUS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {

                                txtnotfound.setVisibility(View.GONE);

                                JSONArray welcome_bonusArray = jsonObject.getJSONArray("welcome_bonus");

                                for (int i = 0; i < welcome_bonusArray.length() ; i++) {
                                    JSONObject welcome_bonusObject = welcome_bonusArray.getJSONObject(i);

                                    WelcomeModel model= new WelcomeModel();
                                    model.setCoins(welcome_bonusObject.getString("coin"));
                                    model.setId(welcome_bonusObject.getString("id"));
                                    model.setGame_played(welcome_bonusObject.getString("game_played"));
                                    model.setCollected_days(jsonObject.getString("collected_days"));
                                    model.setDay(welcome_bonusObject.getString("id"));
                                    model.setImgcoins(coinsList.get(i));

                                    welcomeList.add(model);
                                }

                                WelcomeRewardAdapter  welcomeRewardAdapter = new WelcomeRewardAdapter(Homepage.this,welcomeList,itemClick);
                                reward_rec.setAdapter(welcomeRewardAdapter);

                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(Homepage.this, message);
                                }

                                txtnotfound.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(Homepage.this, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);


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


    private void CollectWelcomeBonus(Dialog dialog) {

        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.COLLECT_BONUS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            String coins = "0";
                            if (jsonObject.has("coin"))
                                coins  = jsonObject.getString("coin");

                            if (code.equalsIgnoreCase("200")) {
                                dialog.dismiss();
                                UserProfile();
                                WelcomeRewardAdapter.showWinDialog(Homepage.this,coins);

                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(Homepage.this, message);


                                }


                            }

                            rlt_progress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rlt_progress.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(Homepage.this, "Something went wrong");
                rlt_progress.setVisibility(View.GONE);

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

    private void UserTermsAndCondition(final WebView webview, final Dialog dialog, final String tag) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.TERMS_CONDITION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");


                            if (code.equalsIgnoreCase("200")) {


                                JSONObject jsonObject1 = jsonObject.getJSONObject("setting");

                                String data = "";

                                if(tag.equals("Privacy Policy"))
                                {
                                    data = jsonObject1.getString("privacy_policy");
                                }
                                else {
                                    data = jsonObject1.getString("terms");
                                }


                                if(data.equals(""))
                                {
                                    txtnotfound.setVisibility(View.VISIBLE);
                                }
                                else {
                                    txtnotfound.setVisibility(View.GONE);
                                }


                                data = data.replaceAll("&#39;","'");

                                String szMessage = "<font face= \"trebuchet\" size=3 color=\"#fff\"><b>"
                                        + data
                                        + "</b></font>";


                                webview.getSettings().setJavaScriptEnabled(true);
                                webview.loadDataWithBaseURL("",szMessage, "text/html", "UTF-8","");


                            } else {
                                if (jsonObject.has("message")) {

                                    Functions.showToast(Homepage.this, message);
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Functions.showToast(Homepage.this, "Something went wrong");
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

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


    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public String kvalue (int number){

        String numberString = "";
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000) + "m";

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000) + "k";

        } else {
            numberString = number+"";

        }
        return numberString;
    }

    @Override
    protected void onStart() {
        super.onStart();

        playSound(R.raw.game_soundtrack,true);
    }

    @Override
    protected void onDestroy() {
        stopPlaying();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlaying();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlaying();
    }

    Sound soundMedia;
    public void playSound(int sound,boolean isloop) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String value = prefs.getString("issoundon", "1");

        if (value.equals("1")) {
            soundMedia.PlaySong(sound,isloop,context);
//            playSound(SoundPoolModel.SOUND_1);
        }
        else {
            stopPlaying();
        }


    }

    private void stopPlaying() {
        soundMedia.StopSong();
//        stopSound(SoundPoolModel.SOUND_1);
    }


    public static final int SOUND_1 = 1;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;
    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        mSoundMap = new HashMap<Integer, Integer>();

        if(mSoundPool != null){
            mSoundMap.put(SOUND_1, mSoundPool.load(this, R.raw.game_soundtrack, 1));
        }
    }
    /*
     *Call this function from code with the sound you want e.g. playSound(SOUND_1);
     */
    public void playSound(int sound) {
        AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, -1, 1.0f);
        }
    }

    public void stopSound(int sound){
        mSoundPool.stop(sound);
    }

}


