package com.swdn.utils;

/**
 * Created by lenovo on 2016/11/9.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
