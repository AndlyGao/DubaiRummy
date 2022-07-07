package com.gamegards.dubairummy._SevenUpGames;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gamegards.dubairummy.Interface.ClassCallback;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy.Utils.Functions;
import com.gamegards.dubairummy.Utils.SharePref;

import java.util.List;

public class SevenupAdapter extends RecyclerView.Adapter<SevenupAdapter.holder> {

    List<SevenupModel> arraylist;
    ClassCallback callback;
    Context context;

    public SevenupAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sevenup_setamount,parent,false));
    }

    public void setArraylist(List<SevenupModel> arraylist) {
        this.arraylist = arraylist;
        notifyDataSetChanged();
    }

    public void onItemSelectListener(ClassCallback callback){
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        SevenupModel model = arraylist.get(position);

        if(model != null)
            holder.bind(model);

    }

    @Override
    public int getItemCount() {
        return arraylist != null ? arraylist.size() : 0;
    }

    class holder extends RecyclerView.ViewHolder{
        public holder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(SevenupModel model){
            int postion = getAdapterPosition();
            ImageView ivContainerbg = itemView.findViewById(R.id.ivContainerbg);
            ivContainerbg.setBackground(Functions.getDrawable(context,R.drawable.ic_jackpot_rule_bg));
            ivContainerbg.clearAnimation();
            TextView tvJackpotHeading = itemView.findViewById(R.id.tvJackpotHeading);
            TextView tvJackpotamount = itemView.findViewById(R.id.tvJackpotamount);
            TextView tvJackpotSelectamount = itemView.findViewById(R.id.tvJackpotSelectamount);
            TextView tvUsersAddedAmount = itemView.findViewById(R.id.tvUsersAddedAmount);
            RelativeLayout rltAmountadded = itemView.findViewById(R.id.rltAmountadded);
            Button btninto = itemView.findViewById(R.id.btninto);

            tvJackpotHeading.setText(model.rule_type);
            tvJackpotamount.setText(""+model.added_amount);
            tvJackpotSelectamount.setText(""+model.select_amount);
            btninto.setText(model.into);

            btninto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Sevenup_A)context).playButtonTouchSound();
                    Animation animBounce = AnimationUtils.loadAnimation(context,
                            R.anim.bounce);
                    animBounce.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    itemView.startAnimation(animBounce);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.Response(v,postion,model);
                        }
                    },700);



                }
            });

            if(model.isAnimatedAddedAmount()
                    && model.getLast_added_id() != SharePref.getInstance().getInt(SharePref.lastAmountAddedID))
            {
                SharePref.getInstance().putInt(SharePref.lastAmountAddedID,model.getLast_added_id());
                model.setAnimatedAddedAmount(false);
                rltAmountadded.setVisibility(View.VISIBLE);
                tvUsersAddedAmount.setText("+"+model.getLast_added_amount());
                SlideToAbove(rltAmountadded);
            }

            if(model.isWine())
            {
                model.setWine(false);
                ivContainerbg.setBackground(Functions.getDrawable(context,R.drawable.ic_jackpot_rule_bg_selected));
                initiAnimation();
                ivContainerbg.startAnimation(blinksAnimation);
            }
        }
    }

    Animation blinksAnimation;
    private void initiAnimation() {
        blinksAnimation = AnimationUtils.loadAnimation(context,R.anim.blink);
        blinksAnimation.setDuration(500);
        blinksAnimation.setRepeatCount(Animation.INFINITE);
//        blinksAnimation.setStartOffset(700);
    }



    public void SlideToAbove(View animationView) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(2000);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        animationView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                animationView.clearAnimation();
                animationView.setVisibility(View.GONE);

//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        rl_footer.getWidth(), rl_footer.getHeight());
//                // lp.setMargins(0, 0, 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                rl_footer.setLayoutParams(lp);

            }

        });

    }

    public void SlideToDown(View animationView) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        animationView.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                rl_footer.clearAnimation();
//
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        rl_footer.getWidth(), rl_footer.getHeight());
//                lp.setMargins(0, rl_footer.getWidth(), 0, 0);
//                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                rl_footer.setLayoutParams(lp);

            }

        });

    }

}
