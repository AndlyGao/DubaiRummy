package com.gamegards.dubairummy.Comman;

import static android.content.Context.MODE_PRIVATE;
import static com.gamegards.dubairummy.Activity.Homepage.MY_PREFS_NAME;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.gamegards.dubairummy.Activity.LoginScreen;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;
import com.gamegards.dubairummy.Utils.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommonAPI {

    public static void CALL_API_UserDetails(Activity context,Callback callback){

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("id", prefs.getString("user_id", ""));
//        params.put("fcm", token);
        int version = 0;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        params.put("app_version", version + "");
        params.put("token", prefs.getString("token", ""));

        ApiRequest.Call_Api(context, Const.PROFILE, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {
                callback.Responce(resp,null,null);
                if(resp != null)
                {
                    ParseResponse(resp,context);
                }

            }
        });

    }

    private static void ParseResponse(String response, Activity context){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String code = jsonObject.getString("code");
            if (code.equalsIgnoreCase("200")) {
                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
                String user_id = jsonObject0.getString("id");
                String name = jsonObject0.optString("name");
                String mobile = jsonObject0.optString("mobile");
                String profile_pic = jsonObject0.optString("profile_pic");
                String referral_code = jsonObject0.optString("referral_code");
                String wallet = jsonObject0.optString("wallet");
                String game_played = jsonObject0.optString("game_played");
                String bank_detail = jsonObject0.optString("bank_detail");
                String upi = jsonObject0.optString("upi");
                String adhar_card = jsonObject0.optString("adhar_card");
                String winning_wallet = jsonObject0.optString("winning_wallet");


                String setting = jsonObject.optString("setting");
                JSONObject jsonObjectSetting = new JSONObject(setting);
               String game_for_private = jsonObjectSetting.optString("game_for_private");
                String app_version = jsonObjectSetting.optString("app_version");
                String  referral_amount = jsonObjectSetting.optString("referral_amount");
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


                SharePref.getInstance().putString(SharePref.SYMBOL_TYPE,Variables.CURRENCY_SYMBOL);
                SharePref.getInstance().putString(SharePref.RAZOR_PAY_KEY,razor_api_key);

                SharePref.getInstance().putString(SharePref.CASHFREE_CLIENT_ID,cashfree_client_id);
                SharePref.getInstance().putString(SharePref.CASHFREE_STAGE,cashfree_stage);
                SharePref.getInstance().putString(SharePref.PAYTM_MERCENT_ID,paytm_mercent_id);
                SharePref.getInstance().putString(SharePref.PAYU_MERCENT_KEY,payumoney_key);

                SharePref.getInstance().putString(SharePref.SHARE_APP_TEXT,share_text);



                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
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
                String REFERRAL_AMOUNT = "referral_amount";
                String JOINING_AMOUNT = "joining_amount";
                editor.putString(REFERRAL_AMOUNT, referral_amount);
                editor.putString(JOINING_AMOUNT, joining_amount);
                editor.apply();


            } else if (code.equals("411")) {

                Intent intent = new Intent(context, LoginScreen.class);
                context.startActivity(intent);
                context.finishAffinity();
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", "");
                editor.putString("referal_code", "");
                editor.putString("img_name", "");
                editor.putString("game_for_private", "");
                editor.putString("app_version", "");
                editor.apply();

                Functions.showToast(context, "You are Logged in from another " +
                                "device.");


            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(context, message);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.dismissLoader();


    }


}
