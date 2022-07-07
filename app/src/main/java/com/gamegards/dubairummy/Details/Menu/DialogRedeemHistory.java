package com.gamegards.dubairummy.Details.Menu;

import static android.content.Context.MODE_PRIVATE;
import static com.gamegards.dubairummy.Activity.Homepage.MY_PREFS_NAME;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamegards.dubairummy.Interface.ApiRequest;
import com.gamegards.dubairummy.Interface.Callback;
import com.gamegards.dubairummy.MyAccountDetails.MyWinningAdapte;
import com.gamegards.dubairummy.MyAccountDetails.UserRedeemHistoryAdapter;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.RedeemCoins.RedeemModel;
import com.gamegards.dubairummy.SampleClasses.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DialogRedeemHistory {

    Dialog alert;
    Context context;
    TextView nofound;
    ProgressBar progressBar;
    RecyclerView rec_winning;
    MyWinningAdapte myWinningAdapte;

    public DialogRedeemHistory(Context context) {
        this.context = context;
        alert = new Dialog(context,android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        alert.setContentView(R.layout.dialog_historyredeem);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nofound = alert.findViewById(R.id.txtnotfound);
        progressBar = alert.findViewById(R.id.progressBar);
        rec_winning = alert.findViewById(R.id.rec_winning);
        rec_winning.setLayoutManager(new LinearLayoutManager(context));

        alert.findViewById(R.id.imgclosetop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        CALL_API_GET_LIST();
    }

    public void show(){alert.show();}
    public void dismiss(){alert.dismiss();}

    private void CALL_API_GET_LIST(){

        NoDataVisible(false);
        final ArrayList<RedeemModel> redeemModelArrayList = new ArrayList();

        HashMap<String, String> params = new HashMap<String, String>();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        params.put("user_id",prefs.getString("user_id", ""));

        ApiRequest.Call_Api(context, Const.USER_Redeem_History_LIST, params, new Callback() {
            @Override
            public void Responce(String resp, String type, Bundle bundle) {

                // progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

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
                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }

                if(redeemModelArrayList.size() <= 0)
                    NoDataVisible(true);

                UserRedeemHistoryAdapter userWinnerAdapter = new UserRedeemHistoryAdapter(context, redeemModelArrayList);
                rec_winning.setAdapter(userWinnerAdapter);


                HideProgressBar(true);
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
