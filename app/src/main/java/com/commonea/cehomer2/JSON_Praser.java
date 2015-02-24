package com.commonea.cehomer2;

/**
 * Created by Goutham on 13-01-2015.
 */

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSON_Praser {
    static JSONObject obj = null;
    public JSON_Praser() {
    }
    public JSONObject JSON_Post(String url, String id, String status, String username, String password) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        JSONObject json = new JSONObject();
        try {
            // JSON data:
            Log.d("JP", "Entered");
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            json.put("id", id);
            json.put("status", status);
            json.put("username", username);
            json.put("password", password);
            JSONArray postjson = new JSONArray();
            postjson.put(json);
            //Sleep

            // Post the data:
            httppost.setHeader("json", json.toString());
            httppost.getParams().setParameter("jsonpost", params);
            // Execute HTTP Post Request
            System.out.print(json);
            //Log.d("json", json.toString());
            HttpResponse response = httpclient.execute(httppost);
            // for JSON:
            Log.d("Response", "Got");
            if (response != null) {
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                obj = new JSONObject(String.valueOf(sb));
            }
        } catch (ClientProtocolException e) {
            Log.d("Exception", "ClientProtocolException");

            // TODO Auto-generated catch block
        } catch (IOException e) {
            obj=null;
            Log.d("Exception", "IOException");
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            Log.d("Exception", "JSONException");


        }
        Log.d("JPreturn", String.valueOf(obj));
        return obj;
    }

}


