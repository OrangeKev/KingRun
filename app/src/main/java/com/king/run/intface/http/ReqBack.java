package com.king.run.intface.http;

/**
 * 接口回调
 */

public interface ReqBack {

    void onSuccess(String result);

    void onError(String error);

}
