package com.dextest;

import android.content.Context;
import android.widget.Toast;

public class ClacUtil {

    public void match(Context context) {
        int a = 10;
        int b = 1;
        Toast.makeText(context, "'计算结果'" + a / b, Toast.LENGTH_LONG).show();
    }

}
