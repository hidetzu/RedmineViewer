package com.redmine.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.*;

public class PostRequest {

	private HttpClient mHttpClient;
	private String     mURL;
	private ArrayList <NameValuePair> mParams;

	public  PostRequest(HttpClient httpClient,
			String url,
			ArrayList <NameValuePair> params) {
		this.mHttpClient = httpClient;
		this.mURL = url;
		this.mParams = params;
	}
	
	public String execute() {
		String ret = null;
    	
		//タイムアウト設定
		HttpParams httpParams = mHttpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 500*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 500*1000);

       HttpPost post = new HttpPost(this.mURL);
		HttpResponse response = null;
		try {
           post.setEntity(new UrlEncodedFormEntity(this.mParams, "utf-8"));
			response = mHttpClient.execute(post);
			if( response.getStatusLine().getStatusCode() == 200) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				response.getEntity().writeTo(outStream);
				ret = outStream.toString();
			}
		} catch (ClientProtocolException e) {
			ret = null;
		} catch (IOException e) {
			ret = null;
		}
		return ret;
	}
}
