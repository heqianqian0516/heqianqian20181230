package model;

import java.util.Map;

import callback.MyCallBack;

public interface IModel {
    void requestDataGet(String url, String params, Class clazz, MyCallBack myCallBack);
    void requestDataPost(String url, Map<String, String> params, Class clazz, MyCallBack myCallBack);
}
