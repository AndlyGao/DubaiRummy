package com.gamegards.dubairummy.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.SampleClasses.Const;
import com.gamegards.dubairummy.Utils.FileUtils;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.gamegards.dubairummy.Activity.Homepage.MY_PREFS_NAME;;

public class UserInformation_BT extends BottomSheetDialogFragment {


    public UserInformation_BT() {
        // Required empty public constructor
    }

    Callback callback;
    public UserInformation_BT(Callback callback) {
        // Required empty public constructor
        this.callback = callback;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    View contentView;

    EditText edtText;
    Activity context;
    private String user_id, name, mobile, profile_pic, referral_code, wallet, game_played, bank_detail, adhar_card, upi;
    ProgressDialog progressDialog;
    String base_64 = "";


    @NonNull
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

     CoordinatorLayout.Behavior behavior;
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        contentView = View.inflate(getContext(), R.layout.dialog_user, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
         behavior = params.getBehavior();

        try {
            Field behaviorField = bottomSheetDialog.getClass().getDeclaredField("behavior");
            behaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) behaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING){
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        if (behavior != null && behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//        }

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
//        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        context = getActivity();

        Intilization();

    }

    ImageView img_diaProfile;
    EditText edtUsername,edtUserbank,edtUserupi,edtUseradhar;
    TextView txt_diaName ;
    TextView txt_diaPhone;
    TextView txt_bank;
    TextView txt_adhar;
    TextView txt_upi ;
    RadioButton radioBank;
    RadioButton radioUpi;
    Spinner spUserTpye;
    ArrayList<String> UserTpyeList;
    private void Intilization(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


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
                        // Funtions.showToast(context, token);
                        UserProfile();
                    }
                });


        ((View) contentView.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        img_diaProfile = contentView.findViewById(R.id.img_diaProfile);
         txt_diaName = contentView.findViewById(R.id.txt_diaName);
         txt_diaPhone = contentView.findViewById(R.id.txt_diaPhone);
         txt_bank = contentView.findViewById(R.id.txt_bank);
         txt_adhar = contentView.findViewById(R.id.txt_adhar);
         txt_upi = contentView.findViewById(R.id.txt_upi);


//        RadioGroup radio_details = contentView.findViewById(R.id.radio_details);
//        spUserTpye = contentView.findViewById(R.id.sp_profiletype);
//
//         UserTpyeList= new ArrayList<>();
//        UserTpyeList.add("Personal");
//        UserTpyeList.add("Agent");

//        spUserTpye.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,UserTpyeList));
//
//        radioBank = contentView.findViewById(R.id.radioBank);
//        radioUpi = contentView.findViewById(R.id.radioUpi);
//        contentView.findViewById(R.id.lnrBankDetails).setVisibility(View.VISIBLE);
//        contentView.findViewById(R.id.lnrUpi).setVisibility(View.GONE);

//        radio_details.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                RadioButton radioButton = contentView.findViewById(checkedId);
//
//                if(radioButton.getText().toString().trim().equals("Bank Details"))
//                {
//                    contentView.findViewById(R.id.lnrBankDetails).setVisibility(View.VISIBLE);
//                    contentView.findViewById(R.id.lnrUpi).setVisibility(View.GONE);
//                    radioBank.setChecked(true);
//                    radioUpi.setChecked(false);
//                }
//                else {
//                    contentView.findViewById(R.id.lnrBankDetails).setVisibility(View.GONE);
//                    contentView.findViewById(R.id.lnrUpi).setVisibility(View.VISIBLE);
//                    radioBank.setChecked(false);
//                    radioUpi.setChecked(true);
//                }
//
//
//            }
//        });

        edtUsername = contentView.findViewById(R.id.edtUsername);

        edtUserbank = contentView.findViewById(R.id.edtUserbank);
        edtUserupi = contentView.findViewById(R.id.edtUserupi);
        edtUseradhar = contentView.findViewById(R.id.edtUseradhar);

        final LinearLayout lnrUserinfo = contentView.findViewById(R.id.lnr_userinfo);
        final LinearLayout lnr_updateuser = contentView.findViewById(R.id.lnr_updateuser);
        lnrUserinfo.setVisibility(View.VISIBLE);
        lnr_updateuser.setVisibility(View.GONE);


        img_diaProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ((View) contentView.findViewById(R.id.img_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lnrUserinfo.getVisibility() == View.VISIBLE)
                {
                    lnrUserinfo.setVisibility(View.GONE);
                    lnr_updateuser.setVisibility(View.VISIBLE);
                }
                else {
                    lnrUserinfo.setVisibility(View.VISIBLE);
                    lnr_updateuser.setVisibility(View.GONE);
                }


            }
        });

        ((ImageView) contentView.findViewById(R.id.imgsub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!edtUsername.getText().toString().trim().equals("")) {
                    lnrUserinfo.setVisibility(View.VISIBLE);
                    lnr_updateuser.setVisibility(View.GONE);

                    UserUpdateProfile();


                } else {
                    Functions.showToast(context, "Input field in empty!");
                }

            }
        });



    }

    public String token = "";
    int version = 0 ;

    private void UserProfile() {

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("id", prefs.getString("user_id", ""));
        params.put("fcm", token);

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
                game_played = jsonObject0.optString("game_played");
                bank_detail = jsonObject0.optString("bank_detail");
                upi = jsonObject0.optString("upi");
                adhar_card = jsonObject0.optString("adhar_card");
                // txtName.setText("Welcome Back "+name);
//                long numberamount = Long.parseLong(wallet);

                edtUsername.setText("" + name);
                edtUserbank.setText("" + bank_detail);
                edtUserupi.setText("" + upi);
                edtUseradhar.setText("" + adhar_card);

                String setting = jsonObject.optString("setting");
                JSONObject jsonObjectSetting = new JSONObject(setting);

                String bank_detail_field = jsonObjectSetting.optString("bank_detail_field");
                String adhar_card_field = jsonObjectSetting.optString("adhar_card_field");
                String upi_field = jsonObjectSetting.optString("upi_field");

                SharePref.getInstance().putString(SharePref.Profile_Field3,bank_detail_field);
                SharePref.getInstance().putString(SharePref.Profile_Field4,adhar_card_field);
                SharePref.getInstance().putString(SharePref.Profile_Field5,upi_field);

                txt_diaName.setText(SharePref.getInstance().getString(SharePref.Profile_Field1,"Name")+": " + name);
                txt_diaPhone.setText(SharePref.getInstance().getString(SharePref.Profile_Field2,"Ph.No")+": " + mobile);
                txt_bank.setText(SharePref.getInstance().getString(SharePref.Profile_Field3,"Bank Details")+": " + bank_detail);
                txt_adhar.setText(SharePref.getInstance().getString(SharePref.Profile_Field4,"Addhar No")+": " + adhar_card);
                txt_upi.setText(SharePref.getInstance().getString(SharePref.Profile_Field5,"UPI")+": " + upi);

                Picasso.with(context).load(Const.IMGAE_PATH + profile_pic).into(img_diaProfile);

                edtUsername.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field1,"Name"));
//                edtUsername.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field2,"Ph.No"));
                edtUserbank.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field3,"Bank Details"));
                edtUseradhar.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field4,"Addhar No"));
                edtUserupi.setHint("Enter "+SharePref.getInstance().getString(SharePref.Profile_Field5,"UPI"));

//                edtUsername.setText("" + name);
//
//
//                txt_diaName.setText("Name: " + name);
//                txt_diaPhone.setText("Ph.No.:" + mobile);
//
//                if(Funtions.checkisStringValid(adhar_card))
//                {
//                    radioUpi.setChecked(true);
//                    radioBank.setChecked(false);
//                    etUpiNumber.setText("" + upi);
//                    txt_adhar.setText("Profile Details 1: " + adhar_card);
//                    txt_bank.setText("Mobile banking : " + upi);
//                    txt_upi.setVisibility(View.GONE);
//
//                    spUserTpye.setSelection(UserTpyeList.indexOf(adhar_card.trim()));
//
//                }
//                else {
//                    if(Funtions.checkisStringValid(bank_detail))
//                    {
//                        radioUpi.setChecked(false);
//                        radioBank.setChecked(true);
//                        String[] bank_detailsList = bank_detail.split(",");
//                        if(bank_detailsList.length >= 2)
//                        {
//                            etBankName.setText("" + bank_detailsList[0]);
//                            etBranch.setText("" + bank_detailsList[1]);
//                            etAccountnumber.setText("" + bank_detailsList[2]);
//
//                            txt_adhar.setText("Bank Name : " + bank_detailsList[0]);
//                            txt_bank.setText("Bank Branch :" + bank_detailsList[1]);
//                            txt_upi.setText("Bank Account no :" + bank_detailsList[2]);
//
//                        }
//                    }
//
//                }





                Picasso.with(context).load(Const.IMGAE_PATH + profile_pic)
                        .placeholder(R.drawable.avatar).into(img_diaProfile);



                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("profile_pic", profile_pic);
                editor.putString("bank_detail", bank_detail);
                editor.putString("upi", upi);
                editor.putString("adhar_card", adhar_card);
                editor.putString("mobile", mobile);
                editor.putString("referal_code", referral_code);
                editor.putString("img_name", profile_pic);
                editor.putString("wallet", wallet);
                editor.apply();


            } else if (code.equals("411")) {


            } else {

                if (jsonObject.has("message")) {
                    String message = jsonObject.getString("message");
                    Functions.showToast(context, message);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    if(Functions.check_permissions(context))
                        openCameraIntent();
                }

                else if (options[item].equals("Choose from Gallery"))
                {

                    if(Functions.check_permissions(context)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(context.getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        context.getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 1);
            }
        }
    }

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public  String getPath(Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    File image_file;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Matrix matrix = new Matrix();
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_file=new File(imageFilePath);
                Uri selectedImage =(Uri.fromFile(image_file));

                InputStream imageStream = null;
                try {
                    imageStream =context.getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

                Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.7), (int)(rotatedBitmap.getHeight()*0.7), true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);



                base_64= Functions.Bitmap_to_base64(context,resized);

                if(image_file!=null);
                Glide.with(this).load(resized).into(img_diaProfile);

                UserUpdateProfile();

            }

            else if (requestCode == 2) {
                Uri selectedImage = data.getData();

                progressDialog.show();
                new ImageSendingAsync().execute(selectedImage);

            }

        }

    }

    public class ImageSendingAsync extends AsyncTask<Uri, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri selectedImage = params[0];

            try {
                image_file= FileUtils.getFileFromUri(context,selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputStream imageStream = null;
            try {
                imageStream =context.getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

            String path=getPath(selectedImage);
            Matrix matrix = new Matrix();
            ExifInterface exif = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    exif = new ExifInterface(path);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

            Bitmap  resized = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.5), (int)(rotatedBitmap.getHeight()*0.5), true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            return resized;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();


            base_64= Functions.Bitmap_to_base64(context,bitmap);

            if(image_file!=null)
                Glide.with(context).load(bitmap).into(img_diaProfile);


            UserUpdateProfile();


            super.onPostExecute(bitmap);
        }
    }
    
    private void UserUpdateProfile() {

//        if(radioBank.isChecked())
//        {
//
//            if (Funtions.getStringFromEdit(etBankName).equals("")) {
//                Funtions.showToast(context, "Please Add Bank Name.");
//                return;
//            } else if (Funtions.getStringFromEdit(etBranch).equals("")) {
//                Funtions.showToast(context, "Please Add Bank Branch.");
//                return;
//            } else if (Funtions.getStringFromEdit(etBranch).equals("")) {
//                Funtions.showToast(context, "Please Add Bank Account number.");
//                return;
//            }
//
//        }
//        else {
//
//            if (Funtions.getStringFromEdit(etUpiNumber).equals("")) {
//                Funtions.showToast(context, "Please Add Mobile Bank number.");
//                return;
//            }
//            else  if (Funtions.getStringFromEdit(etUpiNumber).length() < 10) {
//                Funtions.showToast(context, "Please Add Valid Mobile Bank number.");
//                return;
//            }
//
//        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.USER_UPDATE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        Log.d("DATA_CHECK", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            if (code.equalsIgnoreCase("200")) {
//                                JSONObject jsonObject0 = jsonObject.getJSONArray("user_data").getJSONObject(0);
//                                user_id = jsonObject0.getString("id");
//                                name = jsonObject0.getString("name");
//                                mobile = jsonObject0.getString("mobile");
//                                profile_pic = jsonObject0.getString("profile_pic");
//                                referral_code = jsonObject0.getString("referral_code");
//                                wallet = jsonObject0.getString("wallet");
//                                // txtName.setText("Welcome Back "+name);
//                                long numberamount = Long.parseLong(wallet);
//                                txtwallet.setText("" + NumberFormat.getNumberInstance(Locale.US).format(numberamount));
//                                txtproname.setText(name);
//                                Picasso.with(context).load(Const.IMGAE_PATH + profile_pic).into(imaprofile);
//                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                                editor.putString("name", name);
//                                editor.putString("referal_code", referral_code);
//                                editor.apply();

//                                UserProfile();

                                if(callback != null)
                                    callback.Responce("update","",null);

                                dismiss();
                            } else {
                                if (jsonObject.has("message")) {
                                    String message = jsonObject.getString("message");
                                    Functions.showToast(context, message);
                                }

                            }
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
                SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                params.put("user_id", prefs.getString("user_id", ""));
                params.put("name", Functions.getStringFromEdit(edtUsername));
//                if(radioBank.isChecked())
//                {
//                    String bank_details = Funtions.getStringFromEdit(etBankName)
//                            +","+Funtions.getStringFromEdit(etBranch)
//                            +","+Funtions.getStringFromEdit(etAccountnumber);
//
//                    params.put("bank_detail", bank_details);
//                    params.put("adhar_card", "");
//                    params.put("upi", "");
//                }
//                else {
//                    params.put("bank_detail", "");
//                    params.put("adhar_card", Funtions.getStringFromSpinner(spUserTpye));
//                    params.put("upi", Funtions.getStringFromEdit(etUpiNumber));
//                }

                params.put("bank_detail", Functions.getStringFromEdit(edtUserbank));
                params.put("upi", Functions.getStringFromEdit(edtUserupi));
                params.put("adhar_card", Functions.getStringFromEdit(edtUseradhar));
                params.put("name", Functions.getStringFromEdit(edtUsername));


                params.put("profile_pic",""+base_64);
                params.put("token", prefs.getString("token", ""));
                Log.d("paremter_java", "getParams: " + params);
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


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


}
