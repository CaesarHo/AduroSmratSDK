package com.okhttp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by best on 2017/3/3.
 */

public class StrRequest extends StringRequest {

    private Map<String, String> postMap;
    public StrRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StrRequest(String url,Map<String, String> map ,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        postMap = map;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        return  super.getHeaders();
    }

    public void setPostMap(Map<String, String> map){}
}