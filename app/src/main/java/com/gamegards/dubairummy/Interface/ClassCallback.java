package com.gamegards.dubairummy.Interface;

import android.view.View;

import java.io.Serializable;

public interface ClassCallback extends Serializable {

    void Response(View v, int position, Object object);

}
