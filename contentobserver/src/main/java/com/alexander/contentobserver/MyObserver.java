package com.alexander.contentobserver;

import android.database.ContentObserver;
import android.os.Handler;

public class MyObserver extends ContentObserver {
    public MyObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }
}
