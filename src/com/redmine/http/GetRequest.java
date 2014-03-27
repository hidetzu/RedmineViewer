package com.redmine.http;

import java.io.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.*;

public class GetRequest {
	private HttpClient mHttpClient;
	private String mURL;
	
	public GetRequest(HttpClient httpClient, String url) {
		this.mHttpClient = httpClient;
		this.mURL = url;
	}
	
	public String execute() {
		//タイムアウト設定(5秒)
		HttpParams httpParams = mHttpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 500*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 500*1000);
		
		String ret = null;
    	HttpGet request = new HttpGet(this.mURL);
    	try {
			HttpResponse response = mHttpClient.execute(request);
			if( response.getStatusLine().getStatusCode() == 200) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				response.getEntity().writeTo(outStream);
				ret =outStream.toString();			
			}			
		} catch (IOException e) {
			Log.d("GET ", "error");
			ret = null;
		}	
    	
    	return ret;
	}
}
