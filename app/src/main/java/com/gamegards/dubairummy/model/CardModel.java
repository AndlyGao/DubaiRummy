package com.gamegards.dubairummy.model;

import java.util.ArrayList;

public class CardModel {

    public CardModel(int cardItem) {
        CardItem = cardItem;
    }
    public CardModel(String image) {
        Image = image;
    }

    public CardModel() {

    }

    public String user_id;
    public String winner_user_id;
    public String joker_card = "";
    public String status = "";
    public int score = 0;
    public int won = 0;
    public String user_name;
    public String game_id;


    public String group_value_params = "";
    public String group_value_response = "";



    public String id;
    public int CardItem;
    public String card_id;
    public String Image;
    public int tags;
    public String card_group;
    public int value_grp = 0;
    public String group_value = "Invalid";
    public String group_points = "0";
    public boolean isSelected;
    public ArrayList<CardModel> groups_cards;
    public int result = 0;
    public int packed = 0;

    @Override
    public String toString() {
        return "CardModel{" +
                "id='" + id + '\'' +
                ", CardItem=" + CardItem +
                ", Image='" + Image + '\'' +
                ", tags=" + tags +
                ", card_group='" + card_group + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
