package com.gamegards.dubairummy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamegards.dubairummy.Comman.CommonAPI;
import com.gamegards.dubairummy.Comman.DialogSelectPaymentType;

import com.gamegards.dubairummy.Comman.PaymentGetway_CashFree;
import com.gamegards.dubairummy.Comman.PaymentGetway_Paymt;

import com.gamegards.dubairummy.Comman.PaymentGetway_PayuMoney;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;
import com.gamegards.dubairummy.Utils.Variables;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;
import com.rahman.dialog.Utilities.SmartDialogBuilder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class BuyChipsDetails extends AppCompatActivity implements PaymentResultListener {


    Activity context = this;
    private static final String MY_PREFS_NAME = "Login_data" ;
    Button btnPaynow;
    TextView txtChipsdetails;
    String plan_id = "";
    String chips_details = "";
    String amount = "";
    String RazorPay_ID = "";
    private String order_id;
    ImageView imgback,imgPaynow;
    String county_code = "+91 ";
    String whats_no = "";

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

    String selected_payment = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chips_details);


        Intent intent = getIntent();
        plan_id = intent.getStringExtra("plan_id");
        chips_details = intent.getStringExtra("chips_details");
        amount = intent.getStringExtra("amount");

        imgPaynow = findViewById(R.id.imgPaynow);
        txtChipsdetails = findViewById(R.id.txtChipsdetails);
        txtChipsdetails.setText("Buy "+chips_details+" Pay now "+ Variables.CURRENCY_SYMBOL+amount);


        PaymentGateWayInit();


        imgPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                if(SharePref.getInstance().getBoolean(SharePref.isPaymentGateShow,true))
                {


                    DialogSelectPaymentType dialogSelectPaymentType=new DialogSelectPaymentType(context, new Callback() {
                        @Override
                        public void Responce(String resp, String type, Bundle bundle) {


                        }
                    });

//                    dialogSelectPaymentType.showSelectPaymentType();


//                    paymentGetway_payuMoney.startPayment(plan_id);
//
//                    if(true)
//                        return;


                    selected_payment = SharePref.getInstance().getString(SharePref.PaymentType);
                    if(selected_payment.equalsIgnoreCase(Variables.RAZOR_PAY))
                    {
                        place_order();
                    }
                    else if(selected_payment.equalsIgnoreCase(Variables.PAYTM))
                    {
//                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                _paymentGetway_paymt.startPayment(plan_id,amount);
                            }
                        },1000);
                    }
                    else {
                        _paymentGetwayCashFree.startPayment(plan_id);
                    }


                }
                else {
                    whats_no = prefs.getString("whats_no","");

                    Functions.showToast(BuyChipsDetails.this, ""+whats_no);

                    if (!whats_no.equals(""))
                        Functions.openWhatsappContact(BuyChipsDetails.this,county_code+whats_no);
                }



//
            }
        });
        imgback = findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        CommonAPI.CALL_API_UserDetails(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

            }
        });

    }

    private void PaymentGateWayInit() {
        _paymentGetway_paymt = new PaymentGetway_Paymt(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equalsIgnoreCase(Variables.SUCCESS))
                {
                    dialog_payment_success();
                }
                else {

                }

            }
        });

        _paymentGetwayCashFree = new PaymentGetway_CashFree(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equalsIgnoreCase(Variables.SUCCESS))
                {
                    dialog_payment_success();
                }
                else {

                }

            }
        });

        paymentGetway_payuMoney = new PaymentGetway_PayuMoney(context, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                if(resp.equalsIgnoreCase(Variables.SUCCESS))
                {
                    dialog_payment_success();
                }
                else {

                }

            }
        });
    }


    public void place_order(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Const.PLCE_ORDER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String code=jsonObject.getString("code");
                            String message=jsonObject.getString("message");

                            if (code.equals("200")){

                                order_id=jsonObject.getString("order_id");
                                String Total_Amount=jsonObject.getString("Total_Amount");
                                RazorPay_ID=jsonObject.getString("RazorPay_ID");
                                startPayment(order_id,Total_Amount,RazorPay_ID);
                            }
                            else  if (code.equals("404")) {
                                Functions.showToast(BuyChipsDetails.this, ""+message);
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header= new HashMap<>();
                header.put("token",Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params= new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("plan_id", plan_id);
                Functions.LOGE("BuyChipsDetails",Const.PLCE_ORDER+"\n"+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    PaymentGetway_CashFree _paymentGetwayCashFree;
    PaymentGetway_PayuMoney paymentGetway_payuMoney;
    PaymentGetway_Paymt _paymentGetway_paymt;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(selected_payment.equalsIgnoreCase(Variables.CASH_FREE))
        {
            if(_paymentGetwayCashFree != null)
                _paymentGetwayCashFree.onActivityResult(requestCode, resultCode, data);

        }
        else if(selected_payment.equalsIgnoreCase(Variables.PAYTM))
        {
            if(_paymentGetway_paymt != null)
                _paymentGetway_paymt.onActivityResult(requestCode, resultCode, data);

        }

        if(paymentGetway_payuMoney != null)
            paymentGetway_payuMoney.onActivityResult(requestCode, resultCode, data);


    }


    public void startPayment( String ticket_id, String total_Amount, String razorPay_ID) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        String key = SharePref.getInstance().getString(SharePref.RAZOR_PAY_KEY);

        if(Functions.checkisStringValid(key))
        {
            co.setKeyID(key);
        }

        try {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

            JSONObject options = new JSONObject();
            options.put("name",  prefs.getString("name", ""));
            options.put("description", "chips payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", total_Amount);
            options.put("order_id", razorPay_ID);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "support@androappstech.com");
            preFill.put("contact",  prefs.getString("mobile", ""));
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Functions.showToast(activity, "Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payNow(razorpayPaymentID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            //Funtions.showToast(this, "Payment failed: " + code + " " + response, Toast
            // .LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


    public void payNow(final String payment_id){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Const.PY_NOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            ParseSuccessPayment(jsonObject);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // NoInternet(listTicket.this);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header= new HashMap<>();
                header.put("token",Const.TOKEN);

                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params= new HashMap<>();
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("token", prefs.getString("token", ""));
                params.put("order_id", order_id);
                params.put("payment_id", payment_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BuyChipsDetails.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void ParseSuccessPayment(JSONObject jsonObject) throws JSONException {


        String code=jsonObject.getString("code");
        String message=jsonObject.getString("message");

        if (code.equals("200")){
            Functions.showToast(BuyChipsDetails.this, ""+message);
            dialog_payment_success();
        }
        else  if (code.equals("404")) {
            Functions.showToast(BuyChipsDetails.this, ""+message);
        }

    }

    private void dialog_payment_success(){

        new SmartDialogBuilder(BuyChipsDetails.this)
                .setTitle("Your Payment has been done Successfully!")
                .setSubTitle("Your Payment has been done Successfully!")
                .setCancalable(false)

                //.setTitleFont("Do you want to Logout?") //set title font
                // .setSubTitleFont(subTitleFont) //set sub title font
                .setNegativeButtonHide(true) //hide cancel button
                .setPositiveButton("Ok", new SmartDialogClickListener() {
                    @Override
                    public void onClick(SmartDialog smartDialog) {
                        smartDialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("Cancel", new SmartDialogClickListener() {
            @Override
            public void onClick(SmartDialog smartDialog) {
                // Funtions.showToast(context,"Cancel button Click");
                smartDialog.dismiss();

            }
        }).build().show();



//        final Dialog dialog = new Dialog(BuyChipsDetails.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle("");
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.dialog_payment_successful);
//        final Button btn_payNow = (Button) dialog.findViewById(R.id.btn_payNow);
//
//        btn_payNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//                //Ticketlist();
//            }
//        });
//
//        dialog.setCancelable(false);
//        dialog.show();

    }

}
