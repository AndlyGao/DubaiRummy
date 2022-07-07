package com.gamegards.dubairummy.Details;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gamegards.dubairummy.BaseActivity;
import com.gamegards.dubairummy.Details.Adapter.GameDetailsAdapter;
import com.gamegards.dubairummy.Details.Menu.DialogGameHistory;
import com.gamegards.dubairummy.Details.Menu.DialogRedeemHistory;
import com.gamegards.dubairummy.Details.Menu.DialogTransactionHistory;
import com.gamegards.dubairummy.Interface.ClassCallback;
import com.gamegards.dubairummy.R;

import java.util.ArrayList;
import java.util.List;

public class GameDetails_A extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        initGameDetailsList();
    }

    private void initGameDetailsList() {
        RecyclerView recyclerView = findViewById(R.id.recDetailsList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        GameDetailsAdapter detailsAdapter = new GameDetailsAdapter(this);
        DialogGameHistory dialogGameHistory = new DialogGameHistory(this);
        DialogRedeemHistory dialogRedeemHistory = new DialogRedeemHistory(this);
        DialogTransactionHistory dialogTransactionHistory = new DialogTransactionHistory(this);
        detailsAdapter.onItemSelectListener(new ClassCallback() {
            @Override
            public void Response(View v, int position, Object object) {
                if(position == 0)
                {
                    dialogGameHistory.show();
                }
                else if(position == 1)
                {
                    dialogRedeemHistory.show();
                }
                else if(position == 2)
                {
                    dialogTransactionHistory.show();
                }
            }
        });
        List<GameDetailsModel> gameDetailsModels = new ArrayList<>();

        gameDetailsModels.add(new GameDetailsModel("1","Games History",R.drawable.ic_game_console));
        gameDetailsModels.add(new GameDetailsModel("2","Redeems",R.drawable.ic_game_redeem));
        gameDetailsModels.add(new GameDetailsModel("3","Transactions",R.drawable.ic_game_transaction));
        detailsAdapter.setArrayList(gameDetailsModels);
        recyclerView.setAdapter(detailsAdapter);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}