package com.gamegards.dubairummy.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gamegards.dubairummy.BaseActivity;
import com.gamegards.dubairummy.SampleClasses.CommonFunctions;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;


public class LoginScreen extends BaseActivity {

    private static final String MY_PREFS_NAME = "Login_data";
    EditText edtPhone, edtname, edtReferalCode;
    TextView tv;
    int pStatus = 0;
    private Handler handler = new Handler();
    View imglogin;
    AlertDialog dialog;
    EditText edit_OTP;
    String verificationID;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    boolean isSelected = false;
    RadioButton genderradioButton;
    ImageView imgBackground, imgBackgroundlogin;
    Context context = LoginScreen.this;
    public BottomSheetBehavior sheetBehavior;
    public View bottom_sheet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);


        imgBackground = findViewById(R.id.imgBackground);
        imgBackgroundlogin = findViewById(R.id.imgBackgroundlogin);

//        Picasso.with(context).load(imageResource1).into(imgBackground);
        // Picasso.with(context).load(imageResource2).into(imgBackgroundlogin);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.edtPhone);
        edtname = findViewById(R.id.edtname);
        edtReferalCode = findViewById(R.id.edtReferalCode);
        imglogin = findViewById(R.id.imglogin);
        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidate()) {
                    if (CommonFunctions.isNetworkConnected(LoginScreen.this)) {
                        //phoneLogin();
                        // Funtions.showToast(LoginScreen.this,genderradioButton.getText(),
                        //Toast.LENGTH_SHORT).show();
                        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        // Funtions.showToast(LoginScreen.this, rb.getText());
                        if (isSelected) {

                            login(rb.getText() + "");

                        } else {

                            Functions.showToast(LoginScreen.this, "Please select Gender first ?");

                        }


                    } else {
                        CommonFunctions.showNoInternetDialog(LoginScreen.this);
                    }


                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    isSelected = true;
                    //Funtions.showToast(LoginScreen.this, rb.getText());
                }

            }
        });

        SocialLogin();

        test();

        initBottomDialog();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharePref.getIsLoginWithPassword())
            startActivity(new Intent(this, LoginWithPasswordScreen_A.class));
    }

    String reffer_code = "";
    RadioGroup bottomradioGroup;
    private void initBottomDialog() {
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

         bottomradioGroup = findViewById(R.id.bottomradioGroup);
         sheetBehavior.setDraggable(false);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btn_bottom_sheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        findViewById(R.id.bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });



        final EditText edt_reffer = findViewById(R.id.edt_reffer);

        findViewById(R.id.btnrefer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = bottomradioGroup.getCheckedRadioButtonId();
               RadioButton radioButton = (RadioButton) findViewById(selectedId);


                if(!edt_reffer.getText().toString().trim().equals(""))
                {
                    reffer_code = edt_reffer.getText().toString().trim();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                }
                else {

                    reffer_code = "";
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                CALL_API_TO_VERIFY_SOCIELLOGIN(social_id,social_name,social_email, radioButton.getText().toString().trim());

            }
        });

    }



    private void test(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                Funtions.showToast(this, ""+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

    CallbackManager callbackManager ;
    private void SocialLogin() {

        FacebookSdk.sdkInitialize(this);

        callbackManager = CallbackManager.Factory.create();

        initFacebook();

        findViewById(R.id.imgfacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_type = 2;

                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LoginScreen.this ,
                        Arrays.asList("public_profile" , "email"));

            }
        });

        findViewById(R.id.imggoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_type = 1;

                Sign_in_with_gmail();
            }
        });

        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {

                if(currentAccessToken==null)
                {

                }
                else
                {
                    loadUserProfile(currentAccessToken);
                }
            }
        };



    }

    //google Implimentation
    GoogleSignInClient mGoogleSignInClient;

    public void Sign_in_with_gmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String id = account.getId();
            String fname = "" + account.getGivenName();
            String lname = "" + account.getFamilyName();
            String email = "" + account.getEmail();

            String pic_url;
            if (account.getPhotoUrl() != null) {
                pic_url = account.getPhotoUrl().toString();
            } else {
                pic_url = "null";
            }

            if (fname.equals("") || fname.equals("null"))
                fname = getResources().getString(R.string.app_name);

            if (lname.equals("") || lname.equals("null"))
                lname = "User";


            issocial = true;

            social_name = fname +" "+ lname;
            social_email = email;
            social_id = id;

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//            if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
//            CALL_API_TO_VERIFY_SOCIELLOGIN(id, fname +" "+ lname, email);

        } else {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);

        }

    }

    int login_type = -1;
    private void CALL_API_TO_VERIFY_SOCIELLOGIN(String id, String name, String email,String gender) {

        HashMap params = new HashMap();
        params.put("email",""+email);
        params.put("name",""+name);
        params.put("gender",""+gender);
        params.put("source",""+login_type);
        params.put("referral_code",""+reffer_code);

        ApiRequest.Call_Api(this, Const.email_login, params, new Callback() {
            @Override
            public void Responce(String response, String type, Bundle bundle) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");


                    if (code.equalsIgnoreCase("201")) {

                        String token = jsonObject.getString("token");

                        if (jsonObject.has("user")) {
                            JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String mobile = jsonObject1.getString("mobile");


                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("user_id", id);
                            editor.putString("name", name);
                            editor.putString("mobile", mobile);
                            editor.putString("token", token);
                            editor.apply();

                            Intent i = new Intent(LoginScreen.this, Homepage.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            Functions.showToast(LoginScreen.this, "Login Successful");
                        } else {

                            if (jsonObject.has("message")) {
                                String message = jsonObject.getString("message");
                                Functions.showToast(LoginScreen.this, "Wrong mobile number or password");
                            }

                        }


                    } else if (code.equalsIgnoreCase("200")) {
                        String token = jsonObject.getString("token");
                        String user_id = jsonObject.getString("user_id");

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("user_id", user_id);
                        editor.putString("name", edtname.getText().toString());
                        editor.putString("mobile", edtPhone.getText().toString());
                        editor.putString("token", token);

                        editor.apply();

                        Intent i = new Intent(LoginScreen.this, Homepage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Functions.showToast(LoginScreen.this, "Login Successful");
//
                    } else {

                        if (jsonObject.has("message")) {
                            String message = jsonObject.getString("message");
                            Functions.showToast(LoginScreen.this, message);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    boolean issocial = false;
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String id = account.getId();
                String fname = "" + account.getGivenName();
                String lname = "" + account.getFamilyName();
                String email = "" + account.getEmail();


                // if we do not get the picture of user then we will use default profile picture

                String pic_url;
                if (account.getPhotoUrl() != null) {
                    pic_url = account.getPhotoUrl().toString();
                } else {
                    pic_url = "null";
                }


                if (fname.equals("") || fname.equals("null"))
                    fname = getResources().getString(R.string.app_name);

                if (lname.equals("") || lname.equals("null"))
                    lname = "User";

                issocial = true;

                social_name = fname +" "+ lname;
                social_email = email;
                social_id = id;

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


//                if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
//                CALL_API_TO_VERIFY_SOCIELLOGIN(id, fname +" "+ lname, email);

//                loginform(id, fname, lname, pic_url, "gmail");


            }
        } catch (ApiException e) {
            Log.w("Error message", "signInResult:failed code=" + e.getStatusCode());
        }

    }

    String social_name = "",social_email ="",social_id = "";
    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {

                if(object != null)
                {
                    try {
                        String first_name = object.optString("first_name");
                        String last_name = object.optString("last_name");
                        String email = object.optString("email");
//                    String bithday = object.getString("user_birthday");
                        String id;

                        id = object.getString("id");
                        String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";

//                    PrefMnger.putString(getActivity(),PrefMnger.NAME,first_name + " " +last_name);
//                    PrefMnger.putString(getActivity(),PrefMnger.EMAIL,email);
//                    PrefMnger.putString(getActivity(),PrefMnger.image_url,image_url);

//                    Intent intent = new Intent(LoginActivity.this , )

                        issocial = true;
                        social_name = first_name + " " +last_name;
                        social_email = email;
                        social_id = id;

//                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//                    if(!txtreffer.getText().toString().trim().equals("Enter Reffer code"))
                        CALL_API_TO_VERIFY_SOCIELLOGIN(id,first_name + " " +last_name,email,"Male");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Functions.showToast(context, "Server Error!");
                }


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }



    private void initFacebook()
    {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Functions.showToast(LoginScreen.this, "Login Cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Functions.showToast(LoginScreen.this, exception.getMessage());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void login(final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.SEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        handleResponse(response, value);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Functions.showToast(LoginScreen.this, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", edtPhone.getText().toString());
//                params.put("name", edtname.getText().toString());
//                params.put("gender", value.trim());
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


    private boolean formValidate() {

        if (edtPhone.getText().toString().isEmpty()) {
            return showToastNReturnFalse("Enter mobile number");
        } else if (edtname.getText().toString().isEmpty()) {
            return showToastNReturnFalse("Enter Name");
        }

        return true;
    }

    private boolean showToastNReturnFalse(String text) {
        Functions.showToast(this, text);
        return false;
    }


    private void handleResponse(String response, String value) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            String code = jsonObject.getString("code");


            if (code.equalsIgnoreCase("200")) {

                String otp_id = jsonObject.getString("otp_id");
                phoneLogin(otp_id, value);

            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(LoginScreen.this, message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void phoneLogin(final String otp_id, final String value) {
        // String phoneNumber= "+91"+edtPhone.getText().toString().trim();
        //SendVerificationCode(phoneNumber);
        final Dialog dialog = new Dialog(LoginScreen.this);
        dialog.setContentView(R.layout.dialogbox_ctivity);
        dialog.setTitle("Title...");
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
        edit_OTP = (EditText) dialog.findViewById(R.id.edit_OTP);

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView imglogin = (ImageView) dialog.findViewById(R.id.imglogin);

        imglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_OTP.getText().toString().length() > 0) {
                    String verify_code = edit_OTP.getText().toString();
                    VerifyCode(verify_code, otp_id, value);
                } else {
                    Functions.showToast(getApplicationContext(), "Please Enter OTP");

                }

            }
        });

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }


    private void VerifyCode(final String code, final String otp_id, final String value) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String code = jsonObject.getString("code");


                            if (code.equalsIgnoreCase("201")) {

                                String token = jsonObject.getString("token");

                                if (jsonObject.has("user")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONArray("user").getJSONObject(0);
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String mobile = jsonObject1.getString("mobile");


                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("user_id", id);
                                    editor.putString("name", name);
                                    editor.putString("mobile", mobile);
                                    editor.putString("token", token);
                                    editor.apply();

                                    Intent i = new Intent(LoginScreen.this, Homepage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    Functions.showToast(LoginScreen.this, "Login Successful");
                                } else {

                                    if (jsonObject.has("message")) {
                                        String message = jsonObject.getString("message");
                                        Functions.showToast(LoginScreen.this, "Wrong mobile number or password");
                                    }

                                }


                            } else if (code.equalsIgnoreCase("200")) {
                                String token = jsonObject.getString("token");
                                String user_id = jsonObject.getString("user_id");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("user_id", user_id);
                                editor.putString("name", edtname.getText().toString());
                                editor.putString("mobile", edtPhone.getText().toString());
                                editor.putString("token", token);

                                editor.apply();

                                Intent i = new Intent(LoginScreen.this, Homepage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                Functions.showToast(LoginScreen.this, "Login Successful");
//
                            } else {

                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(LoginScreen.this, message);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //  handleResponse(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Functions.showToast(LoginScreen.this, "Something went wrong");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", edit_OTP.getText().toString());
//                params.put("name", edtname.getText().toString());
                params.put("otp_id", otp_id.trim());
                params.put("mobile", edtPhone.getText().toString());
                params.put("name", edtname.getText().toString());
                params.put("gender", value.trim());
                params.put("referral_code", edtReferalCode.getText().toString().trim());
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


}
